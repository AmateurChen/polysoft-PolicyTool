package com.polysoft.utils; 

import org.apkinfo.api.GetApkInfo;
import org.apkinfo.api.domain.ApkInfo;

public class ApkInfoTools { 
    
	public static void main(String[] args) {
		String filePath = "C:\\Users\\Thinkpad\\Downloads\\NCI-new.apk";
		System.out.println(getApkPackageName(filePath));
	}
	
    public static String getApkPackageName(String filePath) {
		try {
			ApkInfo apkInfo = GetApkInfo.getApkInfoByFilePath(filePath);
			return apkInfo.getPackageName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
 