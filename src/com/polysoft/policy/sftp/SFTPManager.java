package com.polysoft.policy.sftp;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.polysoft.policy.release.ReleaseInfo;
import com.polysoft.policy.release.ServerConfig;

public class SFTPManager {

	public static ChannelSftp connect(ServerConfig config){
		SFTPConnectParamter paramter = new SFTPConnectParamter();
		paramter.setHost(config.getHost());
		paramter.setPort(config.getPort());
		paramter.setUsername(config.getUsername());
		paramter.setPassword(config.getPassword());
//		paramter.setHost("192.168.180.17");
//		paramter.setPort(22);
//		paramter.setUsername("root");
//		paramter.setPassword("ncl@blrj6");
		return connect(paramter);
	}
	
	public static ChannelSftp connect(SFTPConnectParamter paramter) {
		ChannelSftp result = null;
		JSch jsch = new JSch();
		try {
			Session session = jsch.getSession(paramter.getUsername(), paramter.getHost(), paramter.getPort());
			if(null == session) {
				System.err.println("connected result session is null");
				return result;
			}
			session.setPassword(paramter.getPassword());
			Properties properties = new Properties();
			properties.put("StrictHostKeyChecking", "no");
			session.setConfig(properties);
			//连接
			session.connect();
			// 设置通道
			Channel channel = session.openChannel("sftp");
			channel.connect();
			result = (ChannelSftp) channel;
			result.setFilenameEncoding("UTF-8");  
		} catch (JSchException e) {
			e.printStackTrace();
			System.err.println("connect exception; msg =>" + e.getMessage());
		} catch (SftpException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void downloadFile(ChannelSftp sftp, ReleaseInfo info) {
		SFTPDownLocalyPathFile down = new SFTPDownLocalyPathFile(info);
		down.downFile(sftp);
	}
	
	
	public static void uploadFile(ChannelSftp sftp, ReleaseInfo info) {
		
		SFTPUpload upload = new SFTPUpload(info.getReleaseServerDir(), info.getReleaseFileDir());
		upload.upload(sftp);
	}
}
