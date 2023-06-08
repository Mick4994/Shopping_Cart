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
        Object[][] rowData = new Object[100][7];
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);

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
    }

    public void displayProductData() {
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
                        row = number - 1;
                    } else {
                        System.out.println("未找到数字");
                        row++;
                    }
                    Object[] rowData = {product.getProductname(), product.getCas(), product.getStructure(), product.getFormula(),
                            product.getPrice(), product.getRealstock(), product.getCategory()};
                    for (int i = 0; i < rowData.length; i++) {
                        tableModel.setValueAt(rowData[i], row, i);
                    }
                    // row++; // 递增行数
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
