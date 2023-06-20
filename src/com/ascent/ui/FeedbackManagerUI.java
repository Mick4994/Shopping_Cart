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
 * ����Ա���淴������ģ��
 * @author muniu
 * @version 1.0
 */

class FeedbackManagerUI extends JFrame {
    private JTable table;
    private int feedBackNum = 2;

    protected Socket hostSocket;
    /**
     * �����������
     */
    protected ObjectOutputStream outputToServer;

    /**
     * ������������
     */
    protected ObjectInputStream inputFromServer;

    public void LoadFeedBackClient() throws IOException {
        System.out.println("�������ݷ�����..." + ProtocolPort.DEFAULT_HOST + ":" + ProtocolPort.DEFAULT_PORT);

        hostSocket = new Socket(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
        outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
        inputFromServer = new ObjectInputStream(hostSocket.getInputStream());

        System.out.println("���ӳɹ�.");
    }

    public FeedbackManagerUI() {
        setTitle("��������");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); //������ʾ
        // TODO: ��ӷ���������������������񡢰�ť���ı����
        try {
            LoadFeedBackClient();
            outputToServer.writeInt(ProtocolPort.OP_GET_FEEDBACK_NUM);
            outputToServer.flush();
            System.out.println("���ͳɹ���");
            feedBackNum = inputFromServer.readInt();
            System.out.println("���ճɹ���");
//            inputFromServer.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] columnNames = {"�û�������:" + String.valueOf(feedBackNum)};

        Object[][] rowData;
        if(feedBackNum > 0) {
            rowData = new Object[feedBackNum][1];
        } else {
            rowData = new Object[1][1];
        }
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        // �������
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
        // ���õ�Ԫ��Ϊ�ɱ༭
        table.setEnabled(false);
//        table.getTableHeader().setReorderingAllowed(false);
//        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

        // ����������������壬����ӱ��
        JScrollPane scrollPane = new JScrollPane(table);

        // ��������壬�����ò��ֹ�����
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ���������ӵ����ڵ����������
        getContentPane().add(mainPanel);
    }

    public int getFeedBackNum() {
        System.out.println("FeedBackClient�ࣺ��������OP_GEI_FEEDBACK_NUM");
        return 0;
    }
}
