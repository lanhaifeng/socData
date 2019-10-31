package com.mchz.socData.customize;

/**
 * generateData
 * 2019/10/29 14:58
 * 设备类型
 *
 * @author lanhaifeng
 * @since
 **/
public enum DeviceType {
	NOAH,CAPAA,AUDIT,SOC;

	public static DeviceType getDeviceType(String deviceType){
		for (DeviceType type : values()) {
			if(type.toString().equalsIgnoreCase(deviceType)){
				return type;
			}
		}
		return null;
	}
}
