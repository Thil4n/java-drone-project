import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Client {

    public boolean randomBoolean() {
        return Math.random() < 0.5;
    }

    public static void main(String[] args) throws IOException {
        try {

            Socket socket = new Socket("localhost", 5056);
            DataInputStream DataInputstream = new DataInputStream(socket.getInputStream());
            DataOutputStream DataOutputstream = new DataOutputStream(socket.getOutputStream());

            String toSend;
            String recived;

            Random random = new Random();

            int droneId = random.nextInt(100);
            String droneName = "IBDMS_" + droneId;

            System.out.println("Satarting registration process");
            System.out.println("Drone ID : " + droneId + " | Drone Name : " + droneName);
            System.out.println("Initial position : X = 0 | Y = 0");

            toSend = "register-" + droneId + "-" + droneName + "-0-0";

            DataOutputstream.writeUTF(toSend);
            recived = DataInputstream.readUTF();

            Position oldPosition = new Position(0, 0);

            Boolean end = false;

            while (!end) {

                Position currentPosition = new Position(random.nextInt(450), random.nextInt(450));

                toSend = "position-" + droneId + "-" + currentPosition.getX() + "-" + currentPosition.getY();

                System.out.println(
                        "Current position sent : X = " + currentPosition.getX() + " | Y = " + currentPosition.getY());

                DataOutputstream.writeUTF(toSend);

                recived = DataInputstream.readUTF();

                String[] result = recived.split("-");

                switch (result[0]) {

                    case "RETTOPREV":
                        System.out.println("Returning to the previus position\n");
                        currentPosition = oldPosition;
                        break;

                    case "RECALLTOBASE":
                        System.out.println("Returning to the base\n");
                        currentPosition.setX(0);
                        currentPosition.setY(0);

                        toSend = "ACK";
                        DataOutputstream.writeUTF(toSend);
                        break;
                    case "RETTOBASE":
                        System.out.println("Returning to the base\n");
                        currentPosition.setX(0);
                        currentPosition.setY(0);

                        toSend = "ACK";
                        DataOutputstream.writeUTF(toSend);
                        end = true;
                        break;
                    case "POSITION":
                        System.out.println("Changine the position\n");
                        int X = Integer.parseInt(result[1]);
                        int Y = Integer.parseInt(result[2]);
                        currentPosition.setX(X);
                        currentPosition.setY(Y);
                        break;

                    case "ACK":
                        oldPosition = currentPosition;
                        break;
                }

                Boolean isFire = Math.random() < 0.05;
                int fireId = random.nextInt(100);

                if (isFire) {
                    toSend = "fire-" + fireId + "-" + droneId + "-" + currentPosition.getX() + "-"
                            + currentPosition.getY()
                            + "-"
                            + random.nextInt(
                                    10);
                    DataOutputstream.writeUTF(toSend);
                }
                TimeUnit.SECONDS.sleep(10);

            }

            socket.close();
            DataInputstream.close();
            DataOutputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
