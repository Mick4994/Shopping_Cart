package com.ascent.ui;

import com.ascent.util.UserDataClient;

import javax.swing.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.FileInputStream;
/**
 * 管理员界面个人信息模块
 * @author muniu
 * @version 1.0
 */

class PersonManagerUI extends JFrame {
    protected JTextField userText;

    protected JPasswordField password;

    protected JLabel tip;

    protected UserDataClient userDataClient;


    public PersonManagerUI() {
        setTitle("个人信息");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); //居中显示
        // TODO: 添加个人信息界面的组件，比如表格、按钮、文本框等
        String imageName = "R.png";

        ImageIcon recordingIcon = null;
        JLabel recordingLabel = null;
//        JPanel imagePanel = new JPanel();
        try {
            if (imageName.trim().length() == 0) {
                recordingLabel = new JLabel("  图片不存在  ");
            } else {
                recordingIcon = new ImageIcon(getClass().getResource("/images/" + imageName));
                recordingLabel = new JLabel(recordingIcon);
            }
        } catch (Exception exc) {
            recordingLabel = new JLabel("  图片不存在  ");
        }
        getContentPane().add(recordingLabel);
    }
}