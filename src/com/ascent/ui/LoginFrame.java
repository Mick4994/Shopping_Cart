package com.ascent.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.ascent.bean.User;
import com.ascent.util.UserDataClient;

/**
 * 用户登陆窗体
 * @author ascent
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	protected JTextField userText;

	protected JPasswordField password;

	protected JLabel tip;

	protected UserDataClient userDataClient;

	/**
	 * 默认的构造方法，初始化登陆窗体
	 */
	public LoginFrame() {

		setTitle("用户登陆");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel loginPanel = new JPanel();

		JLabel userLabel = new JLabel("用户帐号：");
		JLabel passwordLabel = new JLabel("用户密码：");

		userText = new JTextField(15);
		password = new JPasswordField(15);

		JButton loginButton = new JButton("用户登陆");
		loginButton.setPreferredSize(new Dimension(102, 25));

		////---设置管理员按钮
		JButton manageLogin = new JButton("管理员登陆");
		manageLogin.setPreferredSize(new Dimension(102, 25));


		JButton regist = new JButton("注册");
		regist.setPreferredSize(new Dimension(60, 20));
		regist.setFont(new Font("宋体", Font.BOLD, 10));//设置字体，其中第一个参数是字体名称，第二个参数是字体样式（加粗），第三个参数是字体大小。
		regist.setContentAreaFilled(false);


		JButton button = new JButton("忘记密码");
		button.setPreferredSize(new Dimension(80, 20));
		button.setFont(new Font("宋体", Font.BOLD, 10));//设置字体，其中第一个参数是字体名称，第二个参数是字体样式（加粗），第三个参数是字体大小。
		button.setContentAreaFilled(false); // 使按钮的内容区域透明


		JButton exitButton = new JButton("退出");
		exitButton.setPreferredSize(new Dimension(60, 20));
		exitButton.setFont(new Font("宋体", Font.BOLD, 10));//设置字体，其中第一个参数是字体名称，第二个参数是字体样式（加粗），第三个参数是字体大小。
		exitButton.setContentAreaFilled(false);


		loginPanel.add(userLabel);
		loginPanel.add(new JScrollPane(userText));
		loginPanel.add(passwordLabel);
		loginPanel.add(new JScrollPane(password));
		loginPanel.add(loginButton);
		//----添加管理者按钮组件
		loginPanel.add(manageLogin);
		loginPanel.add(regist);
		//----添加忘记密码按钮
		loginPanel.add(button);
		loginPanel.add(exitButton);


		setResizable(false);
//		setSize(260, 150);
//		setLocation(300, 100);
		setSize(250, 175);
		setLocation(300, 100);


		JPanel tipPanel = new JPanel();

		tip = new JLabel();

		tipPanel.add(tip);

		container.add(BorderLayout.CENTER, loginPanel);
		container.add(BorderLayout.NORTH, tip);

		exitButton.addActionListener(new ExitActionListener());
		loginButton.addActionListener(new LoginActionListener());
		regist.addActionListener(new RegistActionListener());
		////----管理员按钮事件监听
		manageLogin.addActionListener(new ManageActionListener());


		this.addWindowListener(new WindowCloser());
		try {
			userDataClient = new UserDataClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	/**-----------------------
	 * 处理"管理员"按钮事件监听的内部类
	 */
	class ManageActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean bo = false;
			HashMap userTable = userDataClient.getUsers();
			if (userTable != null) {
				String text = userText.getText();//用户输入的账号
				String pwd = new String(password.getPassword());//用户输入的密码
				if (userTable.containsKey(text)) {
					User userObject = (User) userTable.get(text);
					if (userObject.getAuthority() == 1 && userObject.getPassword().equals(pwd)) {
						bo = true;
					}
				}
				//////////////
				if (text.equals("")) {
					tip.setText("账号不能为空");
				}
				else if (pwd.equals("")) {
					tip.setText("密码不能为空");
				}
				else if (bo) {
					userDataClient.closeSocKet();
					setVisible(false);
					dispose();
					MainFrame myFrame = new MainFrame();
					myFrame.setVisible(true);
				} else {
					tip.setText("帐号不存在,或密码错误.");
				}
			} else {
				tip.setText("服务器连接失败,请稍候再试.");
			}
		}
	}






	/**
	 * 处理"退出"按钮事件监听的内部类
	 */
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
			System.exit(0);
		}
	}

	/**
	 * 处理"用户登陆"按钮事件监听的内部类
	 */
	class LoginActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			boolean bo = false;
			HashMap userTable = userDataClient.getUsers();
			if (userTable != null) {
				String text = userText.getText();//用户输入的账号
				String pwd = new String(password.getPassword());//用户输入的密码
				if (userTable.containsKey(text)) {
					User userObject = (User) userTable.get(text);
					if (userObject.getAuthority() == 0 && userObject.getPassword().equals(pwd)) {
						bo = true;
					}
				}
				//////////////
				if (text.equals("")) {
					tip.setText("账号不能为空");
				}
				else if (pwd.equals("")) {
					tip.setText("密码不能为空");
				}
				else if (bo) {
					userDataClient.closeSocKet();
					setVisible(false);
					dispose();
					MainFrame myFrame = new MainFrame();
					myFrame.setVisible(true);
				} else {
					tip.setText("帐号不存在,或密码错误.");
				}
			} else {
				tip.setText("服务器连接失败,请稍候再试.");
			}
		}
	}

	/**
	 * 处理"注册"按钮事件监听的内部类.
	 */
	class RegistActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// 打开注册用户的窗口
			RegistFrame registFrame = new RegistFrame();
			registFrame.setVisible(true);
		}
	}

	/**
	 * 处理"关闭窗口"事件监听的内部类.
	 */
	class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			dispose();
			userDataClient.closeSocKet();
			System.exit(0);
		}
	}
}
