package com.mchz.socData.customize;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * generateData
 * 2019/10/30 14:57
 * 格式化描述
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class FormatDesc{
	//默认值
	private String defaultValue;
	//反射生成数据的工具类名和方法名，如：com.mchz.socData.util.GenerateDataUtil#generatePort
	private String generateDataUtilFun;
	//格式化次数
	private int formatCount;
}
