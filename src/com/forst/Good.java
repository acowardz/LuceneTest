package com.forst;

public class Good {
	private String goodId;
	private String goodsName;
	private Double goodsPrice;
	private String goodsRemark;

	public Good() {
	}

	public Good(String goodId,String goodsName, Double goodsPrice, String goodsRemark) {
		super();
		this.goodId = goodId;
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.goodsRemark = goodsRemark;
	}
	
	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getGoodsRemark() {
		return goodsRemark;
	}
	public void setGoodsRemark(String goodsRemark) {
		this.goodsRemark = goodsRemark;
	}
	
}
