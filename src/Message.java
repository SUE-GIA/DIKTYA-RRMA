//STEFANOS PANAGIOTIS GIANNAKOS 3568
public class Message {
    private boolean isRead;
    private String sender;
    private String receiver;
    private String body;
    private int id;

    public Message( String sender, String receiver, String body, int id) {
        this.isRead = false;
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }
}
