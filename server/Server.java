import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class Server implements AdminPanelListener {
    private AdminPanel adminPanel;
    private ClientHandler myThread;
    List<Thread> threads = new ArrayList<>();

    public Server() {
        adminPanel = new AdminPanel();
        adminPanel.setListener(this);
        adminPanel.setVisible(true);
    }

    public void btnRecallClicked() {
        adminPanel.printText("Recalling all drones to the base");
        myThread.SetCommand("recall");

        try {
            // Open the file in read-write mode
            RandomAccessFile raf = new RandomAccessFile("./data/drones.bin", "rw");

            // Loop through the file until the end is reached
            while (raf.getFilePointer() < raf.length()) {
                // Read the ID, X, and Y values of the current drone
                int id = raf.readInt();
                int currentX = raf.readInt();
                int currentY = raf.readInt();

                // Update the X and Y values to 0
                raf.seek(raf.getFilePointer() - 8); // Move the file pointer back to the X value
                raf.writeInt(0); // Update the X value to 0
                raf.writeInt(0); // Update the Y value to 0
            }
            raf.close();
            System.out.println("All drone positions updated\n");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred.\n");
        }

    }

    public void btnStopServerClicked() {

        for (Thread thread : threads) {
            thread.interrupt();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {

            }
        }

        try {
            // Open file for reading and writing
            RandomAccessFile raf = new RandomAccessFile("./data/drones.bin", "rw");

            // Get the number of drone records in the file
            int droneCount = (int) raf.length() / 12;

            // Loop through all drone records and set x and y coordinates to 0
            for (int i = 0; i < droneCount; i++) {
                // Seek to the beginning of the current drone record
                raf.seek(i * 12);

                // Update x and y coordinates to 0
                raf.writeInt(0);
                raf.writeInt(0);
            }

            // Close the file
            raf.close();

            // Print message indicating that all drone records have been reset
            System.out.println("All drone records have been reset.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while resetting drone records.");
        }

    }

    public void btnChangeCordinatesClicked(int x, int y, int droneId) {
        adminPanel.printText("Changing cordinates of the drone " + droneId);

        try {
            // Open the file in read-write mode
            RandomAccessFile raf = new RandomAccessFile("./data/drones.bin", "rw");

            // Loop through the file until the end is reached
            while (raf.getFilePointer() < raf.length()) {
                // Read the ID, X, and Y values of the current drone
                int id = raf.readInt();
                int currentX = raf.readInt();
                int currentY = raf.readInt();

                if (id == droneId) {
                    // If the current drone matches the specified ID, update the X and Y values
                    raf.seek(raf.getFilePointer() - 8); // Move the file pointer back to the X value
                    raf.writeInt(x); // Update the X value
                    raf.writeInt(y); // Update the Y value
                    System.out.println("Drone position updated\n");
                    break; // Stop looping since the drone has been found and updated
                }
            }
            raf.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred.\n");
        }
    }

    public void btnDeleteFireReportClicked(int fireId) {
        adminPanel.printText("Deleting the fire report " + fireId + "\n");

        File inputFile = new File("data/fires.csv");
        File tempFile = new File("data/fires_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] values = currentLine.split(",");

                // Skip the header row
                if (values[0].equals("FireId")) {
                    continue;
                }

                int currentFireId = Integer.parseInt(values[0]);

                if (currentFireId == fireId) {
                    System.out.println("Fire case deleted\n");
                    continue;

                }

                writer.write(currentLine + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error occurred while reading or writing to the file: \n" +
                    e.getMessage());
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

    }

    public void run() {

        int Port = 5056;

        ServerSocket ServerSocket;
        try {
            ServerSocket = new ServerSocket(Port);
            adminPanel.printText("Server is listening on : " + Port + "\n");

            System.out.println("Server is listening on : " + Port + "\n");

            while (true) {
                Socket newSocket = null;

                try {
                    newSocket = ServerSocket.accept();

                    System.out.println("A new connection identified : " + newSocket + "\n");
                    adminPanel.printText("A new drone connected" + "\n");

                    DataInputStream DataInputstream = new DataInputStream(newSocket.getInputStream());
                    DataOutputStream DataOutputstream = new DataOutputStream(newSocket.getOutputStream());

                    MessageCallback callback = new MessageCallback() {
                        @Override
                        public void onMessageReceived(String message) {
                            // Handle the message received from the child thread here
                            // System.out.println("Received message from child thread: " + message);
                            adminPanel.printText(message + "\n");

                        }
                    };

                    myThread = new ClientHandler(newSocket, DataInputstream, DataOutputstream, callback);
                    threads.add(myThread);
                    myThread.start();

                    System.out.println("Thread assigned \n");

                } catch (Exception e) {
                    try {
                        newSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}