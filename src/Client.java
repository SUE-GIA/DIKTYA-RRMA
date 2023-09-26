//STEFANOS PANAGIOTIS GIANNAKOS 3568
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("java Client <hostname> <port> <FN_ID> <args>");
            return;
        }

        int FN_ID = Integer.parseInt(args[2]);

        if (FN_ID < 1 || FN_ID > 6) {
            System.out.println("The ID needs to be between 1 and 6");
            return;
        }

        try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]))) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            List<String> request = new ArrayList<>();
            request.add(Integer.toString(FN_ID));

            switch (FN_ID) {
                case 1, 2, 4 -> request.add(args[3]);
                case 3 -> {
                    request.add(args[3]);
                    request.add(args[4]);
                    request.add(args[5]);
                }
                case 5, 6 -> {
                    request.add(args[3]);
                    request.add(args[4]);
                }
            }

            out.writeUTF(String.join("~", request));
            String reply = in.readUTF();
            System.out.print(reply);
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Read line: " + e.getMessage());
        }
    }
}
