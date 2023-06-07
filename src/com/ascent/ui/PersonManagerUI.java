package com.ascent.ui;

import com.ascent.util.UserDataClient;

import javax.swing.*;

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

    }
}