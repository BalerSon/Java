package src.people.control;

import java.util.Map;
import java.util.List;

public class Command {
    private String op;
    private Map<String, Object> payload;

    public Command() {}

    public Command(String op, Map<String, Object> payload) {
        this.op = op;
        this.payload = payload;
    }

    public String getOp() {
        return op;
    }
    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setOp(String op) {
        this.op = op;
    }
    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Command{op='" + op + "', payload=" + payload + "}";
    }
}