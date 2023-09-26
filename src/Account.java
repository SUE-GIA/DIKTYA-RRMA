//STEFANOS PANAGIOTIS GIANNAKOS 3568
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String username;
    private int authToken;
    private List<Message> messageBox;

    public Account(String username, int authToken) {
        this.username = username;
        this.authToken = authToken;
        this.messageBox = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public int getAuthToken() {
        return authToken;
    }

    public List<Message> getMessageBox() {
        return new ArrayList<>(messageBox);
    }

    public void addMessage(String sender, String receiver, String body, int id) {
        messageBox.add(new Message(sender, receiver, body, id));
    }

    public boolean deleteMessage(int messageID) {
        for (Message message : messageBox) {
            if (message.getId() == messageID) {
                messageBox.remove(message);
                return true;
            }
        }
        return false;
    }
}
