package com.mchz.socData;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mchz.socData.customize.*;
import com.mchz.socData.util.JsonFormatTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * generateData
 * 2019/10/29 14:45
 * 生成数据
 *
 * @author lanhaifeng
 * @since
 **/
public class GenerateCustomizeData {

	public static void main(String[] args) {
		long startTime = 0l;
		long endTime = 0l;
		/*startTime = 1572314254000l;
		endTime = 1572364654000l;*/
		List<DeviceInfo> deviceInfos = prepareDeviceInfos();
		DataSpecification dataSpecification = DataSpecification.builder().deviceInfos(deviceInfos).startTime(startTime).endTime(endTime).build();
		System.out.println(dataSpecification.formdateData());
	}

	private static JSONObject prepareDataTemplate(){
		String riskDataTemplate = JsonFormatTool.readJsonFile("/dataTemplateExample.json");
		return JSONObject.parseObject(riskDataTemplate);
	}

	private static List<List<FieldDesc>> prepareFieldDescs(){
		String fieldsData = JsonFormatTool.readJsonFile("/fields.json");
		JSONArray fieldArray = JSONArray.parseArray(fieldsData);
		List<List<FieldDesc>> fiels = new ArrayList<>();
		fieldArray.forEach(deviceFields->{
			JSONArray fields = (JSONArray)deviceFields;
			List<FieldDesc> fieldDescs = new ArrayList<>();
			fields.forEach(field->{
				JSONObject fieldJson = (JSONObject)field;
				String name = fieldJson.getString("name");
				String formatDescJson = fieldJson.getString("formatDescs");
				FieldDesc fieldDesc = FieldDesc.builder().name(name).formatDescs(prepareFormateDescs(formatDescJson)).build();
				fieldDescs.add(fieldDesc);
			});

			fiels.add(fieldDescs);
		});

		return fiels;
	}

	private static List<FormatDesc> prepareFormateDescs(String source){
		JSONArray formateDescJsonArray = JSONArray.parseArray(source);
		List<FormatDesc> formateDescs = new ArrayList<>();
		formateDescJsonArray.forEach(formateDescJson->{
			JSONObject jsonObject = (JSONObject)formateDescJson;
			FormatDesc formateDesc = FormatDesc.builder()
					.defaultValue(jsonObject.getString("defaultValue"))
					.formatCount(jsonObject.getInteger("formatCount"))
					.generateDataUtilFun(jsonObject.getString("generateDataUtilFun"))
					.build();
			formateDescs.add(formateDesc);
		});

		return formateDescs;
	}

	private static List<DeviceInfo> prepareDeviceInfos(){
		JSONObject riskDataTemplate = prepareDataTemplate();

		String jsonDevices = JsonFormatTool.readJsonFile("/deviceInfos.json");
		JSONArray devices = JSONArray.parseArray(jsonDevices);

		List<DeviceInfo> deviceInfos = new ArrayList<>();
		Optional.ofNullable(devices).ifPresent(datas->datas.forEach(data->{
			JSONObject device = (JSONObject)data;
			deviceInfos.add(DeviceInfo.builder()
					.name(device.getString("name"))
					.uid(device.getString("uid"))
					.deviceType(DeviceType.getDeviceType(device.getString("type")))
					.count(device.getInteger("count"))
					.dataTemplate(riskDataTemplate)
					.startTime(device.getLong("startTime"))
					.endTime(device.getLong("endTime"))
					.build().init());
		}));

		List<List<FieldDesc>> fields = prepareFieldDescs();
		for (int i = 0; i < deviceInfos.size(); i++) {
			deviceInfos.get(i).setFieldDescs(fields.get(i));
		}

		return deviceInfos;
	}

}
