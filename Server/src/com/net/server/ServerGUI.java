package com.net.server;

import javax.swing.*;
import java.awt.event.*;

public class ServerGUI extends JDialog {

    private JPanel contentPane;
    private JButton stateButton;
    private JTextField portField;
    private JLabel statusLabel;

    private Server server;
    private String port;

    public ServerGUI() {
        configGUI();
        portField.setEnabled(true);
        statusLabel.setText("Stopped");
        stateButton.setText("Run");
    }

    private void changeState() {
        if(server != null && server.isRunning()) {
            statusLabel.setText("Stopped");
            stateButton.setText("Run");
            server.stopServer();
            portField.setEnabled(true);
        } else {
            statusLabel.setText("Running");
            stateButton.setText("Stop");
            this.port = portField.getText();
            portField.setEnabled(false);
            server =  new Server(Integer.valueOf(port));
        }
    }

    private void onCancel() {
        dispose();
    }

    private void configGUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(stateButton);
        setTitle("Server configuration form");

        stateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeState();
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
        ServerGUI dialog = new ServerGUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
