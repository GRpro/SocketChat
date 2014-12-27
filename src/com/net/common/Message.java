package com.net.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by Grigoriy on 12/24/2014.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum MessageType {MESSAGE, CONNECTION, DISCONNECTION};

    private MessageType type;
    private String message;
    private Object data;
    private String userName;

    public Message(String message, Object data, String userName, MessageType type) {
        this.message = message;
        this.data = data;
        this.userName = userName;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
