package com.polysoft.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	public static void main(String[] args) {
		String path = "data/nci1/product/00181000.zip";
		System.out.println(checkReleaseDirectory(path));
	}
	
	
	public static boolean checkReleaseDirectory(String path) {
		return path.matches(".*[/\\\\]+nci[/\\\\]+.*");
	}
	
	
	public static String replaceMd5Path(String path) {
		Pattern compile = Pattern.compile("[/\\\\]+nci[/\\\\]+.*");
		Matcher matcher = compile.matcher(path);
		while(matcher.find()) {
			return matcher.group().replaceAll("[\\\\]+", "/");
		}
		return "";
	}
	
	public static String fileIsUpdate(String path) {
		if(path.matches(".*[/\\\\]+document[/\\\\]+.*")) {
			return "0";
		}
		return "1";
	}
	
	public static boolean isProductZipPath(String path) {
		return path.matches(".*[/\\\\]+[\\d]{8}\\.zip");
	}
}
