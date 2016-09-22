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
		return configPath.get(environment);
	}
	
	public static String getOutMd5FilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_2.0.xml";
	}
	
	public static String getOutMd5ProductFilePath(String directory) {
		return TextUtil.appendSeparator(directory) + "md5_product.xml";
	}
	
}
