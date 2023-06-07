package com.ascent.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import com.ascent.bean.User;

/**
 * ������������ݷ��������������
 * @author ascent
 * @version 1.0
 */
public class UserDataClient implements ProtocolPort {

	/**
	 * socket����
	 */
	protected Socket hostSocket;

	/**
	 * �����������
	 */
	protected ObjectOutputStream outputToServer;

	/**
	 * ������������
	 */
	protected ObjectInputStream inputFromServer;

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public UserDataClient() throws IOException {
		this(ProtocolPort.DEFAULT_HOST, ProtocolPort.DEFAULT_PORT);
	}

	/**
	 * �����������Ͷ˿ںŵĹ��췽��
	 */
	public UserDataClient(String hostName, int port) throws IOException {

		log("�������ݷ�����..." + hostName + ":" + port);
		try {
			hostSocket = new Socket(hostName, port);
			outputToServer = new ObjectOutputStream(hostSocket.getOutputStream());
			inputFromServer = new ObjectInputStream(hostSocket.getInputStream());
			log("���ӳɹ�.");
		} catch (Exception e) {
			log("����ʧ��.");
		}
	}

	/**
	 * �����û�
	 * @return userTable 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String,User> getUsers() {
		HashMap<String,User> userTable = null;

		try {
			log("��������: OP_GET_USERS  ");

			outputToServer.writeInt(ProtocolPort.OP_GET_USERS);
			outputToServer.flush();

			log("��������...");
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
	 * �رյ�ǰSocKet
	 */
	public void closeSocKet() {
		try {
			this.hostSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��־����.
	 * @param msg ��ӡ����־��Ϣ
	 */
	protected void log(Object msg) {
		System.out.println("UserDataClient��: " + msg);
	}

	/**
	 * ע���û�
	 * @param username �û���
	 * @param password ����
	 * @return boolean true:ע��ɹ���false:ע��ʧ��
	 */
	public boolean addUser(String username, String password) {
		HashMap<String,User> map = this.getUsers();
		if (username.equals("") || map.containsKey(username)) {
			return false;
		} else {
			try {
				log("��������: OP_ADD_USERS  ");
				outputToServer.writeInt(ProtocolPort.OP_ADD_USERS);
				outputToServer.writeObject(new User(username, password, 0));
				outputToServer.flush();
				log("��������...");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * ����û���Ϣ
	 */
	public boolean clearUsers() {
		try {
			log("��������: OP_CLEAR_USERS");
			outputToServer.writeInt(ProtocolPort.OP_CLEAR_USERS);
			outputToServer.flush();
			log("��������...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �����û���Ϣ
	 */
	public boolean updateUsers(Object[][] tableData) {
		try {
			// ����û���Ϣ
			clearUsers();

			HashMap<String, User> userTable = getUsers();
			userTable.clear();
			// �����û���Ϣ
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

			// �����û���Ϣ
			saveUsers(userTable);

			return true;
		} catch (Exception e) {
			// �쳣����
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * �����û���Ϣ
	 */
	public void saveUsers(HashMap<String, User> userTable) {
		try {
			log("��������: OP_SAVE_USERS");
			outputToServer.writeInt(ProtocolPort.OP_ADD_USERS);
			//outputToServer.writeInt(userTable.size()); // �����û���Ĵ�С
			for (User user : userTable.values()) {
				outputToServer.writeObject(user); // ��������û�����
				System.out.println(user);
			}
			outputToServer.flush();
			log("�����û���Ϣ�ɹ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
