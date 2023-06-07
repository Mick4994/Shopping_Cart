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
 * 这个类构建产品面板
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

	/**
	 * 构建产品面板的构造方法
	 * @param theParentFrame 面板的父窗体
	 */
	public ProductPanel(MainFrame theParentFrame) {
		try {
			parentFrame = theParentFrame;
			myDataClient = new ProductDataClient();
			selectionLabel = new JLabel("选择类别");
			categoryComboBox = new JComboBox();
			categoryComboBox.addItem("请选择药品分类");

			JLabel lblBackground = new JLabel(); // 创建一个标签组件对象
			ImageIcon icon = new ImageIcon("resourcepack/background.jpg"); // 创建背景图片对象
			lblBackground.setIcon(icon); // 设置标签组件要显示的图标
			lblBackground.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); // 设置组件的显示位置及大小

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

			detailsButton = new JButton("详细...");
			clearButton = new JButton("清空");
			exitButton = new JButton("退出");
			shoppingButton = new JButton("查看购物车");
			chatButton = new JButton("客服聊天");

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
			JOptionPane.showMessageDialog(this, "网络问题 " + exc, "网络问题", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * 设置下拉列选中的分类选项
	 */
	protected void populateListBox() {
		try {
			String category = (String) categoryComboBox.getSelectedItem();
			if (!category.equals("请选择药品分类")) {
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
			JOptionPane.showMessageDialog(this, "网络问题: " + exc, "网络问题", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} 
	}

	/**
	 * 处理选择详细...按钮时触发的事件监听器
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
	 * 处理选择查看购物车按钮时触发的事件监听器
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
	 * 处理选择退出按钮时触发的事件监听器
	 * @author ascent
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			parentFrame.exit();
		}
	}

	class chatActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			System.out.println("启动客服聊天");
			ChatGUI chatGUI = new ChatGUI();
			chatGUI.frame.setVisible(true);
		}
	}

	/**
	 * 处理选择清空按钮时触发的事件监听器
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
	 * 处理选中分类下拉列选的选项时触发的事件监听器
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
	 * 处理选中分类列表中选项时触发的事件监听器
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