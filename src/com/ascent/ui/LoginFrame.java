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
 * �û���½����
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
	 * Ĭ�ϵĹ��췽������ʼ����½����
	 */
	public LoginFrame() {

		setTitle("�û���½");

		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		JPanel loginPanel = new JPanel();

		JPanel userPanel = new JPanel();
		JPanel passwordPanel = new JPanel();
		userPanel.setLayout(new GridLayout(1,2));

		passwordPanel.setLayout(new GridLayout(1,2));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,2,15,15));


		JLabel userLabel = new JLabel("�û��ʺţ�");
		JLabel passwordLabel = new JLabel("�û����룺");

		userText = new JTextField(15);
		password = new JPasswordField(15);

		JButton loginButton = new JButton("�û���½");
		loginButton.setPreferredSize(new Dimension(102, 25));
		JButton manageLogin = new JButton("����Ա��½");
		manageLogin.setPreferredSize(new Dimension(102, 25));
		JButton regist = new JButton("ע��");
		regist.setPreferredSize(new Dimension(60, 20));
		regist.setFont(new Font("����", Font.BOLD, 10));//�������壬���е�һ���������������ƣ��ڶ���������������ʽ���Ӵ֣��������������������С��
		regist.setContentAreaFilled(false);
		JButton forgotpwd = new JButton("��������");
		forgotpwd.setPreferredSize(new Dimension(80, 20));
		forgotpwd.setFont(new Font("����", Font.BOLD, 10));//�������壬���е�һ���������������ƣ��ڶ���������������ʽ���Ӵ֣��������������������С��
		forgotpwd.setContentAreaFilled(false); // ʹ��ť����������͸��
		JButton exitButton = new JButton("�˳�");
		exitButton.setPreferredSize(new Dimension(60, 20));
		exitButton.setFont(new Font("����", Font.BOLD, 10));//�������壬���е�һ���������������ƣ��ڶ���������������ʽ���Ӵ֣��������������������С��
		exitButton.setContentAreaFilled(false);

		userPanel.add(userLabel);
		userPanel.add(new JScrollPane(userText));
		loginPanel.add(userPanel);

		passwordPanel.add(passwordLabel);
		passwordPanel.add(new JScrollPane(password));
		loginPanel.add(passwordPanel);

		buttonPanel.add(loginButton);
		buttonPanel.add(manageLogin);
		buttonPanel.add(regist);
		buttonPanel.add(forgotpwd);
		buttonPanel.setPreferredSize(new Dimension(350,60));

		loginPanel.add(buttonPanel);
		loginPanel.add(exitButton);

		setResizable(false);
//		setSize(260, 150);
//		setLocation(300, 100);
		setSize(400, 200);
		setLocation(400, 100);

		JPanel tipPanel = new JPanel();

		tip = new JLabel();

		tipPanel.add(tip);

		container.add(BorderLayout.CENTER, loginPanel);
		container.add(BorderLayout.NORTH, tip);

		exitButton.addActionListener(new ExitActionListener());//�˳���ť�¼�����
		loginButton.addActionListener(new LoginActionListener());//�û���¼��ť�¼�����
		regist.addActionListener(new RegistActionListener());//���水ť�¼�����
		manageLogin.addActionListener(new ManageActionListener());//����Ա��ť�¼�����
		forgotpwd.addActionListener(new ForgotPwdListener());//�������밴ť�¼�����

		this.addWindowListener(new WindowCloser());
		try {
			userDataClient = new UserDataClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**-----------------------
	 * ����"��������"��ť�¼��������ڲ���
	 */
	class ForgotPwdListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// �������һصĴ���
			PwdRecoveryFrame pwdRecoveryFrame = new PwdRecoveryFrame();
			pwdRecoveryFrame.setVisible(true);
		}
	}

	/**-----------------------
	 * ����"����Ա"��ť�¼��������ڲ���
	 */
	class ManageActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean bo = false;
			HashMap userTable = userDataClient.getUsers();
			if (userTable != null) {
				String text = userText.getText();//�û�������˺�
				String pwd = new String(password.getPassword());//�û����������
				if (userTable.containsKey(text)) {
					User userObject = (User) userTable.get(text);
					if (userObject.getAuthority() == 1 && userObject.getPassword().equals(pwd)) {
						bo = true;
					}
				}
				if (text.equals("")) {
					tip.setText("�˺Ų���Ϊ��");
				}
				else if (pwd.equals("")) {
					tip.setText("���벻��Ϊ��");
				}
				else if (bo) {
					userDataClient.closeSocKet();
					setVisible(false);
					dispose();
					AdministratorInterface ui = new AdministratorInterface();
					ui.setVisible(true);
				} else {
					tip.setText("�ʺŲ�����,���������.");
				}
			} else {
				tip.setText("����������ʧ��,���Ժ�����.");
			}
		}
	}

	/**
	 * ����"�˳�"��ť�¼��������ڲ���
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
	 * ����"�û���½"��ť�¼��������ڲ���
	 */
	class LoginActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			boolean bo = false;
			HashMap userTable = userDataClient.getUsers();
			if (userTable != null) {
				String text = userText.getText();//�û�������˺�
				String pwd = new String(password.getPassword());//�û����������
				if (userTable.containsKey(text)) {
					User userObject = (User) userTable.get(text);
					if (userObject.getAuthority() == 0 && userObject.getPassword().equals(pwd)) {
						bo = true;
					}
				}
				if (text.equals("")) {
					tip.setText("�˺Ų���Ϊ��");
				}
				else if (pwd.equals("")) {
					tip.setText("���벻��Ϊ��");
				}
				else if (bo) {
					userDataClient.closeSocKet();
					setVisible(false);
					dispose();
					MainFrame myFrame = new MainFrame();
					myFrame.setVisible(true);
				} else {
					tip.setText("�ʺŲ�����,���������.");
				}
			} else {
				tip.setText("����������ʧ��,���Ժ�����.");
			}
		}
	}

	/**
	 * ����"ע��"��ť�¼��������ڲ���.
	 */
	class RegistActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// ��ע���û��Ĵ���
			RegistFrame registFrame = new RegistFrame();
			registFrame.setVisible(true);
		}
	}

	/**
	 * ����"�رմ���"�¼��������ڲ���.
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
