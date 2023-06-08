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

class EditableTableModel extends DefaultTableModel {
    public EditableTableModel(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }
}

class UserManagerUI extends JFrame {
    private JPanel panel;
    private JTable table;
    private JButton saveButton;
    private JButton deleteButton;

    private UserDataClient userDataClient;
    private Map<String, User> userTable;

    public UserManagerUI() {
        try {
            userDataClient = new UserDataClient(); // ʵ����UserDataClient
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
        setTitle("�û�����");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // ������ʾ

        // ������������������
        panel = new JPanel();

        // �������ģ��
        String[] columnNames = {"�û���", "����", "�û�Ȩ��"};
        Object[][] rowData = new Object[100][3];
        EditableTableModel tableModel = new EditableTableModel(rowData, columnNames);

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
        panel.add(scrollPane);

        // �������水ť
        saveButton = new JButton("����");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // �������еı༭���ݵ��û�������
                int rowCount = tableModel.getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    String username = (String) tableModel.getValueAt(row, 0);
                    String password = (String) tableModel.getValueAt(row, 1);
                    int permission = (int) tableModel.getValueAt(row, 2);

                    // �����û��������Ϣ
                    User user = userTable.get(username);
                    if (user != null) {
                        user.setPassword(password);
                        user.setAuthority(permission);
                    }
                }

                // �����û���Ϣ
                saveUsers();

                // ��ʾ����ɹ�����������
                JOptionPane.showMessageDialog(UserManagerUI.this, "����ɹ�");
            }
        });

        // ����ɾ����ť
        deleteButton = new JButton("ɾ��");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��ȡѡ�е���
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // ��ȡѡ���е��û���
                    String username = (String) table.getValueAt(selectedRow, 0);

                    // ���û�����ɾ����Ӧ�û�
                    userTable.remove(username);

                    // �ӱ����ɾ��ѡ����
                    tableModel.removeRow(selectedRow);

                    // ��ʾɾ���ɹ�����������
                    JOptionPane.showMessageDialog(UserManagerUI.this, "ɾ���ɹ�");
                }
            }
        });

        // ������ť��岢��Ӱ�ť
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        // ��������壬�����ò��ֹ�����
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ���������ӵ����ڵ����������
        getContentPane().add(mainPanel);
    }

    public void displayUserData() {
        // ��ȡ�û����ݲ�չʾ�������
        userTable = userDataClient.getUsers();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0); // ��ձ������

        for (User user : userTable.values()) {
            Object[] rowData = {user.getUsername(), user.getPassword(), user.getAuthority()};
            tableModel.addRow(rowData);
        }
    }

    public void saveUsers() {
        try {
            // ���û���ת��Ϊ����
            Object[][] tableData = new Object[userTable.size()][3];
            int i = 0;
            for (User user : userTable.values()) {
                tableData[i][0] = user.getUsername();
                tableData[i][1] = user.getPassword();
                tableData[i][2] = user.getAuthority();
                i++;
            }

//            // �����û���Ϣ
//            if (userDataClient.updateUsers(tableData)) {
//                // ������ʾ�û�����
//                displayUserData();
//                // ��ʾ����ɹ�����������
//                JOptionPane.showMessageDialog(UserManagerUI.this, "����ɹ�");
//            } else {
//                JOptionPane.showMessageDialog(UserManagerUI.this, "����ʧ��");
//            }
        } catch (Exception e) {
            // �쳣����
            e.printStackTrace();
            JOptionPane.showMessageDialog(UserManagerUI.this, "��������쳣: " + e.getMessage());
        }
    }

}
