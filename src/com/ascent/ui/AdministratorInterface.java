package com.ascent.ui;

import java.awt.*;
import javax.swing.*;

/**
 * ����Ա����
 * @author muniu
 * @version 1.0
 */

public class AdministratorInterface extends JFrame {
    private JButton productBtn;
    private JButton userBtn;
    private JButton feedbackBtn;
    private JButton personBtn;

    public AdministratorInterface() {
        setTitle("����Ա����");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //�����˴��ڹر�ʱ�Ĳ���������ֹ��������С�
        setLocationRelativeTo(null); //������ʾ

        // ��������������ɰ�ť
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        // ��Ʒ����ť
        productBtn = new JButton("��Ʒ����");
        productBtn.addActionListener(e -> {
            // ����Ʒ������
            ProductManagerUI ui = new ProductManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(productBtn);

        // �û�����ť
        userBtn = new JButton("�û�����");
        userBtn.addActionListener(e -> {
            // ���û�������
            UserManagerUI ui = new UserManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(userBtn);

        // ��������ť
        feedbackBtn = new JButton("��������");
        feedbackBtn.addActionListener(e -> {
            // �򿪷���������
            FeedbackManagerUI ui = new FeedbackManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(feedbackBtn);

        // ������Ϣ��ť
        personBtn = new JButton("����");
        personBtn.addActionListener(e -> {
            // �򿪸��˴���
            PersonManagerUI ui = new PersonManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(personBtn);

        // ����ť�����ӵ���������м�λ��
        add(buttonPanel, BorderLayout.CENTER);
    }
}

