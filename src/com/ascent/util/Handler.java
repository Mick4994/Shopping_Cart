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
					System.out.println("错误代码");
				}
			}
		} catch (IOException exc) {
			log(exc);
		}
	}

	/**
	 * 保存管理员个人生日和昵称的保存
	 */
	private void opSetPersonSMessage() {
		// 处理保存个人信息请求
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
		System.out.println("个人生日和昵称信息已保存");
	}

	/**
	 * 保存管理员个人头像的保存
	 */
	private void opSetPersonSImageMessage() {
		// 接收图像数据
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

			// 保存图像到文件
			Path imagePath = Path.of("./src/images/administratorImage.jpg");
			try {
				Files.write(imagePath, imageData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("头像已保存到文件: " + imagePath.toAbsolutePath());
		}
	}

	/**
	 * 处理管理员个人信息的读取
	 */
	private void opGetPersonMessage() {
		try {
			// 读取图像文件并将其转换为字节数组
			File imageFile = new File("./src/images/administratorImage.jpg");
			BufferedImage image = ImageIO.read(imageFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageData = baos.toByteArray();

			// 发送图像数据给客户端
			outputToClient.writeObject("image"); // 发送图像数据的标识
			outputToClient.flush();
			outputToClient.writeObject(imageData); // 发送图像数据
			outputToClient.flush();
			System.out.println("图像已发送");

			// 读取文件内容为String类型
			String filePath = "./src/images/administratorMessage.txt";
			String fileContent = readFileAsString(filePath);

			// 发送文件内容给客户端
			outputToClient.writeObject(fileContent);
			outputToClient.flush();
			System.out.println("文件内容已发送");

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
	 * 删除用户信息
	 */
	private void opDeleteUser() {
		new ProductDataAccessor().deleteUser();
	}

	/**
	 * 随机生成验证码并返回
	 */
	private void opGetMsg() {
		String msg = String.valueOf((int)(Math.random() * 100000));
		try {
			outputToClient.writeObject(msg);
			outputToClient.flush();
		} catch (IOException exe) {
			log("发生异常：" + exe);
		}
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

			if(category.equals("请选择药品分类")) {
				log("返回未选状态");
				return;
			}

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
			this.myProductDataAccessor.save(user);
		} catch (IOException e) {
			log("发生异常:  " + e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log("发生异常:  " + e);
			e.printStackTrace();
		}
	}

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
	 * 处理商品信息保存
	 */
	protected void opSaveProductData() {
		log("商品保存处理中....");
		try {
			Object[][] productData = (Object[][]) this.inputFromClient.readObject();
			this.myProductDataAccessor.saveProducts(productData);
			new ProductDataAccessor();
			log("商品保存处理完成");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}