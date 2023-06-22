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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ProductManagerUI extends JFrame {
    private JTable table;
    private ProductDataClient productDataClient;

    private Object[][] productData;
    //private int row = 0;

    private JButton saveButton;//���水ť
    private JButton addButton;//��Ӱ�ť
    private JButton delButton;//ɾ����ť

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

        int row1 = 0;
        try {
            ArrayList<String> category = productDataClient.getCategories();
            for (String categoryName : category) {
                ArrayList<Product> productList = productDataClient.getProducts(categoryName);
                for (Product product : productList) {
                    row1++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        productData = new Object[row1][7];

        DefaultTableModel tableModel = new DefaultTableModel(productData, columnNames);

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

        JPanel buttonPanel = new JPanel();
        //���ñ��水ť
        saveButton = new JButton("����");
        saveButton.setSize(102, 52);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProductData ();
            }
        });
        //�������Ӱ�ť
        addButton = new JButton("���");
        addButton.setSize(102, 52);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] rowData = new Object[7];
                tableModel.addRow(rowData);
            }
        });
        //����ɾ����ť
        delButton = new JButton(("ɾ��"));
        delButton.setSize(102, 52);
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();// ��ȡѡ�е���
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);// �ӱ����ɾ��ѡ����
                }
            }
        });
        //_________
        buttonPanel.add(saveButton);
        buttonPanel.add(addButton);
        buttonPanel.add(delButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        //saveProductData ();
    }

    public void displayProductData () {
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
                        //row = number - 1;
                    } else {
                        System.out.println("δ�ҵ�����");
                        //row++;
                    }
                    Object[] rowData = {product.getProductname(), product.getCas(), product.getStructure(), product.getFormula(),
                            product.getPrice(), product.getRealstock(), product.getCategory()};
                    for (int i = 0; i < rowData.length; i++) {
                        tableModel.setValueAt(rowData[i], row, i);
                    }
                    row++; // ��������
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //�������
    public void saveProductData () {
        // ��ȡ�������
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        int rowCount = tableModel.getRowCount();
        int columnCount = tableModel.getColumnCount();

        System.out.println("row: " + rowCount + "  column: " + columnCount);
        productData = new Object[rowCount][columnCount];//������ά�������ڴ洢�������

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if(tableModel.getValueAt(row, column) == null || tableModel.getValueAt(row, column).equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "����ʧ��\n��" + (row+1) + "�д�������Ϊ��");
                    return;//����������д��ڿյĵط����򱣴�ʧ�ܣ���ʾ��ʾ��Ϣ������
                }
                productData[row][column] = tableModel.getValueAt(row, column);
            }
        }
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                System.out.print(productData[row][column] + ",");
            }
            System.out.println();
        }
        productDataClient.saveProductsRequest(productData);
        JOptionPane.showMessageDialog(null,"����ɹ�");
    }
}
