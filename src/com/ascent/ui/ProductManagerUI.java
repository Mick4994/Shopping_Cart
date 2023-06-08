package com.ascent.ui;

import com.ascent.bean.Product;
import com.ascent.util.ProductDataClient;
import com.ascent.util.UserDataClient;

import javax.swing.*;

/**
 * ����Ա���������Ϣģ��
 * @author muniu
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ProductManagerUI extends JFrame {
    private JTable table;
    private ProductDataClient productDataClient;

    public ProductManagerUI() {
        try {
            productDataClient = new ProductDataClient(); // ʵ����UserDataClient
        } catch (IOException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayProductData();
                setVisible(true);
            }
        });
        setTitle("��Ʒ����");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // ������ʾ

        // �������ģ��
        String[] columnNames = {"ҩƷ����", "��ѧ��ժ�ǼǺ�", "�ṹͼ����", "��ʽ", "�۸�", "����", "���"};
        Object[][] rowData = new Object[100][7];
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);

        // �������
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);

        // ���õ�Ԫ��Ϊ�ɱ༭
        table.setEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

        // ����������������壬����ӱ��
        JScrollPane scrollPane = new JScrollPane(table);

        // ��������壬�����ò��ֹ�����
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ���������ӵ����ڵ����������
        getContentPane().add(mainPanel);

        // ��ʾ��Ʒ��������
        displayProductData();
    }

    public void displayProductData() {
        // ���������
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        //tableModel.setRowCount(0); // ��ձ������
        // ��ȡ��Ʒ����
        // ��ȡ��Ʒ����
        try {
            ArrayList<String> category = productDataClient.getCategories();
            int row = 0; // ��ʼ������
            for (String categoryName : category) {
                // ������ִ�ж�ÿ������Ĳ���
                // ��ȡ��Ʒ��������
                ArrayList<Product> productList = productDataClient.getProducts(categoryName);
                System.out.println("��Ʒ���ࣺ" + categoryName);
                for (Product product : productList) {
                    String data = product.getProductname();
                    String pattern = "\\d+";
                    Pattern regex = Pattern.compile(pattern);
                    Matcher matcher = regex.matcher(data);
                    if (matcher.find()) {
                        String numberStr = matcher.group();
                        int number = Integer.parseInt(numberStr);
                        System.out.println("��ȡ�����֣�" + number);
                        row = number - 1;
                    } else {
                        System.out.println("δ�ҵ�����");
                        row++;
                    }
                    Object[] rowData = {product.getProductname(), product.getCas(), product.getStructure(), product.getFormula(),
                            product.getPrice(), product.getRealstock(), product.getCategory()};
                    for (int i = 0; i < rowData.length; i++) {
                        tableModel.setValueAt(rowData[i], row, i);
                    }
                    // row++; // ��������
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
