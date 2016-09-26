package com.polysoft.policy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.polysoft.utils.TextUtil;

public class PropertiesInfo {

	private String path;
	private Properties properties;
	
	
	public PropertiesInfo(String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
		this.properties = this.readProperties(path);
	}
	
	public String getValue(String key, String defValue) {
		return this.properties.getProperty(key, defValue);
	}
	
	public void setValue(String key, String value) {
		this.properties.setProperty(key, value);
	}
	
	public String save(String outPathDir) {
		if(!TextUtil.isEmpty(outPathDir)) {
			this.path = outPathDir + "/" + new File(this.path).getName();
		}
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(new File(this.path));
			this.properties.store(fos, "");
			return this.path;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	
	private Properties readProperties(String path) {
		FileInputStream fis = null;
		Properties properties = new Properties();
		try {
			fis = new FileInputStream(new File(path));
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
}
