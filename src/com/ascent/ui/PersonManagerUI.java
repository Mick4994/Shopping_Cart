package com.ascent.ui;

import com.ascent.util.UserDataClient;

import javax.swing.*;

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

    }
}