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
 *�����һؽ���
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
     * Ĭ�Ϲ��췽������ʼ���û�ע�ᴰ��
     */
    public PwdRecoveryFrame() {
        this.setTitle("�����һ�");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel pwdRecPanel = new JPanel();

        JLabel phoneLabel = new JLabel("������绰��");
        JLabel userLabel = new JLabel("�����û����� ");
        JLabel codeLabel = new JLabel("������֤�룺");

        phoneNum = new JTextField(15);
        userText = new JTextField(15);
        code = new JPasswordField(10);
        JButton sendCode = new JButton("������֤��");
        JButton enter = new JButton("ȷ��");
        JButton exitButton = new JButton("�˳�");
        //��Ӹ����
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
        this.addWindowFocusListener(new WindowFocusListener() {// ���ø�����
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
     * ������֤�밴ť�¼�����
     * @author Long
     */
    public class SendCodeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tip.setText(userDataClient.sendCode(phoneNum.getText()));//������֤��
        }
    }


    /**
     * �˳���ť�¼�����
     * @author Long
     */
    class ExitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            setVisible(false);
            dispose();
        }
    }

    /**
     * ȷ�ϰ�ť�¼�����
     * @author Long
     */
    class EnterActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            // �û�ע�����
            String phone = phoneNum.getText();//�û�����ĵ绰
            String user = userText.getText();//�û�������˺�
            String msg = code.getText();//�û��������֤��
            tip.setText(userDataClient.searchPwd(phone, user , msg));
        }
    }

    /**
     * "�رմ���"�¼������ڲ���
     * @author Long
     */
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            setVisible(false);
            dispose();
        }
    }
}
