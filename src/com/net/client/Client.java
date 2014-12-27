package com.net.client;

import com.net.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Grigoriy on 12/24/2014.
 */
public class Client implements Runnable {

    private volatile Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String userName;

    private boolean isRunning = false;
    private Thread thread;
    private ClientGUI gui;

    public Client(String host, int port, String userName, ClientGUI gui) {
        this.userName = userName;
        this.gui = gui;
        try {
            System.out.println(host);
            System.out.println(port);
            socket = new Socket(host, port);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream((socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    @Override
    public void run() {
        while(isRunning) {
            try {
                Message m = (Message) inputStream.readObject();
                System.out.println(m.getMessage());
                gui.print(m);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Message m) {
        try {
            outputStream.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }
}
