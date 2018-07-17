package com.forst.util;


import java.lang.reflect.Method;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

/**
 * 在Lucene包的基础上，导入bean_until包,抽取工具类
 * @author acowardz
 *
 */
public class BeanUtil {
	//Javabean -> Lucene document
	public static Document javaBeanToDocument(Object obj) throws Exception{
		//lucene 的 文档对象
		Document doc = new Document();
		//获取Javabean的Class对象
		Class clazz = obj.getClass();
		//获取Class对象对应的类或接口所声明的所有属性
		//document也有个filed，因此这里用全类名
		java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();
		for (java.lang.reflect.Field field : declaredFields) {
			//设置特权访问
			field.setAccessible(true);
			//获取字段名
			String fieldName = field.getName();
			System.out.println("--------------"+fieldName);
			//拼结方法名
			//获取首字母，改为大写
			String upperCase = fieldName.substring(0, 1).toUpperCase();
			//拼接
			String methodName = "get" + upperCase + fieldName.substring(1);
			Method method = getMethod(clazz, methodName);
			if (method != null) {
				//获取方法的返回结果
				Object invoke = method.invoke(obj, null);
				if (invoke == null) {
					continue;
				}
				doc.add(new Field(fieldName, invoke.toString(),Store.YES, Index.ANALYZED));
			}
		}
		return doc;
	}
	
	//document -> javabean
	public static Object documentToJavaBean(Document doc,Class clazz) throws Exception {
		Object obj = clazz.newInstance();
		java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();
		for (java.lang.reflect.Field field : declaredFields) {
			String name = field.getName();
			String value = doc.get(name);
			if (value == null) {
				continue;
			}
			org.apache.commons.beanutils.BeanUtils.setProperty(obj, name, value);
		}
		return obj;
	}
	
	//通过方法名获取方法
	public static Method getMethod(Class<Object> clazz,String methodName){
		Method method = null;
		try {
			//第二个为方法中的参数，没有则为空
			method = clazz.getMethod(methodName, null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return method;
	}
}
