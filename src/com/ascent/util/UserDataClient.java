package com.ascent.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import com.ascent.bean.User;

/**
 * 这个类连接数据服务器来获得数据
 * @author ascent
 * @version 1.0
 */
public class UserDataClient implements ProtocolPort {
	/**
	 * 验证码信息
	 */
	private String codeMsg;

	/**
	 * socket引用
	 */
	protected Socket hostSocket;

	/**
	 * 输出流的引用
	 */
	protected ObjectOutputStream outputToServer;

	/**
	 * 输入流的引用
	 */
	protected ObjectInputStream inputFromServer;

	/**
	 * 默认构造方法
	 */
	public UserDataClient() throws IOException {
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * 接受主机名和端口号的构造方法
	 */
	public UserDataClient(String hostName, int port) throws IOException {

		log("连接数据服务器..." + hostName + ":" + port);
		try {
			hostSocket = new Socket(hostName, port);
			outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
			inputFromServer = new ObjectInputStream(hostSocket.getInputStream());
			log("连接成功.");
		} catch (Exception e) {
			log("连接失败.");
		}
	}

	/**
	 * 返回用户
	 * @return userTable 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String,User> getUsers() {
		HashMap<String,User> userTable = null;

		try {
			log("发送请求: OP_GET_USERS  ");

			outputToServer.writeInt(ProtocolPort.OP_GET_USERS);
			outputToServer.flush();

			log("接收数据...");
			userTable = (HashMap<String,User>) inputFromServer.readObject();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userTable;
	}

	/**
	 * 关闭当前SocKet
	 */
	public void closeSocKet() {
		try {
			this.hostSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 日志方法.
	 * @param msg 打印的日志信息
	 */
	protected void log(Object msg) {
		System.out.println("UserDataClient类: " + msg);
	}

	/**
	 * 注册用户
	 * @param username 用户名
	 * @param password 密码
	 * @return boolean true:注册成功，false:注册失败
	 */
	public boolean addUser(String username, String password) {
		HashMap<String,User> map = this.getUsers();
		if (username.equals("") || map.containsKey(username)) {
			return false;
		} else {
			try {
				log("发送请求: OP_ADD_USERS  ");
				outputToServer.writeInt(ProtocolPort.OP_ADD_USERS);
				outputToServer.writeObject(new User(username, password, 0));
				outputToServer.flush();
				log("接收数据...");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * 请求发送验证码
	 */
	public String sendCode(String phone) {
		try {
			Integer.parseInt(phone);
		} catch (NumberFormatException numberFormatException) {
			return "请输入正确的电话格式(7位号码)";
		}
		if (phone.length() != 7) {
			System.out.println(phone.length());
			return "电话长度不正确";
		}
		try {
			log("发送请求: OP_SEND_CODE  ");
			outputToServer.writeInt(ProtocolPort.OP_SEND_CODE);
			outputToServer.flush();
			log("接收数据...");
			codeMsg = (String) inputFromServer.readObject();
			//System.out.println("接收到验证码： " + codeMsg);
			log("接收到验证码: " + codeMsg);
			return "发送成功";
		} catch (IOException e) {
			return e.getMessage();
		} catch (ClassNotFoundException e) {
			return e.getMessage();
		}
	}

	/**
	 * 密码找回验证
	 * @param phone 电话
	 * @param username 用户名
	 * @param msg 验证码
	 * @return String tip:返回提示信息
	 */
	public String searchPwd(String phone, String username , String msg) {
		String tip = "";
		try {
			Integer.parseInt(phone);
		} catch (NumberFormatException numberFormatException) {
			return "请输入正确的电话格式(7位号码)";
		}
		if (phone.length() != 7) {
			System.out.println(phone.length());
			return "电话长度不正确";
		}
		HashMap<String,User> map = this.getUsers();
		if (!msg.equals(codeMsg)) {
			tip = "验证码不正确";
		}
		else if (username.equals("") || !map.containsKey(username)) {
			tip = "用户不存在";
		} else {
			System.out.println("用户密码： " + map.get(username).getPassword());
			tip = "密码找回成功";
		}
		return tip;
	}

}
