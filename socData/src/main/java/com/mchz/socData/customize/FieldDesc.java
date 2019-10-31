package com.mchz.socData.customize;

import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * generateData
 * 2019/10/30 14:56
 * 字段描述
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class FieldDesc {
	//字段名
	@NotNull
	private String name;
	private List<FormatDesc> formatDescs;

	public Object formate(){
		Object value = null;
		FormatDesc formatDesc = getFormatDesc();
		if(formatDesc == null){
			return value;
		}

		if(StringUtils.isNotBlank(formatDesc.getGenerateDataUtilFun()) && formatDesc.getGenerateDataUtilFun().contains("#")){
			String[] generateDataUtil = formatDesc.getGenerateDataUtilFun().split("#");
			try {
				Class util = Class.forName(generateDataUtil[0]);
				Method method = util.getMethod(generateDataUtil[1]);
				value = method.invoke(null, null);
			} catch (Exception e) {
				System.out.println("构造数据错误：" + ExceptionUtils.getFullStackTrace(e));
				throw new RuntimeException("构造数据错误");
			}
		}else {
			value = Optional.ofNullable(formatDesc.getDefaultValue()).orElse(null);
		}
		formatDesc.setFormatCount(formatDesc.getFormatCount() - 1);

		return value;
	}

	private FormatDesc getFormatDesc(){
		return Optional.ofNullable(formatDescs).orElse(new ArrayList<>()).stream().filter(formatDesc -> formatDesc.getFormatCount() > 0).findAny().get();
	}
}
