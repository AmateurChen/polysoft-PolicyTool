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
			new RuntimeException("未找到 "+environment+" 的serverConfig信息");
		}
		return serverConfig;
	}
	
	
	
	public static String getOutMd5FilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_2.0.xml";
	}
	
	public static String getOutMd5ProductFilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_product.xml";
	}
	
}
