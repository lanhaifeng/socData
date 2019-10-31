package com.mchz.socData.customize;

import com.alibaba.fastjson.JSONArray;
import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * generateData
 * 2019/10/29 14:57
 * 数据规格
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class DataSpecification {

	//设备信息
	@NotNull
	private List<DeviceInfo> deviceInfos;
	//数据总数
	private int totalCount;
	//生成数据的开始时间
	private long startTime;
	//生成数据的结束时间
	private long endTime;

	private boolean validate(){
		boolean result = true;
		if(deviceInfos == null || deviceInfos.isEmpty()){
			result = false;
		}

		return result;
	}

	public String formdateData(){
		if(!validate()) throw new RuntimeException("参数不合法");
		JSONArray jsonArray = new JSONArray();
		Optional.ofNullable(deviceInfos).ifPresent(devices -> devices.forEach(deviceInfo -> {
			deviceInfo.formatData(startTime, endTime);
			String deviceData = deviceInfo.getJsonData();
			if(StringUtils.isNotBlank(deviceData)){
				jsonArray.addAll(JSONArray.parseArray(deviceData));
			}
		}));

		return jsonArray.toJSONString();
	}
}

