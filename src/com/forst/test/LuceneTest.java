package com.forst.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.wltea.analyzer.lucene.IKQueryParser;

import com.forst.Good;
import com.forst.util.BeanUtil;
import com.forst.util.Configuration;
import com.forst.util.HightLightUtil;

public class LuceneTest {
	
	//增加
	public void insert(Good good){
		IndexWriter indexWriter = null;
		try {
			//创建索引库管理对象
			//索引库目录、分词器、文档字段的大小
			indexWriter = new IndexWriter(Configuration.getDIRECTORY(), Configuration.ANALYZER,MaxFieldLength.LIMITED);
			//Javabean对象转为文档对象
			Document doc = BeanUtil.javaBeanToDocument(good);
			//添加
			indexWriter.addDocument(doc);
			//将把产生.cfs的文档和原来的文档压缩并合并为一个
			indexWriter.optimize();
			//提交到索引库
			indexWriter.commit();
		} catch (Exception e) {
			//回滚
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			try {
				indexWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//修改
	public void update(Good good){
		//索引库管理对象
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(Configuration.getDIRECTORY(),Configuration.ANALYZER,MaxFieldLength.LIMITED);
			Document doc = BeanUtil.javaBeanToDocument(good);
			//根据某个字段修改,先删除，再添加
			Term term = new Term("goodId", good.getGoodId().toString());
			indexWriter.updateDocument(term, doc);
			indexWriter.commit();
		} catch (Exception e) {
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				indexWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//删除
	public void delete(String goodId){
		//索引管理对象
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(Configuration.getDIRECTORY(),Configuration.ANALYZER, MaxFieldLength.LIMITED);
			indexWriter.deleteDocuments(new Term("goodId", goodId));
			indexWriter.commit();
		} catch (Exception e) {
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				indexWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//普通查询
	public List<Good> select(String goodsName){
		List<Good> list = new ArrayList<Good>();
		IndexSearcher indexSearcher = null;
		try {
			//创建库的搜索对象
			indexSearcher = new IndexSearcher(Configuration.getDIRECTORY());
			//查询解析器
			QueryParser queryParser = new QueryParser(Configuration.LOCAL_VERSION,"goodsName",Configuration.ANALYZER);
			//将查询的字段交给查询解析器
			Query parse = queryParser.parse(goodsName);
			//查询索引库
			TopDocs search = indexSearcher.search(parse, 10);
			//
			ScoreDoc[] scoreDocs = search.scoreDocs;
			//查询真正得文档对象
			for (ScoreDoc scoreDoc : scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				System.out.println("文档内容为："+doc.get("goodId")+"|"+doc.get("goodsName")+"|"+doc.get("goodsRemark"));
				Good good = (Good) BeanUtil.documentToJavaBean(doc, Good.class);
				list.add(good);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	//分页查询
	public List<Good> selectPage(String goodsName,int pageSize,int currentPage){
		List<Good> list = new ArrayList<Good>();
 		//库的搜索对象
		IndexSearcher indexSearcher = null;
		int startRecord = (currentPage - 1)*pageSize ;
		//结束的序号+1
		int endRecord = currentPage * pageSize;
		
		if (currentPage < 1) {
			currentPage = 1;
		}
		
		try {
			indexSearcher = new IndexSearcher(Configuration.getDIRECTORY());
			//查询解析器
			QueryParser queryParser = new QueryParser(Configuration.LOCAL_VERSION,"goodsName",Configuration.ANALYZER);
			//查询的字段交给解析器
			Query parse = queryParser.parse(goodsName);
			//搜索索引库
			TopDocs search = indexSearcher.search(parse, endRecord);
			if (endRecord > search.totalHits) {
				endRecord = search.totalHits;
			}
			//得到真正得文档对象
			 ScoreDoc[] scoreDoc= search.scoreDocs;
			 for (int i=startRecord; i< endRecord ; i++) {
				//转换为document
				 Document doc = indexSearcher.doc(scoreDoc[i].doc);
				 Good good =(Good) BeanUtil.documentToJavaBean(doc, Good.class);
				 list.add(good);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (indexSearcher != null) {
				try {
					indexSearcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	//排序查询
	public List<Good> selectSort(String goodsName){
		List<Good> list = new ArrayList<Good>();
		IndexSearcher indexSearcher = null;
		try {
			//创建库的搜索对象
			indexSearcher = new IndexSearcher(Configuration.getDIRECTORY());
			//创建排序字段,降序
			SortField sortField = new SortField("goodsName", SortField.STRING, true);
			//升序
			SortField sortField2 = new SortField("goodsPrice", SortField.DOUBLE);
			//设置sort对象
			Sort sort = new Sort();
			sort.setSort(sortField,sortField2);
			//使用IKQueryParser进行多字段数据查询
			Query query=IKQueryParser.parseMultiField(new String[]{"goodsName","goodsRemark"},goodsName);
			TopDocs search = indexSearcher.search(query,null,20,sort);
			/*//查询解析器
			QueryParser queryParser = new QueryParser(Configuration.LOCAL_VERSION,"goodsName",Configuration.ANALYZER);
			//将查询的字段交给查询解析器
			Query parse = queryParser.parse(goodsName);
			//查询索引库
			TopDocs search = indexSearcher.search(parse, 10);*/
			//查询真正得文档对象
			ScoreDoc[] scoreDocs = search.scoreDocs;
			for (ScoreDoc scoreDoc : scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				System.out.println("文档内容为："+doc.get("goodId")+"|"+doc.get("goodsName")+"|"+doc.get("goodsRemark"));
				Good good = (Good) BeanUtil.documentToJavaBean(doc, Good.class);
				list.add(good);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
		//排序查询
	public List<Good> selectHightLightSort(String goodsName){
		List<Good> list = new ArrayList<Good>();
		IndexSearcher indexSearcher = null;
		try {
			//创建库的搜索对象
			indexSearcher = new IndexSearcher(Configuration.getDIRECTORY());
			//创建排序字段,降序
			SortField sortField = new SortField("goodsName", SortField.STRING, true);
			//升序
			SortField sortField2 = new SortField("goodsPrice", SortField.DOUBLE);
			//设置sort对象
			Sort sort = new Sort();
			sort.setSort(sortField,sortField2);
			//使用IKQueryParser进行多字段数据查询
			Query query=IKQueryParser.parseMultiField(new String[]{"goodsName","goodsRemark"},goodsName);
			TopDocs search = indexSearcher.search(query,null,20,sort);
			//查询真正得文档对象
			ScoreDoc[] scoreDocs = search.scoreDocs;
			for (ScoreDoc scoreDoc : scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				doc.getField("goodsName").setValue(HightLightUtil.doHighLight(query,"goodsName", doc.get("goodsName"), 5));
				doc.getField("goodsRemark").setValue(HightLightUtil.doHighLight(query,"goodsRemark", doc.get("goodsRemark"), 20));
				Good good = (Good) BeanUtil.documentToJavaBean(doc, Good.class);
				list.add(good);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				indexSearcher.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
