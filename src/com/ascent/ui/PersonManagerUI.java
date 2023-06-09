package com.ascent.ui;

import com.ascent.util.ProtocolPort;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * 管理员界面个人信息模块
 *
 * @author muniu
 * @version 1.0
 */

class PersonManagerUI extends JFrame {
    protected JLabel avatarLabel;
    protected JTextField birthdayText;
    protected JTextField nicknameText;
    protected Socket hostSocket;
    JButton saveButton;
    /**
     * 输出流的引用
     */
    protected ObjectOutputStream outputToServer;

    /**
     * 输入流的引用
     */
    protected ObjectInputStream inputFromServer;

    public PersonManagerUI() {
        setTitle("个人信息");
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); //居中显示

        // 创建头像框
        avatarLabel = new JLabel();
        avatarLabel.setBounds(85, 10, 150, 150);
        add(avatarLabel);

        // 创建生日文本框
        JLabel birthdayLabel = new JLabel("生日:");
        birthdayLabel.setBounds(10, 165, 50, 30);
        add(birthdayLabel);
        birthdayText = new JTextField();
        birthdayText.setBounds(70, 165, 150, 30);
        add(birthdayText);

        // 创建昵称文本框
        JLabel nicknameLabel = new JLabel("昵称:");
        nicknameLabel.setBounds(10, 205, 50, 30);
        add(nicknameLabel);
        nicknameText = new JTextField();
        nicknameText.setBounds(70, 205, 150, 30);
        add(nicknameText);

        try {
            PersonManagerClient();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取个人数据
        try {
            System.out.println("发送请求: OP_GET_PERSON_MSG");
            outputToServer.writeInt(ProtocolPort.OP_GET_PERSON_MSG);
            outputToServer.flush();
            System.out.println("接收数据...");

            // 读取图像数据
            String dataType = (String) inputFromServer.readObject();
            if (dataType.equals("image")) {
                byte[] imageData = (byte[]) inputFromServer.readObject();
                ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                BufferedImage avatarImage = ImageIO.read(bais);
                if (avatarImage != null) {
                    ImageIcon avatarIcon = new ImageIcon(avatarImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    avatarLabel.setIcon(avatarIcon);
                    System.out.println("头像已更新");
                } else {
                    System.out.println("无法解析图像数据");
                }
            } else {
                System.out.println("未收到图像数据");
            }

            // 读取生日和昵称信息
            String fileContent = (String) inputFromServer.readObject();
            // 切割文件内容
            String[] parts = fileContent.split(",");
            // 将切割后的部分放置到文本框中
            if (parts.length >= 2) {
                birthdayText.setText(parts[0]);
                nicknameText.setText(parts[1]);
            } else {
                System.out.println("文件内容格式不正确");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 创建更换头像菜单
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("菜单");
        JMenuItem changeAvatarMenuItem = new JMenuItem("更换头像");
        menu.add(changeAvatarMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        changeAvatarMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    sendAvatarImage(selectedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        // 创建保存按钮
        saveButton = new JButton("保存");
        saveButton.setBounds(100, 250, 80, 30);
        add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                savePersonInfo();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // 初始化其他组件

        // 设置布局管理器（可以根据需要选择其他布局管理器）
        setLayout(null);
    }

    public void PersonManagerClient() throws IOException {
        System.out.println("连接数据服务器..." + ProtocolPort.DEFAULT_HOST + ":" + ProtocolPort.DEFAULT_PORT);

        hostSocket = new Socket(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
        outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
        inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

        System.out.println("连接成功.");
    }

    private void sendAvatarImage(File file) throws IOException {
        if (file.exists()) {
            System.out.println("发送请求: OP_SAVE_PERSON_IMAGE_MSG");
            outputToServer.writeInt(ProtocolPort.OP_SAVE_PERSON_IMAGE_MSG);
            outputToServer.flush();
            byte[] imageData = Files.readAllBytes(file.toPath());
            outputToServer.writeObject("image");
            outputToServer.writeObject(imageData);
            outputToServer.flush();
            System.out.println("头像已导入");
            // 更新头像
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            BufferedImage avatarImage = ImageIO.read(bais);
            if (avatarImage != null) {
                ImageIcon avatarIcon = new ImageIcon(avatarImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                avatarLabel.setIcon(avatarIcon);
                System.out.println("头像已更新");
            } else {
                System.out.println("无法解析图像数据");
            }
        } else {
            System.out.println("文件不存在");
        }
    }

    private void savePersonInfo() throws IOException {
        String birthday = birthdayText.getText();
        String nickname = nicknameText.getText();
        String fileContent = birthday + "," + nickname;

        System.out.println("发送请求: OP_SAVE_PERSON_INFO");
        outputToServer.writeInt(ProtocolPort.OP_SAVE_PERSON_INFO);
        outputToServer.flush();

        outputToServer.writeObject(fileContent);
        outputToServer.flush();
        System.out.println("个人信息已保存");
    }
}
