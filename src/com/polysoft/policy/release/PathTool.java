package com.polysoft.policy.release;

import java.util.HashMap;
import java.util.Map;

import com.polysoft.utils.TextUtil;

public class PathTool {

	private static final Map<String, ServerConfigInfo> configPath = new HashMap<String, ServerConfigInfo>();
	
	
	
	
	
	
	
	public static void addServerConfig(ServerConfigInfo config) {
		configPath.put(config.getEnvironment(), config);
	}
	
	public static ServerConfigInfo getServerConfig(String environment) {
		ServerConfigInfo serverConfig = configPath.get(environment);
		if(null == serverConfig) {
			serverConfig = initServerConfig();
			addServerConfig(serverConfig);
			return initServerConfig();
		}
		return serverConfig;
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
	
	public static String getOutMd5FilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_2.0.xml";
	}
	
	public static String getOutMd5ProductFilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_product.xml";
	}
	
}
