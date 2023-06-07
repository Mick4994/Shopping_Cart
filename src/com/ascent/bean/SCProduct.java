package com.ascent.bean;

/**
 * 购物车清单的元素
 */
public class SCProduct {
    private int num = 1;
    private Product theProduct;

    public SCProduct(Product theProduct) {
        this.theProduct = theProduct;
    }

    public Product getTheProduct() {
        return theProduct;
    }
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

