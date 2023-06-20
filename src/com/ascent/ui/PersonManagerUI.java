package com.ascent.ui;

import com.ascent.util.UserDataClient;

import javax.swing.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.FileInputStream;
/**
 * ����Ա���������Ϣģ��
 * @author muniu
 * @version 1.0
 */

class PersonManagerUI extends JFrame {
    protected JTextField userText;

    protected JPasswordField password;

    protected JLabel tip;

    protected UserDataClient userDataClient;


    public PersonManagerUI() {
        setTitle("������Ϣ");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); //������ʾ
        // TODO: ��Ӹ�����Ϣ���������������񡢰�ť���ı����
        String imageName = "R.png";

        ImageIcon recordingIcon = null;
        JLabel recordingLabel = null;
//        JPanel imagePanel = new JPanel();
        try {
            if (imageName.trim().length() == 0) {
                recordingLabel = new JLabel("  ͼƬ������  ");
            } else {
                recordingIcon = new ImageIcon(getClass().getResource("/images/" + imageName));
                recordingLabel = new JLabel(recordingIcon);
            }
        } catch (Exception exc) {
            recordingLabel = new JLabel("  ͼƬ������  ");
        }
        getContentPane().add(recordingLabel);
    }
}