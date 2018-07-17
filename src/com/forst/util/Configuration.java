package com.forst.util;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Configuration {
private Configuration(){}
	
	public  static final Version LOCAL_VERSION=Version.LUCENE_30;
	//public  static final Analyzer ANALYZER=new StandardAnalyzer(LOCAL_VERSION);
	//中文分词
	//使用IKAnalyzer,当参数为true时，表示使用最大词长分词，false表示使用细粒度分词
	public static final Analyzer ANALYZER = new IKAnalyzer(true);
	private static Directory DIRECTORY;
	private static String PATH="e:/testdir/lucenedir";
	
	static{
		try{
			DIRECTORY=FSDirectory.open(new File(PATH));
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static Directory getDIRECTORY() {
		return DIRECTORY;
	}
}
