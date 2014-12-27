package com.net.server;

import com.net.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Grigoriy on 12/24/2014.
 */
public class Server implements Runnable {

    private volatile TreeMap<String, ClientThread> clients;
    private volatile boolean isRunning = false;
    private volatile ServerSocket serverSocket;
    private int port;

    private DateFormat formatter;
    private Thread thread;
    private static Calendar calendar;

    public Server(int port) {
        clients = new TreeMap<String, ClientThread>();
        formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Socket socket = null;
        while (isRunning) {
            try {
                System.out.println("run");
                if(isRunning) {
                    socket = serverSocket.accept();
                }
                System.out.println("new client accepted");
                ClientThread newClient = new ClientThread(socket);
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        System.out.println("server stopping...");
        this.isRunning = false;
        for (ClientThread client : clients.values()) {
            client.interrupt();
        }
        System.out.println("server stopped");
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    /**
     * New instance of this class will created for each client-server connection
     */
    private class ClientThread implements Runnable {
        private Thread thread;
        private boolean isRunning;
        private volatile Socket socket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public ClientThread(Socket socket) {
            this.socket = socket;
            try {
                this.outputStream = new ObjectOutputStream(socket.getOutputStream());
                this.inputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.isRunning = true;
            thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run() {
            Message request;
            Message response = null;
            try {
                while (isRunning) {
                    try {
                        request = (Message) inputStream.readObject();
                        switch (request.getType()) {
                            case CONNECTION:
                                response = new Message(" : connected on     " + formatter.format(new Date()), null, request.getUserName(), Message.MessageType.CONNECTION);
                                Server.this.clients.put(request.getUserName(), this);
                                break;
                            case MESSAGE:
                                response = new Message(" : " + request.getMessage() + "     " + formatter.format(new Date()), null, request.getUserName(), Message.MessageType.MESSAGE);
                                break;
                            case DISCONNECTION:
                                response = new Message(" : disconnected at     " + formatter.format(new Date())
                                        , null, request.getUserName(), Message.MessageType.DISCONNECTION);
                                Server.this.clients.remove(request.getUserName());
                                isRunning = false;
                                break;
                        }
                        sendMessage(response);
                    } catch(NullPointerException e) {
                        //do nothing
                    } catch (SocketException e) {
                        //do nothing
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    System.out.println("socket closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public ObjectInputStream getInputStream() {
            return inputStream;
        }
        public ObjectOutputStream getOutputStream() {
            return outputStream;
        }
        public boolean isRunning() {return isRunning;}

        public void interrupt() {
            this.isRunning = false;
            thread.interrupt();
        }
    }

    private void sendMessage( Message msg) {
        Collection<ClientThread> col = clients.values();
        for (ClientThread clientThread : col) {
            try {
                clientThread.getOutputStream().writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
