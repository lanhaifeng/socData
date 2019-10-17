package com.mchz.socData.util;

import java.util.Random;

/**
 * generateData
 * 2019/10/17 10:30
 * 生成数据工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class GenerateDataUtil {

	public String generateIp(){
		return "";
	}

	public String generateActionName(){
		String[] actionNames = "delete,write,move,invoke,run".split(",");
		Random random = new Random();
		random.nextInt(3);
		return actionNames[random.nextInt(3)];
	}
}
