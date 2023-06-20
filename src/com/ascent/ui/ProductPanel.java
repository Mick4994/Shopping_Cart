package com.ascent.ui;

import javax.swing.*;
import javax.swing.event.*;

import com.ascent.bean.Product;
import com.ascent.util.ProductDataClient;
import com.ascent.ui.ChatGUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;

/**
 * ����๹����Ʒ���
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProductPanel extends JPanel {

	protected JLabel selectionLabel;

	protected JComboBox categoryComboBox;

	protected JPanel topPanel;

	protected JList productListBox;

	protected JScrollPane productScrollPane;

	protected JButton detailsButton;

	protected JButton chatButton;

	protected JButton clearButton;

	protected JButton exitButton;

	protected JButton shoppingButton;

	protected JPanel bottomPanel;

	protected MainFrame parentFrame;

	protected ArrayList<Product> productArrayList;

	protected ProductDataClient myDataClient;

	protected ChatGUI chatGUI;

	protected boolean is_chatting = false;

	/**
	 * ������Ʒ���Ĺ��췽��
	 * @param theParentFrame ���ĸ�����
	 */
	public ProductPanel(MainFrame theParentFrame) {
		try {
			parentFrame = theParentFrame;
			myDataClient = new ProductDataClient();
			selectionLabel = new JLabel("ѡ�����");
			categoryComboBox = new JComboBox();
			categoryComboBox.addItem("��ѡ��ҩƷ����");

			JLabel lblBackground = new JLabel(); // ����һ����ǩ�������
			ImageIcon icon = new ImageIcon("resourcepack/background.jpg"); // ��������ͼƬ����
			lblBackground.setIcon(icon); // ���ñ�ǩ���Ҫ��ʾ��ͼ��
			lblBackground.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); // �����������ʾλ�ü���С

			ArrayList categoryArrayList = myDataClient.getCategories();

			Iterator iterator = categoryArrayList.iterator();
			String aCategory;

			while (iterator.hasNext()) {
				aCategory = (String) iterator.next();
				categoryComboBox.addItem(aCategory);
			}

			topPanel = new JPanel();
			productListBox = new JList();
			productListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			productScrollPane = new JScrollPane(productListBox);

			detailsButton = new JButton("��ϸ...");
			clearButton = new JButton("���");
			exitButton = new JButton("�˳�");
			shoppingButton = new JButton("�鿴���ﳵ");
			chatButton = new JButton("�ͷ�����");

			bottomPanel = new JPanel();

			this.setLayout(new BorderLayout());

			topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			topPanel.add(selectionLabel);
			topPanel.add(categoryComboBox);

			this.add(BorderLayout.NORTH, topPanel);
			this.add(BorderLayout.CENTER, productScrollPane);

			bottomPanel.setLayout(new FlowLayout());
			bottomPanel.add(shoppingButton);
			bottomPanel.add(detailsButton);
			bottomPanel.add(clearButton);
			bottomPanel.add(exitButton);
			bottomPanel.add(chatButton);

			this.add(BorderLayout.SOUTH, bottomPanel);

			detailsButton.addActionListener(new DetailsActionListener());
			clearButton.addActionListener(new ClearActionListener());
			exitButton.addActionListener(new ExitActionListener());

			chatButton.addActionListener(new chatActionListener());

			shoppingButton.addActionListener(new ShoppingActionListener());
			categoryComboBox.addItemListener(new GoItemListener());
			productListBox.addListSelectionListener(new ProductListSelectionListener());

			detailsButton.setEnabled(false);
			clearButton.setEnabled(false);
			shoppingButton.setEnabled(false);

		} catch (IOException exc) {
			JOptionPane.showMessageDialog(this, "�������� " + exc, "��������", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * ����������ѡ�еķ���ѡ��
	 */
	protected void populateListBox() {
		try {
			String category = (String) categoryComboBox.getSelectedItem();
			if (!category.equals("��ѡ��ҩƷ����")) {
				productArrayList = myDataClient.getProducts(category);
			} else {
				productArrayList = new ArrayList<Product>();
			}

			Object[] theData = productArrayList.toArray();

			System.out.println(productArrayList + ">>>>>>>>>>>");
			productListBox.setListData(theData);
			productListBox.updateUI();

			if (productArrayList.size() > 0) {
				clearButton.setEnabled(true);
			} else {
				clearButton.setEnabled(false);
			}
		} catch (IOException exc) {
			JOptionPane.showMessageDialog(this, "��������: " + exc, "��������", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} 
	}

	/**
	 * ����ѡ����ϸ...��ťʱ�������¼�������
	 * @author ascent
	 */
	class DetailsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int index = productListBox.getSelectedIndex();
			Product product = (Product) productArrayList.get(index);
			ProductDetailsDialog myDetailsDialog = new ProductDetailsDialog(parentFrame, product, shoppingButton);
			myDetailsDialog.setVisible(true);
		}
	}

	/**
	 * ����ѡ��鿴���ﳵ��ťʱ�������¼�������
	 * @author ascent
	 */
	class ShoppingActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ShoppingCartDialog myShoppingDialog = new ShoppingCartDialog(
					parentFrame, shoppingButton);
			myShoppingDialog.setVisible(true);
		}
	}

	/**
	 * ����ѡ���˳���ťʱ�������¼�������
	 * @author ascent
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			parentFrame.exit();
		}
	}

	/**
	 * �������찴ťʱ�������¼�������
	 * @author Mick4994
	 */
	class chatActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(chatGUI != null) {
				if(!chatGUI.is_chatting) {
					System.out.println("�����ͷ�����");
					chatGUI = new ChatGUI();
					chatGUI.frame.setVisible(true);
					chatGUI.is_chatting = true;
				}
			} else {
				System.out.println("�����ͷ�����");
				chatGUI = new ChatGUI();
				chatGUI.frame.setVisible(true);
				chatGUI.is_chatting = true;
			}
		}
	}

	/**
	 * ����ѡ����հ�ťʱ�������¼�������
	 * @author ascent
	 */
	class ClearActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object[] noData = new Object[1];
			productListBox.setListData(noData);
			categoryComboBox.setSelectedIndex(0);
		}
	}

	/**
	 * ����ѡ�з���������ѡ��ѡ��ʱ�������¼�������
	 * @author ascent
	 */
	class GoItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				populateListBox();
			}
		}
	}

	/**
	 * ����ѡ�з����б���ѡ��ʱ�������¼�������
	 * @author ascent
	 */
	class ProductListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (productListBox.isSelectionEmpty()) {
				detailsButton.setEnabled(false);
			} else {
				detailsButton.setEnabled(true);
			}
		}
	}
}