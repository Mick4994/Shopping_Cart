package com.ascent.util;

import java.util.*;
import java.io.*;
import com.ascent.bean.Product;
import com.ascent.bean.User;

/**
 * 产品数据读取的实现类
 * @author ascent
 * @version 1.0
 */
public class ProductDataAccessor extends DataAccessor {

	// ////////////////////////////////////////////////////
	//
	// 产品文件格式如下
	// 产品名称,化学文摘登记号,结构图,公式,价格,数量,类别
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////
	//
	// 用户文件格式如下
	// 用户帐号,用户密码,用户权限
	// ----------------------------------------------------
	//
	// ////////////////////////////////////////////////////
	/**
	 * 商品信息数据文件名
	 */
	protected static final String PRODUCT_FILE_NAME = "product.txt";

	/**
	 * 用户信息数据文件名
	 */
	protected static final String USER_FILE_NAME = "user.txt";

	/**
	 * 数据记录的分割符
	 */
	protected static final String RECORD_SEPARATOR = "----------";

	/**
	 * 默认构造方法
	 */
	public ProductDataAccessor() {
		load();
	}


	/**
	 * 读取数据的方法
	 */
	@Override
	public void load() {

		dataTable = new HashMap<String,ArrayList<Product>>();
		userTable = new HashMap<String,User>();

		ArrayList<Product> productArrayList = null;
		StringTokenizer st = null;

		Product productObject = null;
		User userObject = null;
		String line = "";

		String productName, cas, structure, formula, price, realstock, category;
		String userName, password, authority;

		try {
			log("读取文件: " + PRODUCT_FILE_NAME + "...");
			BufferedReader inputFromFile1 = new BufferedReader(new FileReader(PRODUCT_FILE_NAME));

			while ((line = inputFromFile1.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue; // 跳过空行，获取下一行
				}

				st = new StringTokenizer(line, ",");
				//每行的数据 通过字段分割 分别存储于相应的变量中
				productName = st.nextToken().trim();
				cas = st.nextToken().trim();
				structure = st.nextToken().trim();
				formula = st.nextToken().trim();
				price = st.nextToken().trim();
				realstock = st.nextToken().trim();
				category = st.nextToken().trim();

				productObject = getProductObject(productName, cas, structure,formula, price, realstock, category);

				if (dataTable.containsKey(category)) {//该类别的键如果存在，则获取相应的 ArrayList
					productArrayList = dataTable.get(category);
				} else {   //否则，创建一个新的 ArrayList 并将其添加到 dataTable 中
					productArrayList = new ArrayList<Product>();
					dataTable.put(category, productArrayList);
				}
				productArrayList.add(productObject);
			}

			inputFromFile1.close();
			log("文件读取结束!");

			line = "";
			log("读取文件: " + USER_FILE_NAME + "...");
			BufferedReader inputFromFile2 = new BufferedReader(new FileReader(USER_FILE_NAME));
			while ((line = inputFromFile2.readLine()) != null) {

				st = new StringTokenizer(line, ",");

				userName = st.nextToken().trim();
				password = st.nextToken().trim();
				authority = st.nextToken().trim();
				userObject = new User(userName, password, Integer.parseInt(authority));

				if (!userTable.containsKey(userName)) {
					userTable.put(userName, userObject);
				}
			}

			inputFromFile2.close();
			log("文件读取结束!");
			log("准备就绪!\n");
		} catch (FileNotFoundException exc) {
			log("没有找到文件: " + PRODUCT_FILE_NAME + " 或 "+USER_FILE_NAME+".");
			log(exc);
		} catch (IOException exc) {
			log("读取文件发生异常: " + PRODUCT_FILE_NAME+ " 或 "+USER_FILE_NAME+".");
			log(exc);
		}
	}

	/**
	 * 清空用户数据
	 */
	public void deleteUser() {
		try {
			log("清空文件: " + USER_FILE_NAME + "...");
			File file = new File(USER_FILE_NAME);

			// 检查文件是否存在
			if (file.exists()) {
				synchronized (file) {
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
						// 清空文件内容
						writer.write(""); // 或者 writer.write("\n") 以保留一个换行符

						// 刷新缓冲区
						writer.flush();

						log("文件清空完成!");
					} catch (IOException e) {
						log("清空文件发生异常: " + USER_FILE_NAME + ".");
						log(e);
					}
				}
			} else {
				log("文件不存在: " + USER_FILE_NAME + ".");
			}
		} catch (Exception e) {
			log("清空文件发生异常: " + USER_FILE_NAME + ".");
			log(e);
		}
	}



	/**
	 * 返回带有这些参数的商品对象
	 * @param productName 药品名称
	 * @param cas 化学文摘登记号
	 * @param structure 结构图名称
	 * @param formula 公式
	 * @param price 价格
	 * @param realstock 数量
	 * @param category 类别
	 * @return new Product(productName, cas, structure, formula, price, realstock, category);
	 */
	private Product getProductObject(String productName, String cas,
			String structure, String formula, String price, String realstock, String category) {
		return new Product(productName, cas, structure, formula, price, realstock, category);
	}

	/**
	 * 保存数据
	 */
	@Override
	public void save(User user) {
		log("读取文件: " + USER_FILE_NAME + "...");
		try {
			String userinfo = user.getUsername() + "," + user.getPassword() + "," + user.getAuthority();
			RandomAccessFile fos = new RandomAccessFile(USER_FILE_NAME, "rws");
//			fos.seek(fos.length());
//			fos.write(("\n" + userinfo).getBytes());
			// 检查文件是否为空
			if (fos.length() > 0) {
				fos.writeBytes("\n"); // 文件不为空，加上换行符
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 日志方法.
	 */
	@Override
	protected void log(Object msg) {
		System.out.println("ProductDataAccessor类: " + msg);
	}

	@Override
	public HashMap<String,User> getUsers() {
		this.load();
		return this.userTable;
	}

}