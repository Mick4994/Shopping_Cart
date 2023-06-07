package com.ascent.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
public class ChatGUI extends JDialog {
    public JFrame frame;
    private JTextArea chatArea;
    private JTextField chatField;
    private JButton sendButton;

    public Socket socket;
    public PrintWriter os;
    public BufferedReader is;
    public ChatGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("ChatGUI");
        frame.setBounds(100, 100, 500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        chatArea = new JTextArea();
        chatArea.setBounds(10, 10, 460, 400);
        frame.getContentPane().add(chatArea);

        chatField = new JTextField();
        chatField.setBounds(10, 420, 350, 30);
        frame.getContentPane().add(chatField);
        chatField.setColumns(10);

        sendButton = new JButton("Send");
        sendButton.setBounds(370, 420, 100, 30);
        frame.getContentPane().add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatArea.append("Client: " + chatField.getText() + "\n");
                os.println(chatField.getText());
                chatField.setText("");
            }
        });
    }
}
