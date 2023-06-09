package com.ascent.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.ascent.bean.Product;
import com.ascent.bean.SCProduct;
import com.ascent.util.ShoppingCart;
import com.sun.tools.javac.Main;

/**
 * 显示购物车所购买商品信息
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ShoppingCartDialog extends JDialog {

	protected ShoppingCart shoppingCart;

	protected MainFrame parentFrame;

	private JButton shoppingButton;

	private HashMap<String,JTextField> textMap;

	private JLabel tipLabel;

	/**
	 * 带两个参数的构造方法
	 * @param theParentFrame 父窗体
	 * @param shoppingButton 查看购物车按钮
	 */
	public ShoppingCartDialog(MainFrame theParentFrame, JButton shoppingButton) {
		super(theParentFrame, "购物车", true);
		textMap = new HashMap<String,JTextField>();
		parentFrame = theParentFrame;
		this.shoppingButton = shoppingButton;
		this.shoppingCart = theParentFrame.user.shoppingCart;

		lookShoppingCar();
	}

	/**
	 * 构建显示购物车里商品信息的界面
	 */
	public void lookShoppingCar() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel infoPanel = new JPanel();
		tipLabel = new JLabel("");
		infoPanel.add(tipLabel);
		infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		infoPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 0, 2, 10);

		//shoppingCart = new ShoppingCart();
		ArrayList<SCProduct> shoppingList = shoppingCart.getShoppingList();

		JLabel pruductLabel;
		Product product = null;
		for (int i = 0; i < shoppingList.size(); i++) {
			c.gridy = c.gridy + 2;
			String str = "";
			SCProduct scProduct = shoppingList.get(i);
			product = scProduct.getTheProduct();
			str = str + "产品名：" + product.getProductname() + "    ";
			str = str + "CAS号：" + product.getCas() + "    ";
			str = str + "公式：" + product.getFormula() + "    ";
			str = str + "类别：" + product.getCategory();
			pruductLabel = new JLabel(str);
			JPanel panel = new JPanel(new FlowLayout());
			JLabel l = new JLabel("数量：");
			JTextField jtf = new JTextField(7);
			jtf.setText(String.valueOf(scProduct.getNum()));

			JButton delButton = new JButton("删除");

			panel.add(pruductLabel);
			panel.add(l);
			panel.add(jtf);

			panel.add(delButton);

			delButton.addActionListener(new DelActionListener(product.getProductname()));

			jtf.addFocusListener(new JtfFocusListener(scProduct, jtf));
			textMap.put(product.getProductname(), jtf);
			pruductLabel.setForeground(Color.black);
			infoPanel.add(panel, c);
		}

		container.add(BorderLayout.NORTH, infoPanel);

		JPanel bottomPanel = new JPanel();
		JButton okButton = new JButton("提交表单");
		bottomPanel.add(okButton);
		JButton clearButton = new JButton("清空");
		bottomPanel.add(clearButton);

		container.add(BorderLayout.SOUTH, bottomPanel);

		okButton.addActionListener(new OkButtonActionListener());
		clearButton.addActionListener(new ClearButtonActionListener());
		setResizable(false);
		this.pack();
		Point parentLocation = parentFrame.getLocation();
		this.setLocation(parentLocation.x + 50, parentLocation.y + 50);
	}

	/**
	 * 处理删除按钮事件监听的内部类
	 */
	class DelActionListener implements ActionListener {
		String key;

		public DelActionListener(String productName) {
			this.key = productName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(key + "-----------");
			shoppingCart.delProduct(key);
			setVisible(false);//关闭当前界面
			new ShoppingCartDialog(parentFrame, shoppingButton).setVisible(true);//刷新界面
		}
	}

	/**
	 * 处理数量输入框（商品的个数）的内部类
	 */
	class JtfFocusListener implements FocusListener {
		private SCProduct theProduct;
		private JTextField jtf;

		public JtfFocusListener(SCProduct theProduct, JTextField jtf) {
			this.theProduct = theProduct;
			this.jtf = jtf;
		}

		@Override
		public void focusGained(FocusEvent e) {
			int n = theProduct.getNum();
			jtf.setText(String.valueOf(n));
		}

		@Override
		public void focusLost(FocusEvent e) {
			int n = theProduct.getNum();
			try {
				n = Integer.parseInt(jtf.getText());
				jtf.setText(jtf.getText());
			} catch (NumberFormatException numberFormatException) {
				jtf.setText("格式错误");
				System.out.println("商品数量输入格式错误");
			}
			theProduct.setNum(n);
		}
	}

	/**
	 * 处理"OK"按钮的内部类
	 * @author ascent
	 */
	class OkButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			Set set = textMap.keySet();
			for (Object o : set) {
				JTextField t = (JTextField) textMap.get((String) o);
				if ("".equals(t.getText())) {
					tipLabel.setText("请输入数量");
					return;
				}
			}
			setVisible(false);
			ShoppingMessageDialog myMessageDialog = new ShoppingMessageDialog(parentFrame);
			myMessageDialog.setVisible(true);
		}
	}

	/**
	 * 处理"clear"按钮的内部类
	 * @author ascent
	 */
	class ClearButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//ShoppingCart shopping = new ShoppingCart();
			shoppingCart.clearProduct();
			shoppingButton.setEnabled(false);
			setVisible(false);
		}
	}
}
