import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner reader = new Scanner(System.in);
            InetAddress ip = InetAddress.getByName("localhost");
            Socket socket = new Socket(ip, 6666);
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            while (true) {
                System.out.println(dataIn.readUTF());
                String toSend = reader.nextLine();
                dataOut.writeUTF(toSend);
                if (toSend.equals("Exit")) {
                    System.out.println("closing this connection: " + socket);
                    socket.close();
                    System.out.println("Connection closed");
                    break;
                } else if (toSend.equals("Message")) {
                    System.out.println("Enter your message:");
                    String message = reader.nextLine();
                    dataOut.writeUTF(message);
                    String received = dataIn.readUTF();
                    System.out.println(received);
                } else {
                    String received = dataIn.readUTF();
                    System.out.println(received);
                }
            }
            reader.close();
            dataIn.close();
            dataOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
