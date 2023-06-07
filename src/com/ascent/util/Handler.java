package com.ascent.util;

import java.io.*;
import java.net.*;
import java.util.*;

import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 这个类是socket连接的处理器 例如：
 * <pre>
 * Handler aHandler = new Handler(clientSocket, myProductDataAccessor);
 * aHandler.start();
 * </pre>
 * @author ascent
 * @version 1.0
 */
public class Handler extends Thread implements ProtocolPort {

	protected Socket clientSocket;

	protected ObjectOutputStream outputToClient;

	protected ObjectInputStream inputFromClient;

	protected ProductDataAccessor myProductDataAccessor;

	protected boolean done;

	/**
	 * 带两个参数的构造方法
	 * @param theClientSocket 客户端Socket对象
	 * @param theProductDataAccessor 处理商品数据的对象
	 * @throws IOException 构造对象时可能发生IOException异常
	 */
	public Handler(Socket theClientSocket,ProductDataAccessor theProductDataAccessor) throws IOException {
		clientSocket = theClientSocket;
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		myProductDataAccessor = theProductDataAccessor;
		done = false;
	}

	/**
	 * 执行多线程的run()方法，处理客户端发送的命令
	 */
	@Override
	public void run() {

		try {
			while (!done) {

				log("等待命令...");

				int opCode = inputFromClient.readInt();
				log("opCode = " + opCode);

				switch (opCode) {
				case ProtocolPort.OP_GET_PRODUCT_CATEGORIES:
					opGetProductCategories();
					break;
				case ProtocolPort.OP_GET_PRODUCTS:
					opGetProducts();
					break;
				case ProtocolPort.OP_GET_USERS:
					opGetUsers();
					break;
				case ProtocolPort.OP_ADD_USERS:
					opAddUser();
					break;
				case ProtocolPort.OP_CLEAR_USERS:
					opDeleteUser();
					break;
				default:
					System.out.println("错误代码");
				}
			}
		} catch (IOException exc) {
			log(exc);
		}
	}

	/**
	 * 删除用户信息
	 */
	private void opDeleteUser() {
	    new	ProductDataAccessor().deleteUser();
	}


	/**
	 * 返回用户信息
	 */
	private void opGetUsers() {
		try {
			HashMap<String,User> userTable = myProductDataAccessor.getUsers();
			outputToClient.writeObject(userTable);
			outputToClient.flush();
		} catch (IOException exe) {
			log("发生异常：" + exe);
		}
	}

	/**
	 * 返回分类名称
	 */
	protected void opGetProductCategories() {
		try {
			ArrayList<String> categoryList = myProductDataAccessor.getCategories();
			outputToClient.writeObject(categoryList);
			outputToClient.flush();
			log("发出 " + categoryList.size() + " 类别信息到客户端");
		} catch (IOException exc) {
			log("发生异常:  " + exc);
		}
	}

	/**
	 * 返回某个分类名称的所有商品
	 */
	protected void opGetProducts() {
		try {
			log("读取份类信息");
			String category = (String) inputFromClient.readObject();
			log("类别是 " + category);

			ArrayList<Product> recordingList = myProductDataAccessor.getProducts(category);

			outputToClient.writeObject(recordingList);
			outputToClient.flush();
			log("发出 " + recordingList.size() + "个产品信息到客户端.");
		} catch (IOException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("发生异常:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * 处理用户注册
	 */
	public void opAddUser() {
		try {
			User user = (User) this.inputFromClient.readObject();
			System.out.println(user);
			this.myProductDataAccessor.save(user);
		} catch (IOException e) {
			log("发生异常:  " + e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log("发生异常:  " + e);
			e.printStackTrace();
		}
	}

//	/**
//	 * 处理多用户注册
//	 */
//	public void opSaveUsersRequest() {
//		try {
//			// 接收传递的对象
//			ObjectInputStream inputStream = new ObjectInputStream(this.inputFromClient);
//			HashMap<String, User> userTable = (HashMap<String, User>) inputStream.readObject();
//
//			// 遍历 userTable，并将用户保存到数据库中
//			for (Map.Entry<String, User> entry : userTable.entrySet()) {
//				User user = entry.getValue();
//				this.myProductDataAccessor.save(user);
//			}
//
//			log("保存用户信息成功");
//		} catch (IOException e) {
//			log("发生异常: " + e);
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			log("发生异常: " + e);
//			e.printStackTrace();
//		}
//	}


	/**
	 * 处理线程运行时标志
	 * @param flag
	 */
	public void setDone(boolean flag) {
		done = flag;
	}

	/**
	 * 打印信息到控制台
	 * @param msg
	 */
	protected void log(Object msg) {
		System.out.println("处理器: " + msg);
	}

}