package com.mchz.socData.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * generateData
 * 2019/10/17 10:30
 * 生成数据工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class GenerateDataUtil {

	/**
	 * 2019/10/17 11:30
	 * 生成随机ip
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static String generateIp() {
		int[][] range = {{607649792, 608174079}, // 36.56.0.0-36.63.255.255
				{1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
				{1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
				{2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
				{2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
				{-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
				{-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
				{-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
				{-770113536, -768606209}, // 210.25.0.0-210.47.255.255
				{-569376768, -564133889}, // 222.16.0.0-222.95.255.255
		};

		int index = RandomUtil.generateZeroRandom(9);
		return num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
	}

	private static String num2ip(int ip) {
		int[] b = new int[4];
		b[0] = (ip >> 24) & 0xff;
		b[1] = (ip >> 16) & 0xff;
		b[2] = (ip >> 8) & 0xff;
		b[3] = ip & 0xff;
		return Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);
	}

	/**
	 * 2019/10/17 11:31
	 * 生成随机操作名
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static String generateActionName(){
		String[] actionNames = "delete,write,move,invoke,run".split(",");
		return actionNames[RandomUtil.generateZeroRandom(4)];
	}

	/**
	 * 2019/10/17 11:31
	 * 生成随机危险级别，1，2，3低中高
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static int generateLevel(){
		Integer[] levels = new Integer[]{1,2,3};
		return levels[RandomUtil.generateZeroRandom(2)];
	}

	/**
	 * 2019/10/17 11:31
	 * 生成type
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static int generateType(){
		Integer[] types = new Integer[]{1,2,3,4,5,6,7};
		return types[RandomUtil.generateZeroRandom(6)];
	}

	/**
	 * 2019/10/17 11:36
	 * 随机生成指定数量的uuid
	 *
	 * @param count
	 * @author lanhaifeng
	 * @return java.util.List<java.lang.String>
	 */
	public static List<String> generateSourceDvuids(int count){
		if(count < 1) return null;
		List<String> sourceDvuids = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			sourceDvuids.add(UUID.randomUUID().toString());
		}

		return sourceDvuids;
	}

	/**
	 * 2019/10/17 11:31
	 * 生成随机端口
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static int generatePort(){
		return RandomUtil.generateRangeRandom(1024, 65535);
	}
}
