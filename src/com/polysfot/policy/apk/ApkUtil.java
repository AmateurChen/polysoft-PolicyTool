package com.polysfot.policy.apk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import org.apkinfo.api.util.AXMLPrinter2;

import com.polysoft.xml.XmlManipulateDom;

public class ApkUtil {

	public static void main(String[] args) {
		new ApkUtil().initApk("E:\\cxtest\\releaseDir\\MagnumMobile1.2.apk");
	}
	
	public ApkUtil() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void initApk(String path) {
		InputStream is = null;
		try {
			ZipFile zipFile = new ZipFile(new File(path));
			FileHeader fileHeader = zipFile.getFileHeader("AndroidManifest.xml");
			if(null == fileHeader) {
				return ;
			}
			
			System.out.println("getCrc32 =" + fileHeader.getCrc32());
			is  = zipFile.getInputStream(fileHeader);
			String xml = DecompileApkXml.parser(is);
		    XmlManipulateDom dom = new XmlManipulateDom();
		    dom.parser(new AndroidManifestXml(), xml);
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
}
