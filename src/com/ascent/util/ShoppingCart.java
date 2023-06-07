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
	public void addProduct(Product myProduct,int buyNum) {
		SCProduct product;
		boolean bo = false;
		for (int i = 0; i < shoppingList.size(); i++) {
			product = shoppingList.get(i);
			if (myProduct.getProductname().trim().equals(product.getTheProduct().getProductname().trim())) {
				product.setNum(product.getNum() + buyNum);
				bo = true;
				break;
			}
		}
		if (!bo) {
			shoppingList.add(new SCProduct(myProduct));
			for (int i = 0; i < shoppingList.size(); i++) {
				product = shoppingList.get(i);
				if (myProduct.getProductname().trim().equals(product.getTheProduct().getProductname().trim())) {
					product.setNum(buyNum);
					bo = true;
					break;
				}
			}
		}
	}

	/**
	 * ��չ��ﳵ��������Ʒ
	 */
	public void clearProduct() {
		shoppingList.clear();
	}

}
