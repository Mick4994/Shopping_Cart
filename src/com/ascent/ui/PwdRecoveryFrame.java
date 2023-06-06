package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;

import javax.swing.*;

import com.ascent.util.UserDataClient;

/**
 *密码找回界面
 * @author Long
 * @version 1.0
 */
public class PwdRecoveryFrame extends JFrame {
    private JTextField phoneNum;

    private JTextField userText;

    private JTextField code;

    private JLabel tip;

    private UserDataClient userDataClient;

    /**
     * 默认构造方法，初始化用户注册窗体
     */
    public PwdRecoveryFrame() {
        this.setTitle("密码找回");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel pwdRecPanel = new JPanel();

        JLabel phoneLabel = new JLabel("请输入电话：");
        JLabel userLabel = new JLabel("输入用户名： ");
        JLabel codeLabel = new JLabel("输入验证码：");

        phoneNum = new JTextField(15);
        userText = new JTextField(15);
        code = new JPasswordField(10);
        JButton sendCode = new JButton("发送验证码");
        JButton enter = new JButton("确认");
        JButton exitButton = new JButton("退出");
        //添加各组件
        pwdRecPanel.add(phoneLabel);
        pwdRecPanel.add(new JScrollPane(phoneNum));
        pwdRecPanel.add(userLabel);
        pwdRecPanel.add(new JScrollPane(userText));
        pwdRecPanel.add(codeLabel);
        pwdRecPanel.add(new JScrollPane(code));
        pwdRecPanel.add(enter);
        pwdRecPanel.add(sendCode);
        pwdRecPanel.add(exitButton);

        setResizable(false);
        setSize(260, 180);
        setLocation(300, 100);

        JPanel tipPanel = new JPanel();

        tip = new JLabel();

        tipPanel.add(tip);

        container.add(BorderLayout.CENTER, pwdRecPanel);
        container.add(BorderLayout.NORTH, tip);

        enter.addActionListener(new PwdRecoveryFrame.EnterActionListener());
        exitButton.addActionListener(new PwdRecoveryFrame.ExitActionListener());
        sendCode.addActionListener(new PwdRecoveryFrame.SendCodeActionListener());
        this.addWindowListener(new PwdRecoveryFrame.WindowCloser());
        this.addWindowFocusListener(new WindowFocusListener() {// 设置父窗口
            public void windowGainedFocus(WindowEvent e) {
            }
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().toFront();
            }
        });
        try {
            userDataClient = new UserDataClient();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 发送验证码按钮事件监听
     * @author Long
     */
    public class SendCodeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tip.setText(userDataClient.sendCode(phoneNum.getText()));//发送验证码
        }
    }


    /**
     * 退出按钮事件监听
     * @author Long
     */
    class ExitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            setVisible(false);
            dispose();
        }
    }

    /**
     * 确认按钮事件监听
     * @author Long
     */
    class EnterActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            // 用户注册操作
            String phone = phoneNum.getText();//用户输入的电话
            String user = userText.getText();//用户输入的账号
            String msg = code.getText();//用户输入的验证码
            tip.setText(userDataClient.searchPwd(phone, user , msg));
        }
    }

    /**
     * "关闭窗口"事件处理内部类
     * @author Long
     */
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            dispose();
        }
    }
}
