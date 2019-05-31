package akkaimpl.messages;

import akkaimpl.state.State;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message msg = (Message) o;
        return this.getContent() == msg.getContent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}
