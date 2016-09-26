package com.polysoft.policy.release;

import com.polysoft.utils.TextUtil;

public class ServerConfigInfo {

	// 环境标识
	private String environment;
	// iP 地址
	private String host;
	// 端口
	private int port;
	// 用户名
	private String username;
	// 密码
	private String password;
	
	private String serverRootPath;
	
	private String md5FilePath = "";
	private String productMd5FilePath = "";
	
	private String md5ConfigFilePath ="";
	private String versionConfigFilePath = "";
	private String updateContentsFilePath = "";
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEnvironment() {
		return environment;
	}
	
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	public String getServerRootPath() {
		return serverRootPath;
	}
	
	public void setServerRootPath(String serverRootPath) {
		this.serverRootPath = serverRootPath;
	}
	
	public String getMd5FilePath() {
		if(TextUtil.isEmpty(this.md5FilePath)) {
			return this.serverRootPath + "/update/md5_2.0.xml";
		}
		return md5FilePath;
	}
	
	public void setMd5FilePath(String md5FilePath) {
		this.md5FilePath = md5FilePath;
	}
	
	public String getProductMd5FilePath() {
		if(TextUtil.isEmpty(this.productMd5FilePath)) {
			return this.serverRootPath + "/update/md5_product.xml";
		}
		return productMd5FilePath;
	}
	
	public void setProductMd5FilePath(String productMd5FilePath) {
		this.productMd5FilePath = productMd5FilePath;
	}
	
	public String getMd5ConfigFilePath() {
		if(TextUtil.isEmpty(this.md5ConfigFilePath)) {
			return this.serverRootPath + "/md5pro2.0/1.properties";
		}
		return md5ConfigFilePath;
	}
	
	public void setMd5ConfigFilePath(String md5ConfigFilePath) {
		this.md5ConfigFilePath = md5ConfigFilePath;
	}
	
	public String getVersionConfigFilePath() {
		if(TextUtil.isEmpty(this.versionConfigFilePath)) {
			return this.serverRootPath + "/download2.0/versionConfig.properties";
		}
		return versionConfigFilePath;
	}
	
	public void setVersionConfigFilePath(String versionConfigFilePath) {
		this.versionConfigFilePath = versionConfigFilePath;
	}
	
	public String getUpdateContentsFilePath() {
		if(TextUtil.isEmpty(this.updateContentsFilePath)) {
			return this.serverRootPath + "/download2.0/updateContents.properties";
		}
		return updateContentsFilePath;
	}
	
	public void setUpdateContentsFilePath(String updateContentsFilePath) {
		this.updateContentsFilePath = updateContentsFilePath;
	}
	
}
