package com.polysoft.policy;

import java.util.List;

import com.jcraft.jsch.ChannelSftp;
import com.polysoft.policy.sftp.SFTPFile;
import com.polysoft.policy.sftp.SFTPManager;
import com.polysoft.utils.TextUtil;

public class Main {

	
	public static void main(String[] args) {
		String downFilePath = "/ncltybbpo/.bash_history";
		String outPath = "C:\\Users\\Thinkpad\\Desktop\\sftp\\";
		
		String uploadPath = "/test/sftp";
		String uploadFilePath = "C:\\Users\\Thinkpad\\Desktop\\sftp\\";
		
//		ChannelSftp sftp = SFTPManager.connect(SFTPManager.getConnectParamter17());
//		
////		SFTPManager.uploadFile(sftp, uploadPath, uploadFilePath);
//		List<SFTPFile> listFile = SFTPManager.getDirectory(sftp, downFilePath);
//		SFTPManager.downloadFile(sftp, listFile, outPath);
		System.out.println("===========>");
	}
	
}