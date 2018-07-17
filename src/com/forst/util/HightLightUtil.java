package com.forst.util;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

public class HightLightUtil {
	public static String doHighLight(Query query,String fieldName,String fieldValue, int size){
		String result="";
		try {					
			//设置高亮的格式
			Formatter formatter=new SimpleHTMLFormatter("<font color='red'>","</font>");
			//指定高亮的查询条件
			Scorer scorer=new QueryScorer(query);
			
			Highlighter highlighter=new Highlighter(formatter, scorer);
			//指定高亮后的长度，需要SimpleFragmenter类型参数，这里指定长度为参数size（结尾为标点符号或不够长的词一般不显示出来,所以一般会少几个字）
			highlighter.setTextFragmenter(new SimpleFragmenter(size));
			//设置对哪个字段进行高亮操作，返回高亮后的结果
			result=highlighter.getBestFragment(Configuration.ANALYZER, fieldName, fieldValue);
			//把高亮后的值重新赋给字段
			if(result==null){				
				if(fieldValue!=null&&fieldValue.length()>=size){
					result=fieldValue.substring(0, size);
				}else{
					result=fieldValue;
				}
			}		
			System.out.println("util.reuslt："+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return result;
	}

}
