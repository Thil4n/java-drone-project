import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    String command = "";

    final DataInputStream DataInputstream;
    final DataOutputStream DataOutputstream;
    final Socket newSocket;
    final MessageCallback callback;

    public void SetCommand(String command) {
        this.command = command;
    }

    public ClientHandler(Socket newSocket, DataInputStream DataInputstream,
            DataOutputStream DataOutputstream, MessageCallback callback) {
        this.newSocket = newSocket;
        this.DataInputstream = DataInputstream;
        this.DataOutputstream = DataOutputstream;
        this.callback = callback;
    }

    @Override
    public void run() {
        String receivedString;
        String stringToReturn;

        int xBoundary = 430;
        int yBoundary = 430;

        int X;
        int Y;
        int DroneId = -1;

        try {

            while (!Thread.currentThread().isInterrupted()) {

                if (command.length() != 0) {
                    String[] cmdResult = command.split("-");

                    switch (cmdResult[0]) {
                        case "recall":

                            stringToReturn = "RECALLTOBASE";
                            DataOutputstream.writeUTF(stringToReturn);
                            System.out.println("Returning to the base\n");

                            break;

                        case "stop":
                            stringToReturn = "RETTOBASE";
                            DataOutputstream.writeUTF(stringToReturn);
                            System.out.println("Returning to the base & stopping\n");
                            break;

                        case "position":
                            if (Integer.parseInt(cmdResult[1]) == DroneId) {

                                stringToReturn = "POSITION-" + cmdResult[2] + "-" + cmdResult[3] + "-";
                                DataOutputstream.writeUTF(stringToReturn);
                                System.out.println("Changine the position\n");
                            }
                            break;
                    }

                    command = "";

                    continue;

                }

                receivedString = DataInputstream.readUTF();

                String[] result = receivedString.split("-");

                switch (result[0]) {

                    case "register":
                        callback.onMessageReceived("Registering the drone with id:" + result[1]);

                        stringToReturn = "registered";
                        DataOutputstream.writeUTF(stringToReturn);

                        try {
                            // Open file for writing
                            RandomAccessFile raf = new RandomAccessFile("./data/drones.bin", "rw");

                            // Seek to the end of the file
                            raf.seek(raf.length());

                            // Write the new drone data to the file
                            raf.writeInt(Integer.parseInt(result[1]));
                            raf.writeInt(0);
                            raf.writeInt(0);

                            // Close the file
                            raf.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("An error occurred.\n");
                        }

                        break;

                    case "position":
                        DroneId = Integer.parseInt(result[1]);
                        X = Integer.parseInt(result[2]);
                        Y = Integer.parseInt(result[3]);

                        if (xBoundary < X || yBoundary < Y) {
                            stringToReturn = "RETTOPREV";
                            System.out.println("Sending command to return to the previous position\n");
                            callback.onMessageReceived("Sending to the previous position the drone:" + result[1]);
                        } else {
                            stringToReturn = "ACK";
                            System.out.println("Acknowledging the position\n");
                            callback.onMessageReceived("Acknowledging the position of the drone " + DroneId);

                            try {
                                RandomAccessFile raf = new RandomAccessFile("./data/drones.bin", "rw");
                                int droneCount = (int) raf.length() / 12;

                                for (int i = 0; i < droneCount; i++) {
                                    int id = raf.readInt();
                                    int x = raf.readInt();
                                    int y = raf.readInt();

                                    if (id == DroneId) {
                                        raf.seek(i * 12); // go back to the beginning of the current record
                                        raf.writeInt(DroneId);
                                        raf.writeInt(X);
                                        raf.writeInt(Y);
                                    }
                                }
                                raf.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("An error occurred.\n");
                            }
                        }

                        DataOutputstream.writeUTF(stringToReturn);
                        break;

                    case "fire":
                        int fireId = Integer.parseInt(result[1]);
                        int droneId = Integer.parseInt(result[2]);
                        X = Integer.parseInt(result[3]);
                        Y = Integer.parseInt(result[4]);
                        int severity = Integer.parseInt(result[5]);

                        System.out.println("Fire signal received\n");
                        callback.onMessageReceived("Fire signal received from the drone " + droneId);

                        StringBuilder contentBuilder = new StringBuilder();
                        contentBuilder.append(fireId).append(",").append(droneId).append(",").append(X).append(",")
                                .append(Y).append(",").append(severity);

                        try (FileWriter fw = new FileWriter("data/fires.csv", true);
                                BufferedWriter bw = new BufferedWriter(fw)) {
                            bw.write(contentBuilder.toString());
                            bw.newLine();
                        } catch (IOException e) {
                            System.err.println("Error occurred while writing to the file: " + e.getMessage());
                        }
                        break;

                    default:
                        DataOutputstream.writeUTF("Invalid input\n");
                        break;
                }
            }
            stringToReturn = "RETTOBASE";
            DataOutputstream.writeUTF(stringToReturn);

        } catch (IOException e) {
            System.out.println("Client disconnected\n");
            // e.printStackTrace();
        }

    }
}