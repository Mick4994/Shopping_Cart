package com.ascent.bean;

/**
 * ���ﳵ�嵥��Ԫ��
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

