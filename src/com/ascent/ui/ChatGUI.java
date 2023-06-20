package com.ascent.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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

    public boolean is_chatting = false;

    public ChatGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("ChatGUI");
        frame.setBounds(200, 200, 500, 500);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("关闭客服聊天");
                is_chatting = false;
            }
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

    }
}
