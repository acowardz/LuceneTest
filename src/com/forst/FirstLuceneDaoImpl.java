package com.forst;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class FirstLuceneDaoImpl {

	/**
	 * 保存指定的商品信息
	 * @param goods 商品信息
	 */
	public void saveGoods(Good goods){
		
		//指定索引库目录
		Directory directory=null;
		IndexWriter indexWriter=null;
		try {
			directory = FSDirectory.open(new File("e:/testdir/lucenedir"));
			//指定分词器 
			Analyzer analyzer= new StandardAnalyzer(Version.LUCENE_30);
			/*
			 *创建索引库管理对象（主要用来增删改索引库信息）
			* MaxFieldLength，用于限制Field的大小。这个变量可以让用户有计划地
			*对大文档Field进行截取。假如取值为10000，就只索引每个Field的前
			*10000个Term（关键字）。其它的部分都不会被Lucene索引，也不能被搜索到。 */

			indexWriter=new IndexWriter(directory,analyzer,MaxFieldLength.LIMITED);
			//创建lucene文档对象，并添加字段
			Document doc=new Document();
			/*
			 * Store.YES表示把当前字段存到文档中，Store.NO表示 不把当前字段存到文档中
			 * Index：
			 * ANALYZED，表示当前字段建立索引，并且进行分词，产生多个term
			 * NOT_ANALYZED，表示当前字段建立索引，但不进行分词，整个字段值作为一个整体，产生一个term
			 * NO,不创建索引，以后不能用此字段查询
			 * 
			 */
			if (goods.getGoodsName() != null) {
				doc.add(new Field("goodsName", goods.getGoodsName(),
						Store.YES, Index.ANALYZED));
			}
			if (goods.getGoodsPrice() != null) {
				doc.add(new Field("goodsPrice", goods.getGoodsPrice()
						.toString(), Store.YES, Index.NOT_ANALYZED));
			}
			if (goods.getGoodsRemark() != null) {
				doc.add(new Field("goodsRemark", goods.getGoodsRemark(),
						Store.YES, Index.ANALYZED));
			}			
			//添加文档对象
			indexWriter.addDocument(doc);
			//提交到索引库
			indexWriter.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(indexWriter!=null){
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
}

