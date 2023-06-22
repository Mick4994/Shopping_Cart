package com.ascent.ui;

import com.ascent.bean.Product;
import com.ascent.util.ProductDataClient;
import com.ascent.util.UserDataClient;

import javax.swing.*;

/**
 * 管理员界面个人信息模块
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

    private JButton saveButton;//保存按钮
    private JButton addButton;//添加按钮
    private JButton delButton;//删除按钮

    public ProductManagerUI() {
        try {
            productDataClient = new ProductDataClient(); // 实例化UserDataClient
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
        setTitle("商品管理");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 创建表格模型
        String[] columnNames = {"药品名称", "化学文摘登记号", "结构图名称", "公式", "价格", "数量", "类别"};

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

        // 创建表格
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);

        // 设置单元格为可编辑
        table.setEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

        // 创建带滚动条的面板，并添加表格
        JScrollPane scrollPane = new JScrollPane(table);

        // 创建主面板，并设置布局管理器
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 将主面板添加到窗口的内容面板中
        getContentPane().add(mainPanel);

        // 显示商品集合数据
        displayProductData();

        JPanel buttonPanel = new JPanel();
        //设置保存按钮
        saveButton = new JButton("保存");
        saveButton.setSize(102, 52);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProductData ();
            }
        });
        //设置增加按钮
        addButton = new JButton("添加");
        addButton.setSize(102, 52);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] rowData = new Object[7];
                tableModel.addRow(rowData);
            }
        });
        //设置删除按钮
        delButton = new JButton(("删除"));
        delButton.setSize(102, 52);
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();// 获取选中的行
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);// 从表格中删除选中行
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
        // 填充表格数据
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        //tableModel.setRowCount(0); // 清空表格内容
        // 获取商品分类
        // 获取商品分类
        try {
            ArrayList<String> category = productDataClient.getCategories();
            int row = 0; // 初始化行数
            for (String categoryName : category) {
                // 在这里执行对每个分类的操作
                // 获取商品集合数据
                ArrayList<Product> productList = productDataClient.getProducts(categoryName);
                System.out.println("商品分类：" + categoryName);
                for (Product product : productList) {
                    String data = product.getProductname();
                    String pattern = "\\d+";
                    Pattern regex = Pattern.compile(pattern);
                    Matcher matcher = regex.matcher(data);
                    if (matcher.find()) {
                        String numberStr = matcher.group();
                        int number = Integer.parseInt(numberStr);
                        System.out.println("提取的数字：" + number);
                        //row = number - 1;
                    } else {
                        System.out.println("未找到数字");
                        //row++;
                    }
                    Object[] rowData = {product.getProductname(), product.getCas(), product.getStructure(), product.getFormula(),
                            product.getPrice(), product.getRealstock(), product.getCategory()};
                    for (int i = 0; i < rowData.length; i++) {
                        tableModel.setValueAt(rowData[i], row, i);
                    }
                    row++; // 递增行数
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //保存操作
    public void saveProductData () {
        // 获取表格数据
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        int rowCount = tableModel.getRowCount();
        int columnCount = tableModel.getColumnCount();

        System.out.println("row: " + rowCount + "  column: " + columnCount);
        productData = new Object[rowCount][columnCount];//创建二维数组用于存储表格数据

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if(tableModel.getValueAt(row, column) == null || tableModel.getValueAt(row, column).equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "保存失败\n第" + (row+1) + "行存在数据为空");
                    return;//若表格数据中存在空的地方，则保存失败，显示提示信息并返回
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
        JOptionPane.showMessageDialog(null,"保存成功");
    }
}
