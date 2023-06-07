package com.ascent.ui;

import java.awt.*;
import javax.swing.*;

/**
 * 管理员界面
 * @author muniu
 * @version 1.0
 */

public class AdministratorInterface extends JFrame {
    private JButton productBtn;
    private JButton userBtn;
    private JButton feedbackBtn;
    private JButton personBtn;

    public AdministratorInterface() {
        setTitle("管理员界面");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //设置了窗口关闭时的操作，即终止程序的运行。
        setLocationRelativeTo(null); //居中显示

        // 创建面板用于容纳按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        // 商品管理按钮
        productBtn = new JButton("商品管理");
        productBtn.addActionListener(e -> {
            // 打开商品管理窗口
            ProductManagerUI ui = new ProductManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(productBtn);

        // 用户管理按钮
        userBtn = new JButton("用户管理");
        userBtn.addActionListener(e -> {
            // 打开用户管理窗口
            UserManagerUI ui = new UserManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(userBtn);

        // 反馈处理按钮
        feedbackBtn = new JButton("反馈处理");
        feedbackBtn.addActionListener(e -> {
            // 打开反馈处理窗口
            FeedbackManagerUI ui = new FeedbackManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(feedbackBtn);

        // 个人信息按钮
        personBtn = new JButton("个人");
        personBtn.addActionListener(e -> {
            // 打开个人窗口
            PersonManagerUI ui = new PersonManagerUI();
            ui.setVisible(true);
        });
        buttonPanel.add(personBtn);

        // 将按钮面板添加到主界面的中间位置
        add(buttonPanel, BorderLayout.CENTER);
    }
}

