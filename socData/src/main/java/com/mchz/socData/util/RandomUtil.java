package com.mchz.socData.util;

import java.util.Random;

/**
 * generateData
 * 2019/10/17 10:34
 * 生成随机数工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class RandomUtil {

	/**
	 * 2019/10/17 11:20
	 * 生成范围为[start,end]的随机数
	 *
	 * @param start			最小随机数
	 * @param end			最大随机数
	 * @author lanhaifeng
	 * @return int
	 */
	public static int generateRangeRandom(int start, int end){
		Random rand = new Random();
		return rand.nextInt(end - start + 1) + start;
	}

	/**
	 * 2019/10/17 11:20
	 * 生成范围为[0,end]的随机数
	 *
	 * @param end			最大随机数
	 * @author lanhaifeng
	 * @return int
	 */
	public static int generateZeroRandom(int end){
		return generateRangeRandom(0, end);
	}
}
