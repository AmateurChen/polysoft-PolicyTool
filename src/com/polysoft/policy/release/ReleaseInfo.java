package com.polysoft.policy.release;

public class ReleaseInfo {

	// 发布环境
	String environment;
	// 发布版本
	String version;
	// 发布内容
	String content;
	// 本地发布文件目录
	String releaseFileDir;
	// 文件备份目录，备份服务器上将被替换的文件
	String backupsFileDir;
	// 发布文件对应服务器上的目录
	String releaseServerDir;
	
	public static ReleaseInfo getReleaseInfo() {
		String environment = "17技术测试环境";
		String version = "2.001.20160922001";
		String content = "修复各种问题";
		String releaseFileDir = "E:/cxtest/releaseDir";
		String releaseServerDir = "/cxtest/server/transferServer1/deploy/ROOT.war/file/update/all2.0";
		String backupsFileDir = "E:/cxtest/backups";
		
		ReleaseInfo info = new ReleaseInfo();
		info.setEnvironment(environment);
		info.setVersion(version);
		info.setContent(content);
		info.setReleaseFileDir(releaseFileDir);
		info.setReleaseServerDir(releaseServerDir);
		info.setBackupsFileDir(backupsFileDir);
		
		return info;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReleaseFileDir() {
		return releaseFileDir;
	}

	public void setReleaseFileDir(String releaseFileDir) {
		this.releaseFileDir = releaseFileDir;
	}

	public String getBackupsFileDir() {
		return backupsFileDir;
	}

	public void setBackupsFileDir(String backupsFileDir) {
		this.backupsFileDir = backupsFileDir;
	}

	public String getReleaseServerDir() {
		return releaseServerDir;
	}

	public void setReleaseServerDir(String releaseServerDir) {
		this.releaseServerDir = releaseServerDir;
	}
	
}
