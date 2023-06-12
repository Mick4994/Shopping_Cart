package com.ascent.ui;

import com.ascent.bean.User;
import com.ascent.util.DataAccessor;
import com.ascent.util.UserDataClient;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员界面个人信息模块
 * @author muniu
 * @version 1.0
 */

class EditableTableModel extends DefaultTableModel {
    public EditableTableModel(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        // 只有第2列可编辑，列索引从0开始
        return column == 1;
    }
}

class UserManagerUI extends JFrame {
    private JPanel panel;
    private JTable table;
    private JButton saveButton;
    private JButton deleteButton;

    private UserDataClient userDataClient;
    private Map<String, User> userTable;
    private TableModelListener tableModelListener;

    // 在类的成员变量中定义一个标志
    private boolean updatingFlag = false;

    public UserManagerUI() {
        try {
            userDataClient = new UserDataClient(); // 实例化UserDataClient
        } catch (IOException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayUserData();
                setVisible(true);
            }
        });
        setTitle("用户管理");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 创建面板用于容纳组件
        panel = new JPanel();

        // 创建表格模型
        String[] columnNames = {"用户名", "密码", "用户权限"};
        Object[][] rowData = new Object[100][3];
        EditableTableModel tableModel = new EditableTableModel(rowData, columnNames);

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
        panel.add(scrollPane);

        // 创建保存按钮
        saveButton = new JButton("保存");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 保存表格中的编辑数据到用户对象中
                int rowCount = tableModel.getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    String username = (String) tableModel.getValueAt(row, 0);
                    String password = (String) tableModel.getValueAt(row, 1);
                    int permission = (int) tableModel.getValueAt(row, 2);

                    // 更新用户对象的信息
                    User user = userTable.get(username);
                    if (user != null) {
                        user.setPassword(password);
                        user.setAuthority(permission);
                    }
                }

                // 保存用户信息
                saveUsers();

                // 提示保存成功或其他操作
                JOptionPane.showMessageDialog(UserManagerUI.this, "保存成功");
            }
        });

        // 创建删除按钮
        deleteButton = new JButton("删除");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选中的行
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // 获取选中行的用户名
                    String username = (String) table.getValueAt(selectedRow, 0);

                    // 从用户表中删除对应用户
                    userTable.remove(username);

                    // 从表格中删除选中行
                    tableModel.removeRow(selectedRow);

                    saveUsers();

                    // 提示删除成功或其他操作
                    JOptionPane.showMessageDialog(UserManagerUI.this, "删除成功");
                }
            }
        });

        // 创建按钮面板并添加按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        // 创建主面板，并设置布局管理器
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 将主面板添加到窗口的内容面板中
        getContentPane().add(mainPanel);

        // 创建表格模型监听器
        tableModelListener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = table.convertRowIndexToModel(e.getFirstRow());
                    int column = table.convertColumnIndexToModel(e.getColumn());

                    // 确保行索引在有效范围内
                    if (row >= 0 && row < tableModel.getRowCount()) {
                        // 确保列索引在有效范围内
                        if (column >= 0 && column < tableModel.getColumnCount()) {
                            // 检查标志，避免递归调用
                            if (!updatingFlag) {
                                // 设置标志为 true
                                updatingFlag = true;
                                // 获取修改后的数据
                                Object newData = tableModel.getValueAt(row, column);
                                // 更新表格中对应位置的数据
                                tableModel.setValueAt(newData, row, column);
                                // 设置标志为 false
                                updatingFlag = false;
                            }
                        }
                    }
                }
            }
        };

        // 添加表格模型监听器
        tableModel.addTableModelListener(tableModelListener);
    }

    public void displayUserData() {
        // 移除表格模型的监听器
        table.getModel().removeTableModelListener(tableModelListener);

        // 获取用户数据并展示到表格中
        userTable = userDataClient.getUsers();
        EditableTableModel tableModel = (EditableTableModel) table.getModel();
        tableModel.setRowCount(0); // 清空表格内容

        // 设置表格列数
        tableModel.setColumnCount(3);

        for (User user : userTable.values()) {
            Object[] rowData = {user.getUsername(), user.getPassword(), user.getAuthority()};
            tableModel.addRow(rowData);
        }

        // 重新添加表格模型的监听器
        table.getModel().addTableModelListener(tableModelListener);
    }

    public void saveUsers() {
        try {
            // 将用户表转换为数组
            Object[][] tableData = new Object[userTable.size()][3];
            int i = 0;
            for (User user : userTable.values()) {
                tableData[i][0] = user.getUsername();
                tableData[i][1] = user.getPassword();
                tableData[i][2] = user.getAuthority();
                i++;
            }

            // 更新用户信息
            if (userDataClient.updateUsers(tableData)) {
                // 重新显示用户数据
                displayUserData();
                // 提示保存成功或其他操作
                JOptionPane.showMessageDialog(UserManagerUI.this, "保存成功");
            } else {
                JOptionPane.showMessageDialog(UserManagerUI.this, "保存失败");
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
            JOptionPane.showMessageDialog(UserManagerUI.this, "保存出现异常: " + e.getMessage());
        }
    }
}
