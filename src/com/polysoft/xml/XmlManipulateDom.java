package com.polysoft.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.polysoft.utils.TextUtil;

public class XmlManipulateDom implements XmlManipulateImp {

	
	
	@Override
	public Object parser(XmlManipulateDomImp domImp, InputStream xmlStream) throws Exception {
		// TODO Auto-generated method stub
		if(null == xmlStream)
			throw new NullPointerException("parser XML is null");
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(xmlStream);
			return domImp.manipulate(document);
		} catch (ParserConfigurationException e) {
			throw new Exception("初始化DOM异常（0x00001）", e);
		} catch (SAXException e) {
			throw new Exception("初始化DOM异常（0x00002）", e);
		} catch (IOException e) {
			throw new Exception("XML加载DOM异常（0x00003）", e);
		}
	}

	@Override
	public Object parser(XmlManipulateDomImp manipulateImp, File xmlFile) throws Exception{
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(xmlFile);
			return this.parser(manipulateImp, fis);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if(null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Object parser(XmlManipulateDomImp domImp, String xmlInfo) throws Exception{
		// TODO Auto-generated method stub
		if(TextUtil.isEmpty(xmlInfo)) 
			throw new NullPointerException("请指定需要解析的XML字符串");
		
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(xmlInfo.getBytes());
			return this.parser(domImp, is);
		} catch (Exception e) {
			throw e;
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	@Override
	public Object createXml(XmlManipulateDomImp domImp, String outputFilePath) throws Exception{
		// TODO Auto-generated method stub
		StreamResult streamResult = null;
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Object obj = domImp.manipulate(document);
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			//设置编码
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//是否进行代码缩进
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(outputFilePath));
			streamResult = new StreamResult(pw);
			transformer.transform(source, streamResult);
			pw.flush();
			pw.close();
			return obj;
		} catch (ParserConfigurationException e) {
			throw new Exception("初始化DOM异常（0x10001）", e);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
