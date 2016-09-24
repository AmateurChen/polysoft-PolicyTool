package com.polysoft.policy.release;

import java.util.HashMap;
import java.util.Map;

import com.polysoft.utils.TextUtil;

public class PathTool {

	private static final Map<String, ServerConfig> configPath = new HashMap<String, ServerConfig>();
	
	
	
	
	
	
	
	public static void addServerConfig(ServerConfig config) {
		configPath.put(config.getEnvironment(), config);
	}
	
	public static ServerConfig getServerConfig(String environment) {
		ServerConfig serverConfig = configPath.get(environment);
		if(null == serverConfig) {
			serverConfig = initServerConfig();
			addServerConfig(serverConfig);
			return initServerConfig();
		}
		return serverConfig;
	}
	
	private static ServerConfig initServerConfig() {
		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setEnvironment("17技术测试环境");
		serverConfig.setHost("192.168.180.17");
		serverConfig.setPort(22);
		serverConfig.setUsername("root");
		serverConfig.setPassword("ncl@blrj6");
		
		return serverConfig;
	}
	
	public static String getOutMd5FilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_2.0.xml";
	}
	
	public static String getOutMd5ProductFilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_product.xml";
	}
	
}
