//STEFANOS PANAGIOTIS GIANNAKOS 3568
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    public static void main(String[] args) {
        ArrayList<Account> accounts = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Server's running!");
            int[] nextMessageID = {0};
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new MessagingClient(clientSocket, accounts, nextMessageID);
            }
        } catch (IOException e) {
            System.out.println("Server socket: " + e.getMessage());
        }
    }
}

class MessagingClient extends Thread {
    private final ArrayList<Account> accounts;
    private Socket clientSocket = null;
    private int[] nextMessageID;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private final ReentrantLock lock = new ReentrantLock();

    public MessagingClient(Socket aClientSocket, ArrayList<Account> accounts, int[] nextMessageID) {
        this.accounts = accounts;
        this.nextMessageID = nextMessageID;
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            start();
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    public void run() {
        try {
            String[] request = in.readUTF().split("~", -1);
            int FN_ID = Integer.parseInt(request[0]);
            System.out.println("Client " + clientSocket.getInetAddress() + ", Port " + clientSocket.getPort() + ", ID " + FN_ID);

            switch (FN_ID) {
                case 1 -> handleFunction1(request);
                case 2 -> handleFunction2(request);
                case 3 -> handleFunction3(request);
                case 4 -> handleFunction4(request);
                case 5 -> handleFunction5(request);
                case 6 -> handleFunction6(request);
            }
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Read line: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Close the socket once the server has replied.
            } catch (IOException e) {
                System.out.println("Close: " + e.getMessage()); // Close failed.
            }
        }
    }

    private void handleFunction1(String[] request) throws IOException {
        String username = request[1];
        if (getUser(username) == null && isValid(username)) {
            Account newAccount = new Account(username, createToken(4));
            lock.lock();
            try {
                accounts.add(newAccount);
            } finally {
                lock.unlock();
            }
            out.writeUTF(Integer.toString(newAccount.getAuthToken()));
        } else if (getUser(username) != null) {
            out.writeUTF("Sorry, the user already exists");
        } else if (!isValid(username)) {
            out.writeUTF("Invalid Username");
        }
    }

    private void handleFunction2(String[] request) throws IOException {
        int authToken = Integer.parseInt(request[1]);
        if (getUser(authToken) != null) {
            out.writeUTF(getUsernames());
        } else {
            out.writeUTF("Invalid Auth Token");
        }
    }

    private void handleFunction3(String[] request) throws IOException {
        int authToken = Integer.parseInt(request[1]);
        Account sender = getUser(authToken);
        Account recipient = getUser(request[2]);
        String message = request[3];
        if (sender != null && recipient != null) {
            lock.lock();
            try {
                nextMessageID[0]++;
                recipient.addMessage(sender.getUsername(), recipient.getUsername(), message, nextMessageID[0]);
            } finally {
                lock.unlock();
            }
            out.writeUTF("OK");
        } else if (recipient == null) {
            out.writeUTF("User does not exist");
        } else {
            out.writeUTF("Invalid Auth Token");
        }
    }

    private void handleFunction4(String[] request) throws IOException {
        int authToken = Integer.parseInt(request[1]);
        Account user = getUser(authToken);
        if (user != null) {
            String messages = getMessages(user);
            if (!messages.equals("")) out.writeUTF(messages);
            else out.writeUTF("The user's message box is empty");
        } else {
            out.writeUTF("Invalid Auth Token");
        }
    }

    private void handleFunction5(String[] request) throws IOException {
        int authToken = Integer.parseInt(request[1]);
        int messageID = Integer.parseInt(request[2]);
        Account user = getUser(authToken);
        if (user != null) {
            Message message = getMessage(user, messageID);
            if (message != null) {
                if (!message.isRead()) message.setRead(true);
                out.writeUTF("(" + message.getSender() + ")" + message.getBody());
            } else {
                out.writeUTF("Message ID does not exist");
            }
        } else {
            out.writeUTF("Invalid Auth Token");
        }
    }

    private void handleFunction6(String[] request) throws IOException {
        int authToken = Integer.parseInt(request[1]);
        int messageID = Integer.parseInt(request[2]);
        Account user = getUser(authToken);
        if (user != null) {
            if (deleteMessage(user, messageID)) {
                out.writeUTF("OK");
            } else {
                out.writeUTF("Message does not exist");
            }
        } else {
            out.writeUTF("Invalid Auth Token");
        }
    }

    //test to digit
    private int createToken(int digit_num) {
        if (digit_num < 1 || digit_num > 9) {
            digit_num = 4;
        }
        Random generator = new Random();

        String authToken = Integer.toString(generator.nextInt(9) + 1);
        for (int i = 0; i < (digit_num - 1); i++) {
            authToken = authToken.concat(Integer.toString(generator.nextInt(10)));
        }

        return Integer.parseInt(authToken);
    }

    private Account getUser(String username) {
        lock.lock();
        try {
            for (Account account : accounts) {
                if (account.getUsername().equals(username)) {
                    return account;
                }
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    private Account getUser(int authToken) {
        lock.lock();
        try {
            for (Account account : accounts) {
                if (account.getAuthToken() == authToken) {
                    return account;
                }
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    private boolean isValid(String username) {
        for (int i = 0; i < username.length(); i++) {
            if (!Character.isAlphabetic(username.charAt(i)) && !Character.isDigit(username.charAt(i)) && username.charAt(i) != '_') {
                return false;
            }
        }
        return true;
    }

    private String getUsernames() {
        StringBuilder usernames = new StringBuilder();
        lock.lock();
        try {
            for (int i = 0; i < accounts.size(); i++) {
                usernames.append(i + 1).append(". ").append(accounts.get(i).getUsername());
                if (i != accounts.size() - 1) {
                    usernames.append("\n");
                }
            }
        } finally {
            lock.unlock();
        }
        return usernames.toString();
    }

    private String getMessages(Account user) {
        StringBuilder messages = new StringBuilder();
        lock.lock();
        try {
            if (user != null) {
                ArrayList<Message> messageBox = (ArrayList<Message>) user.getMessageBox();
                for (int i = 0; i < messageBox.size(); i++) {
                    messages.append(messageBox.get(i).getId()).append(". from: ").append(messageBox.get(i).getSender()).append(messageBox.get(i).isRead() ? "" : "*");
                    if (i != messageBox.size() - 1) {
                        messages.append("\n");
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        return messages.toString();
    }

    private Message getMessage(Account user, int messageID) {
        lock.lock();
        try {
            if (user != null) {
                for (int i = 0; i < user.getMessageBox().size(); i++) {
                    if (user.getMessageBox().get(i).getId() == messageID) {
                        return user.getMessageBox().get(i);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        return null;
    }
    //sto pdf exei lathos ID, run me 6 ID.
    private boolean deleteMessage(Account user, int messageID) {
        if (user != null) {
            lock.lock();
            try {
                return user.deleteMessage(messageID);
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

}
