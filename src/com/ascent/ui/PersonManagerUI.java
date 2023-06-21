package com.ascent.ui;

import com.ascent.util.ProtocolPort;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

/**
 * ����Ա���������Ϣģ��
 * @author muniu
 * @version 1.0
 */

class PersonManagerUI extends JFrame {
    protected JLabel avatarLabel;
    protected JTextField birthdayText;
    protected JTextField nicknameText;
    protected Socket hostSocket;
    /**
     * �����������
     */
    protected ObjectOutputStream outputToServer;

    /**
     * ������������
     */
    protected ObjectInputStream inputFromServer;

    public PersonManagerUI() {
        setTitle("������Ϣ");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); //������ʾ

        // ����ͷ���
        avatarLabel = new JLabel();
        avatarLabel.setBounds(80, 10, 150, 150);
        add(avatarLabel);

        // ���������ı���
        JLabel birthdayLabel = new JLabel("����:");
        birthdayLabel.setBounds(10, 165, 50, 30);
        add(birthdayLabel);
        birthdayText = new JTextField();
        birthdayText.setBounds(70, 165, 150, 30);
        add(birthdayText);

        // �����ǳ��ı���
        JLabel nicknameLabel = new JLabel("�ǳ�:");
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

        // ��ȡ��������
        try {
            System.out.println("��������: OP_GET_PERSON_MSG");
            outputToServer.writeInt(ProtocolPort.OP_GET_PERSON_MSG);
            outputToServer.flush();
            System.out.println("��������...");

            // ��ȡͼ������
            String dataType = (String) inputFromServer.readObject();
            if (dataType.equals("image")) {
                byte[] imageData = (byte[]) inputFromServer.readObject();
                ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                BufferedImage avatarImage = ImageIO.read(bais);
                if (avatarImage != null) {
                    ImageIcon avatarIcon = new ImageIcon(avatarImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    avatarLabel.setIcon(avatarIcon);
                    System.out.println("ͷ���Ѹ���");
                } else {
                    System.out.println("�޷�����ͼ������");
                }
            } else {
                System.out.println("δ�յ�ͼ������");
            }

            // ��ȡ���պ��ǳ���Ϣ
            String fileContent = (String) inputFromServer.readObject();
            // �и��ļ�����
            String[] parts = fileContent.split(",");
            // ���и��Ĳ��ַ��õ��ı�����
            if (parts.length >= 2) {
                birthdayText.setText(parts[0]);
                nicknameText.setText(parts[1]);
            } else {
                System.out.println("�ļ����ݸ�ʽ����ȷ");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }



        // ��ʼ���������

        // ���ò��ֹ����������Ը�����Ҫѡ���������ֹ�������
        setLayout(null);
    }

    public void PersonManagerClient() throws IOException {
        System.out.println("�������ݷ�����..." + ProtocolPort.DEFAULT_HOST + ":" + ProtocolPort.DEFAULT_PORT);

        hostSocket = new Socket(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
        outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
        inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

        System.out.println("���ӳɹ�.");
    }
}
