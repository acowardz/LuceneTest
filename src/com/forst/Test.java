package com.forst;

import java.util.List;

import com.forst.test.LuceneTest;

public class Test {
	
	public static void testInsert(){
		Good good=new Good("商品 017","反动商品17",33.3,"德丽莎认为天命不好17");
		LuceneTest lucene = new LuceneTest();
		lucene.insert(good);
		System.out.println("--------操作成功--------");
	}
	
	public static void testUpdate(){
		Good good=new Good("good001","good one",11.0,"one update is good");
		LuceneTest lucene = new LuceneTest();
		lucene.update(good);
		System.out.println("--------操作成功--------");
	}
	
	public static void testSelect(){
		LuceneTest lucene = new LuceneTest();
		List<Good> goods = lucene.select("反动");
		for (Good good : goods) {
			System.out.println(good.getGoodId()+"---"+good.getGoodsName()+"---"+good.getGoodsRemark());
		}
	}

	public static void testDelete(){
		LuceneTest lucene = new LuceneTest();
		lucene.delete("good002");
		System.out.println("操作完成");
	}
	
	public static void testSelectPage(){
		LuceneTest lucene = new LuceneTest();
		List<Good> goods = lucene.selectPage("good", 5, 2);
		for (Good good : goods) {
			System.out.println(good.getGoodId()+"---"+good.getGoodsName()+"---"+good.getGoodsRemark());
		}
	}
	public static void testSelectSort(){
		LuceneTest lucene = new LuceneTest();
		List<Good> goods = lucene.selectSort("商品");
		for (Good good : goods) {
			System.out.println(good.getGoodId()+"---"+good.getGoodsName()+"---"+good.getGoodsPrice()+"---"+good.getGoodsRemark());
		}
	}
	
	public static void selectHightLightSort(){
		LuceneTest lucene = new LuceneTest();
		List<Good> goods = lucene.selectHightLightSort("商品");
		for (Good good : goods) {
			System.out.println(good.getGoodId()+"---"+good.getGoodsName()+"---"+good.getGoodsPrice()+"---"+good.getGoodsRemark());
		}
	}
	
	public static void main(String[] args) {
		testInsert();
		//testUpdate();
		//testDelete();
		testSelect();
		//testSelectPage();
		//testSelectSort();
		//selectHightLightSort();
	}

}
