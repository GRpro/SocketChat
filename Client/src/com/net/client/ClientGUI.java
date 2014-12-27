package com.net.client;

import com.net.common.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JDialog {
    private JPanel contentPane;
    private JButton sendButton;
    private JButton closeButton;
    private JButton registerButton;

    private JTextField messageField;
    private JTextField userNameField;
    private JTextField hostField;
    private JTextField portField;
    private JTextArea chatArea;

    private Client client;

    public ClientGUI() {
        configGUI();
        chatArea.setEditable(false);
        chatArea.setSelectionColor(Color.blue);
        chatArea.setEnabled(false);
        sendButton.setEnabled(false);
        messageField.setEnabled(false);
    }

    private void onCancel() {
        Message msg = new Message(null , null, client.getUserName(), Message.MessageType.DISCONNECTION);
        client.sendMessage(msg);
        dispose();
    }

    private void onSend() {
        Message msg = new Message(messageField.getText(), null, client.getUserName(), Message.MessageType.MESSAGE);
        client.sendMessage(msg);
    }

    private void onConnect() {
        client = new Client(hostField.getText(), Integer.valueOf(portField.getText()), String.valueOf(userNameField.getText()), this);
        client.sendMessage(new Message(" : connected ", null, userNameField.getText(), Message.MessageType.CONNECTION));
        chatArea.setEnabled(true);
        sendButton.setEnabled(true);
        messageField.setEnabled(true);
        userNameField.setEnabled(false);
        hostField.setEnabled(false);
        portField.setEnabled(false);
        registerButton.setEnabled(false);
    }

    public void print(Message msg) {
        String s = msg.getUserName() + msg.getMessage() + "\n";
        chatArea.append(s);
    }

    private void configGUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(sendButton);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSend();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onConnect();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientGUI dialog = new ClientGUI();
                dialog.pack();
                dialog.setVisible(true);
                System.exit(0);
            }
        });

    }
}
