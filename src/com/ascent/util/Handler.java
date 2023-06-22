package com.ascent.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import com.ascent.bean.Product;
import com.ascent.bean.User;

import javax.imageio.ImageIO;

/**
 * �������socket���ӵĴ����� ���磺
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
	 * �����������Ĺ��췽��
	 * @param theClientSocket �ͻ���Socket����
	 * @param theProductDataAccessor ������Ʒ���ݵĶ���
	 * @throws IOException �������ʱ���ܷ���IOException�쳣
	 */
	public Handler(Socket theClientSocket,ProductDataAccessor theProductDataAccessor) throws IOException {
		clientSocket = theClientSocket;
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		myProductDataAccessor = theProductDataAccessor;
		done = false;
	}

	/**
	 * ִ�ж��̵߳�run()����������ͻ��˷��͵�����
	 */
	@Override
	public void run() {

		try {
			while (!done) {

				log("�ȴ�����...");

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
				case ProtocolPort.OP_SEND_CODE:
					opGetMsg();
					break;
				case ProtocolPort.OP_CLEAR_USERS:
					opDeleteUser();
					break;
				case ProtocolPort.OP_GET_FEEDBACK:
					opGetFeedBack();
					break;
				case ProtocolPort.OP_GET_FEEDBACK_NUM:
					opGetFeedBackNum();
					break;
				case ProtocolPort.OP_GET_PERSON_MSG:
					opGetPersonMessage();
					break;
				case ProtocolPort.OP_SAVE_PERSON_IMAGE_MSG:
					opSetPersonSImageMessage();
					break;
				case ProtocolPort.OP_SAVE_PERSON_INFO:
					opSetPersonSMessage();
					break;
				case ProtocolPort.OP_SAVE_PRODUCTS:
					opSaveProductData();
					break;
				default:
					System.out.println("�������");
				}
			}
		} catch (IOException exc) {
			log(exc);
		}
	}

	/**
	 * �������Ա�������պ��ǳƵı���
	 */
	private void opSetPersonSMessage() {
		// �����������Ϣ����
		String fileContent = null;
		try {
			fileContent = (String) inputFromClient.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("./src/images/administratorMessage.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.write(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("�������պ��ǳ���Ϣ�ѱ���");
	}

	/**
	 * �������Ա����ͷ��ı���
	 */
	private void opSetPersonSImageMessage() {
		// ����ͼ������
		String dataType = null;
		try {
			dataType = (String) inputFromClient.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (dataType.equals("image")) {
			byte[] imageData = new byte[0];
			try {
				imageData = (byte[]) inputFromClient.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			// ����ͼ���ļ�
			Path imagePath = Path.of("./src/images/administratorImage.jpg");
			try {
				Files.write(imagePath, imageData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("ͷ���ѱ��浽�ļ�: " + imagePath.toAbsolutePath());
		}
	}

	/**
	 * �������Ա������Ϣ�Ķ�ȡ
	 */
	private void opGetPersonMessage() {
		try {
			// ��ȡͼ���ļ�������ת��Ϊ�ֽ�����
			File imageFile = new File("./src/images/administratorImage.jpg");
			BufferedImage image = ImageIO.read(imageFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageData = baos.toByteArray();

			// ����ͼ�����ݸ��ͻ���
			outputToClient.writeObject("image"); // ����ͼ�����ݵı�ʶ
			outputToClient.flush();
			outputToClient.writeObject(imageData); // ����ͼ������
			outputToClient.flush();
			System.out.println("ͼ���ѷ���");

			// ��ȡ�ļ�����ΪString����
			String filePath = "./src/images/administratorMessage.txt";
			String fileContent = readFileAsString(filePath);

			// �����ļ����ݸ��ͻ���
			outputToClient.writeObject(fileContent);
			outputToClient.flush();
			System.out.println("�ļ������ѷ���");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String readFileAsString(String filePath) throws IOException {
		StringBuilder content = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		while ((line = reader.readLine()) != null) {
			content.append(line);
		}
		reader.close();
		return content.toString();
	}

	/**
	 * ɾ���û���Ϣ
	 */
	private void opDeleteUser() {
		new ProductDataAccessor().deleteUser();
	}

	/**
	 * ���������֤�벢����
	 */
	private void opGetMsg() {
		String msg = String.valueOf((int)(Math.random() * 100000));
		try {
			outputToClient.writeObject(msg);
			outputToClient.flush();
		} catch (IOException exe) {
			log("�����쳣��" + exe);
		}
	}



	/**
	 * �����û���Ϣ
	 */
	private void opGetUsers() {
		try {
			HashMap<String,User> userTable = myProductDataAccessor.getUsers();
			outputToClient.writeObject(userTable);
			outputToClient.flush();
		} catch (IOException exe) {
			log("�����쳣��" + exe);
		}
	}

	/**
	 * ���ط�������
	 */
	protected void opGetProductCategories() {
		try {
			ArrayList<String> categoryList = myProductDataAccessor.getCategories();
			outputToClient.writeObject(categoryList);
			outputToClient.flush();
			log("���� " + categoryList.size() + " �����Ϣ���ͻ���");
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
		}
	}

	/**
	 * ����ĳ���������Ƶ�������Ʒ
	 */
	protected void opGetProducts() {
		try {
			log("��ȡ������Ϣ");
			String category = (String) inputFromClient.readObject();
			log("����� " + category);

			if(category.equals("��ѡ��ҩƷ����")) {
				log("����δѡ״̬");
				return;
			}

			ArrayList<Product> recordingList = myProductDataAccessor.getProducts(category);

			outputToClient.writeObject(recordingList);
			outputToClient.flush();
			log("���� " + recordingList.size() + "����Ʒ��Ϣ���ͻ���.");
		} catch (IOException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		} catch (ClassNotFoundException exc) {
			log("�����쳣:  " + exc);
			exc.printStackTrace();
		}
	}

	/**
	 * �����û�ע��
	 */
	public void opAddUser() {
		try {
			User user = (User) this.inputFromClient.readObject();
			this.myProductDataAccessor.save(user);
		} catch (IOException e) {
			log("�����쳣:  " + e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log("�����쳣:  " + e);
			e.printStackTrace();
		}
	}

	/**
	 * �����߳�����ʱ��־
	 * @param flag
	 */
	public void setDone(boolean flag) {
		done = flag;
	}

	/**
	 * ��ӡ��Ϣ������̨
	 * @param msg
	 */
	protected void log(Object msg) {
		System.out.println("������: " + msg);
	}

	protected void opGetFeedBackNum() {
		File feedbackfile = new File("./FeedbackMsg/");
		String[] filelist = feedbackfile.list();
		try {
			if (filelist != null) {
				outputToClient.writeInt(filelist.length);
			} else {
				outputToClient.writeInt(0);
			}
			outputToClient.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void opGetFeedBack() throws IOException {
		File feedbackfile = new File("./FeedbackMsg/");
		String[] filelist = feedbackfile.list();
//		assert filelist != null;
		if(filelist == null) {
			return;
		}
		if(filelist.length != 0) {
			String[] context = new String[filelist.length];
			for(String filename : filelist) {
//            System.out.println(filename);
				File file = new File("./FeedbackMsg/" + filename);
				try {
					BufferedReader inputFromFile = new BufferedReader(
							new FileReader("./FeedbackMsg/" + filename));
					String line = inputFromFile.readLine();
					if(line != null) {
						outputToClient.writeObject(line);
						outputToClient.flush();
					}
					inputFromFile.close();
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}

		}

	}

	/**
	 * ������Ʒ��Ϣ����
	 */
	protected void opSaveProductData() {
		log("��Ʒ���洦����....");
		try {
			Object[][] productData = (Object[][]) this.inputFromClient.readObject();
			this.myProductDataAccessor.saveProducts(productData);
			new ProductDataAccessor();
			log("��Ʒ���洦�����");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}