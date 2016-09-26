package com.polysoft.policy;

import com.polysoft.policy.release.PathTool;
import com.polysoft.policy.release.ReleaseInfo;
import com.polysoft.policy.release.ServerConfigInfo;
import com.polysoft.policy.ui.ReleaseOperation;
import com.polysoft.utils.TextUtil;

public class Main {

	
	private static ReleaseInfo initReleaseInfo() {
		ReleaseInfo info = new ReleaseInfo();
		// 发布环境
		info.setEnvironment("17技术测试环境");
		// 发布版本
		info.setVersion("2.001.20160926001");
		// 发布内容
		info.setContent("11");
		// 备份目录
		info.setBackupsFileDir("E:/cxtest/backups");
		// 发布至服务端目录
		info.setReleaseServerDir("/cxtest/server/transferServer1/deploy/ROOT.war/file/update/all2.0");
		// 发布文件目录
		info.setReleaseFileDir("E:/cxtest/releaseDir");
		
		return info;
	}
	
	private static ServerConfigInfo initServerConfig() {
		ServerConfigInfo serverConfig = new ServerConfigInfo();
		serverConfig.setEnvironment("17技术测试环境");
		serverConfig.setHost("192.168.180.17");
		serverConfig.setPort(22);
		serverConfig.setUsername("root");
		serverConfig.setPassword("ncl@blrj6");
		serverConfig.setServerRootPath("/cxtest/server/transferServer1/deploy/ROOT.war/file");
		serverConfig.setVersionConfigFilePath("");
		return serverConfig;
	}
	
	public static void main(String[] args) {
		PathTool.addServerConfig(initServerConfig());
		ReleaseInfo info = initReleaseInfo();
		
		ReleaseOperation operation = new ReleaseOperation(info);
		String checkResult = operation.checkReleaseInfo(info);
		if(!TextUtil.isEmpty(checkResult)) {
			System.out.println(checkResult);
		} else {
			operation.clickRelease();
		}
		System.out.println("===========>完成");
	}
	
}
