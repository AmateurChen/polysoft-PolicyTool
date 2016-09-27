package com.polysfot.policy.apk;

import java.io.InputStream;

import org.apkinfo.api.util.AXmlResourceParser;

public class DecompileApkXml {

	private static final float[] RADIX_MULTS = { 0.0039063F, 3.051758E-005F,
			1.192093E-007F, 4.656613E-010F };

	private static final String[] DIMENSION_UNITS = { "px", "dip", "sp", "pt",
			"in", "mm", "", "" };

	private static final String[] FRACTION_UNITS = { "%", "%p", "", "", "", "",
			"", "" };

	public static String parser(InputStream is) {
		StringBuffer result = new StringBuffer();
		
		try {
			AXmlResourceParser parser = new AXmlResourceParser();
			parser.open(is);
			while (true) {
				int type = parser.next();
				if (type == 1) {
					break;
				}
				switch (type) {
				case 0:// xml开始
					result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					break;
				case 2: // 节点
					// 节点名前缀
					String nodeNameSpacePrefix = getNamespacePrefix(parser.getPrefix());
					// 节点名
					String nodeName = parser.getName();
					result.append("<" + nodeNameSpacePrefix + nodeName +" ");

					int namespaceCountBefore = parser.getNamespaceCount(parser.getDepth() - 1);
					int namespaceCount = parser.getNamespaceCount(parser.getDepth());
					for (int i = namespaceCountBefore; i != namespaceCount; i++) {
						String namespacePrefix = parser.getNamespacePrefix(i);
						String namespaceUri = parser.getNamespaceUri(i);
						//第一个属性，带地址
						result.append("sxmlns:"+namespacePrefix+"="+"\""+namespaceUri+"\" ");
					}
					//其他属性
					for (int i = 0; i != parser.getAttributeCount(); i++) {
						// 属性名前缀
						String namespacePrefix = getNamespacePrefix(parser.getAttributePrefix(i));
						// 属性名
						String attributeName = parser.getAttributeName(i);
						// 属性值
						String attributeValue = getAttributeValue(parser, i);
						result.append(namespacePrefix + attributeName + "=\"" + attributeValue +"\" ");
					}
					result.append(">");
					break;
				case 3://节点结束
					// 结束节点前缀
					String endNodeNameSpacePrefix = getNamespacePrefix(parser.getPrefix());
					// 结束节点名
					String endNodeName = parser.getName();
					result.append("</"+endNodeNameSpacePrefix + endNodeName + ">");
					
					break;
				case 4:
					//节点值
					result.append(parser.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	private static String getNamespacePrefix(String prefix) {
		if ((prefix == null) || (prefix.length() == 0)) {
			return "";
		}
		return prefix + ":";
	}

	private static String getAttributeValue(AXmlResourceParser parser, int index) {
		int type = parser.getAttributeValueType(index);
		int data = parser.getAttributeValueData(index);
		if (type == 3) {
			return parser.getAttributeValue(index);
		}
		if (type == 2) {
			return String.format("?%s%08X", new Object[] { getPackage(data),
					Integer.valueOf(data) });
		}
		if (type == 1) {
			return String.format("@%s%08X", new Object[] { getPackage(data),
					Integer.valueOf(data) });
		}
		if (type == 4) {
			return String.valueOf(Float.intBitsToFloat(data));
		}
		if (type == 17) {
			return String.format("0x%08X",
					new Object[] { Integer.valueOf(data) });
		}
		if (type == 18) {
			return data != 0 ? "true" : "false";
		}
		if (type == 5) {
			return Float.toString(complexToFloat(data))
					+ DIMENSION_UNITS[(data & 0xF)];
		}
		if (type == 6) {
			return Float.toString(complexToFloat(data))
					+ FRACTION_UNITS[(data & 0xF)];
		}
		if ((type >= 28) && (type <= 31)) {
			return String.format("#%08X",
					new Object[] { Integer.valueOf(data) });
		}
		if ((type >= 16) && (type <= 31)) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>",
				new Object[] { Integer.valueOf(data), Integer.valueOf(type) });
	}

	private static String getPackage(int id) {
		if (id >>> 24 == 1) {
			return "android:";
		}
		return "";
	}

	private static void log(String format, Object[] arguments) {
		System.out.printf(format, arguments);
		System.out.println();
	}

	public static float complexToFloat(int complex) {
		return (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4 & 0x3)];
	}
}
