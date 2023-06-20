package com.ascent.ui;

import com.ascent.util.ProtocolPort;
import com.ascent.util.UserDataClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import com.ascent.bean.Product;
/**
 * 管理员界面反馈处理模块
 * @author muniu
 * @version 1.0
 */

class FeedbackManagerUI extends JFrame {
    private JTable table;
    private int feedBackNum = 2;

    protected Socket hostSocket;
    /**
     * 输出流的引用
     */
    protected ObjectOutputStream outputToServer;

    /**
     * 输入流的引用
     */
    protected ObjectInputStream inputFromServer;

    public void LoadFeedBackClient() throws IOException {
        System.out.println("连接数据服务器..." + ProtocolPort.DEFAULT_HOST + ":" + ProtocolPort.DEFAULT_PORT);

        hostSocket = new Socket(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
        outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
        inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

        System.out.println("连接成功.");
    }

    public FeedbackManagerUI() {
        setTitle("反馈处理");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); //居中显示
        // TODO: 添加反馈处理界面的组件，比如表格、按钮、文本框等
        try {
            LoadFeedBackClient();
            outputToServer.writeInt(ProtocolPort.OP_GET_FEEDBACK_NUM);
            outputToServer.flush();
            System.out.println("发送成功！");
            feedBackNum = inputFromServer.readInt();
            System.out.println("接收成功！");
//            inputFromServer.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] columnNames = {"用户反馈数:" + String.valueOf(feedBackNum)};

        Object[][] rowData;
        if(feedBackNum > 0) {
            rowData = new Object[feedBackNum][1];
        } else {
            rowData = new Object[1][1];
        }
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        // 创建表格
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(50);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);

        if(feedBackNum > 0) {
            try {
                outputToServer.writeInt(ProtocolPort.OP_GET_FEEDBACK);
                outputToServer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < feedBackNum; i++) {
                Object line = null;
                try {

                    line = inputFromServer.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                tableModel.setValueAt(line, i, 0);
            }
        }
        // 设置单元格为可编辑
        table.setEnabled(false);
//        table.getTableHeader().setReorderingAllowed(false);
//        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

        // 创建带滚动条的面板，并添加表格
        JScrollPane scrollPane = new JScrollPane(table);

        // 创建主面板，并设置布局管理器
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 将主面板添加到窗口的内容面板中
        getContentPane().add(mainPanel);
    }

    public int getFeedBackNum() {
        System.out.println("FeedBackClient类：发送请求：OP_GEI_FEEDBACK_NUM");
        return 0;
    }
}
