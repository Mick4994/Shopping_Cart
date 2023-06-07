package com.ascent.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
public class ChatGUI extends JDialog {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField chatField;
    private JButton sendButton;

    public Socket socket;
    public PrintWriter os;
    public BufferedReader is;
    public ChatGUI() {

    }
}
