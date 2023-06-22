package com.ascent.util;

/**
 * socket������漰�ı�־
 * @author ascent
 * @version 1.0
 */
public interface ProtocolPort {
	/**
	 * Ĭ�϶˿ں�
	 */
	public static final int DEFAULT_PORT = 5170;

	/**
	 * Ĭ�Ϸ�������ַ
	 */
	public static final String DEFAULT_HOST = "localhost";
	/**
	 * ��ȡ������Ʒ�������Ʊ�־
	 */
	public static final int OP_GET_PRODUCT_CATEGORIES = 100;

	/**
	 * ���ݷ������ƻ�ø÷����µ�������Ʒ�����־
	 */
	public static final int OP_GET_PRODUCTS = 101;

	/**
	 * ��ȡ�û���־
	 */
	public static final int OP_GET_USERS = 102;

	/**
	 * ע���û���־
	 */
	public static final int OP_ADD_USERS = 103;

	/**
	 * ��֤�뷢�ͱ�־
	 */
	public static final int OP_SEND_CODE = 104;

	/**
	 * ����û���Ϣ
	 */
	public static final int OP_CLEAR_USERS = 105;

	/**
	 * �����û���־
	 */
	public static final int OP_UPDATE_USERS = 106;

	public static final int OP_GET_FEEDBACK = 107;

	public static final int OP_GET_FEEDBACK_NUM = 108;

	/**
	 * ��ȡ����Ա������Ϣ��־
	 */
	public static final int OP_GET_PERSON_MSG = 109;

	/**
	 * �������Ա����ͷ���־
	 */
	public static final int OP_SAVE_PERSON_IMAGE_MSG = 110;

	/**
	 * �������Ա�����ǳƺ����ձ�־
	 */
	public static final int OP_SAVE_PERSON_INFO = 111;

	/**
	 * ������Ʒ��Ϣ��־
	 */
	public static final int OP_SAVE_PRODUCTS = 112;
}
