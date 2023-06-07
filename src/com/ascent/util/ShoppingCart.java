package com.ascent.util;

import java.util.ArrayList;
import com.ascent.bean.Product;
import com.ascent.bean.SCProduct;

/**
 * ���ﳵ
 * @author ascent
 * @version 1.0
 */
public class ShoppingCart {

	/**
	 * ��Ź�����Ʒ��Ϣ
	 */
	private ArrayList<SCProduct> shoppingList = new ArrayList<SCProduct>();

	/**
	 * ��ȡ���й�����Ʒ��Ϣ
	 * @return shoppingList
	 */
	public ArrayList<SCProduct> getShoppingList() {
		return this.shoppingList;
	}

	/**
	 * �����Ʒ�����ﳵ
	 * @param myProduct
	 */
	public void addProduct(Product myProduct) {
		SCProduct product;
		boolean bo = false;
		for (int i = 0; i < shoppingList.size(); i++) {
			product = shoppingList.get(i);
			if (myProduct.getProductname().trim().equals(product.getTheProduct().getProductname().trim())) {
				product.setNum(product.getNum() + 1);
				bo = true;
				break;
			}
		}
		if (!bo) {
			shoppingList.add(new SCProduct(myProduct));
		}
	}

	/**
	 * ɾ�����ﳵ��ĳ����Ʒ
	 */
	public void delProduct(String productName) {
		SCProduct product;
		for (int i = 0; i < shoppingList.size(); i++) {
			product = shoppingList.get(i);
			if (productName.equals(product.getTheProduct().getProductname())) {
				shoppingList.remove(i);
				return;
			}
		}
		System.out.println("����Ʒ������");
	}


	/**
	 * ��չ��ﳵ��������Ʒ
	 */
	public void clearProduct() {
		shoppingList.clear();
	}

}
