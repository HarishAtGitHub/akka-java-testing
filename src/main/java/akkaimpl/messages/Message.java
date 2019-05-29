package akkaimpl.messages;

public class Message {
    private int content;

    public Message(int content) {
        this.content = content;
    }

    public int getContent() {
        return this.content;
    }

    public void setContent(int state) {
        this.content = content;
    }

    public String toString() {
        return String.valueOf(this.content);
    }
}
