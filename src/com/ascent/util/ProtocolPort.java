package com.ascent.util;

/**
 * socket编程所涉及的标志
 * @author ascent
 * @version 1.0
 */
public interface ProtocolPort {
	/**
	 * 默认端口号
	 */
	public static final int DEFAULT_PORT = 5170;

	/**
	 * 默认服务器地址
	 */
	public static final String DEFAULT_HOST = "localhost";
	/**
	 * 获取所有商品分类名称标志
	 */
	public static final int OP_GET_PRODUCT_CATEGORIES = 100;

	/**
	 * 根据分类名称获得该分类下的所有商品对象标志
	 */
	public static final int OP_GET_PRODUCTS = 101;

	/**
	 * 获取用户标志
	 */
	public static final int OP_GET_USERS = 102;

	/**
	 * 注册用户标志
	 */
	public static final int OP_ADD_USERS = 103;

	/**
	 * 验证码发送标志
	 */
	public static final int OP_SEND_CODE = 104;

	/**
	 * 清空用户信息
	 */
	public static final int OP_CLEAR_USERS = 105;

	/**
	 * 更新用户标志
	 */
	public static final int OP_UPDATE_USERS = 106;

	public static final int OP_GET_FEEDBACK = 107;

	public static final int OP_GET_FEEDBACK_NUM = 108;

	/**
	 * 读取管理员个人信息标志
	 */
	public static final int OP_GET_PERSON_MSG = 109;

	/**
	 * 保存管理员个人头像标志
	 */
	public static final int OP_SAVE_PERSON_IMAGE_MSG = 110;

	/**
	 * 保存管理员个人昵称和生日标志
	 */
	public static final int OP_SAVE_PERSON_INFO = 111;

	/**
	 * 保存商品信息标志
	 */
	public static final int OP_SAVE_PRODUCTS = 112;
}
