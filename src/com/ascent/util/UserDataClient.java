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
	 * 清除用户信息
	 */
	public boolean clearUsers() {
		try {
			log("发送请求: OP_CLEAR_USERS");
			outputToServer.writeInt(ProtocolPort.OP_CLEAR_USERS);
			outputToServer.flush();
			log("接收数据...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 拷贝用户信息
	 */
	public boolean updateUsers(Object[][] tableData) {
		try {
			// 清空用户信息
			clearUsers();

			HashMap<String, User> userTable = getUsers();
			userTable.clear();
			// 更新用户信息
			for (Object[] rowData : tableData) {
				String username = (String) rowData[0];
				String password = (String) rowData[1];
				int permission = (int) rowData[2];
				System.out.println(username+" "+password+" "+permission);
				if (!username.isEmpty()) {
					User user = new User(username, password, permission);
					userTable.put(username, user);
				}
			}

			// 保存用户信息
			saveUsers(userTable);

			return true;
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 保存用户信息
	 */
	public void saveUsers(HashMap<String, User> userTable) {
		try {
			log("发送请求: OP_SAVE_USERS");
			outputToServer.writeInt(ProtocolPort.OP_ADD_USERS);
			//outputToServer.writeInt(userTable.size()); // 发送用户表的大小
			for (User user : userTable.values()) {
				outputToServer.writeObject(user); // 逐个发送用户对象
				System.out.println(user);
			}
			outputToServer.flush();
			log("保存用户信息成功");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
