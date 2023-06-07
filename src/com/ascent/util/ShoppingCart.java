package com.ascent.util;

import java.util.ArrayList;
import com.ascent.bean.Product;
import com.ascent.bean.SCProduct;

/**
 * 购物车
 * @author ascent
 * @version 1.0
 */
public class ShoppingCart {

	/**
	 * 存放购买商品信息
	 */
	private ArrayList<SCProduct> shoppingList = new ArrayList<SCProduct>();

	/**
	 * 获取所有购买商品信息
	 * @return shoppingList
	 */
	public ArrayList<SCProduct> getShoppingList() {
		return this.shoppingList;
	}

	/**
	 * 添加商品到购物车
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
	 * 清空购物车所购买商品
	 */
	public void clearProduct() {
		shoppingList.clear();
	}

}
