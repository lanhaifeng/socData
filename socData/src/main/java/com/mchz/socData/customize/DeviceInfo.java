package com.mchz.socData.customize;

import com.alibaba.fastjson.JSONObject;
import com.mchz.socData.util.GenerateDataUtil;
import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * generateData
 * 2019/10/29 15:30
 * 设备信息
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class DeviceInfo {

	//设备名
	private String name;
	//设备uid
	private String uid;
	//设备类型
	private DeviceType deviceType;
	//数据字段描述
	@NotNull
	private List<FieldDesc> fieldDescs;
	//数据模板
	@NotNull
	private JSONObject dataTemplate;
	//数据数量
	private int count;
	//数据开始时间
	private long startTime;
	//数据结束时间
	private long endTime;

	//生成的数据
	private StringBuilder jsonData;

	public void setFieldDescs(List<FieldDesc> fieldDescs) {
		Optional.ofNullable(fieldDescs).ifPresent(fields->this.fieldDescs=fields);
	}

	public List<FieldDesc> addFieldDesc(FieldDesc fieldDesc){
		Optional.ofNullable(fieldDesc).ifPresent(field->fieldDescs.add(field));

		return fieldDescs;
	}

	public String getJsonData() {
		return jsonData == null ? "" : jsonData.toString();
	}

	public DeviceInfo init(){
		if(jsonData == null){
			jsonData = new StringBuilder();
		}
		if(fieldDescs == null){
			fieldDescs = new ArrayList<>();
		}
		return this;
	}

	private boolean validateKeyExist(JSONObject dataTemplate, String key){
		if(dataTemplate == null || StringUtils.isBlank(key)) return false;
		if(!key.contains(".")) {
			return dataTemplate.containsKey(key);
		}else {
			return validateKeyExist(dataTemplate.getJSONObject(key.substring(0, key.indexOf("."))), key.substring(key.indexOf(".") + 1, key.length()));
		}
	}

	public boolean validate(){
		boolean result = true;
		if(dataTemplate == null || StringUtils.isBlank(dataTemplate.toJSONString()) || StringUtils.isBlank(uid)
				|| deviceType == null || fieldDescs == null || jsonData == null){
			result = false;
		}
		for (FieldDesc fieldDesc : fieldDescs) {
			if(fieldDesc.getFormatDescs() == null){
				return false;
			}
			result = validateKeyExist(dataTemplate, fieldDesc.getName());
			if(!result){
				return false;
			}

			for (FormatDesc formateDesc : fieldDesc.getFormatDescs()) {
				if(StringUtils.isBlank(formateDesc.getGenerateDataUtilFun()) && formateDesc.getDefaultValue() == null && formateDesc.getFormatCount() > 0){
					return false;
				}
			}
		}

		return result;
	}

	private void formatTime(long startTime, long endTime){
		if(startTime > 0 && endTime > 0 && startTime <= endTime){
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}

	public void formatData(long startTime, long endTime) {
		if (!validate()) throw new RuntimeException("校验失败");
		if (count < 1) return;
		formatTime(startTime, endTime);
		jsonData.append("[");
		for (int i = 0; i < count; i++) {
			JSONObject template = JSONObject.parseObject(dataTemplate.toJSONString());
			formatBaseData(template);
			Optional.ofNullable(fieldDescs).ifPresent(fields -> fields.forEach(field -> {
				Object value = template.get(field.getName());
				Object formatValue = field.formate();
				if(formatValue != null){
					value = formatValue;
				}
				updateJson(template, field.getName(), value);
			}));
			jsonData.append(template.toJSONString()).append(",");
		}
		String result = jsonData.toString();
		result = result.substring(0, result.length() - 1) + "]";
		jsonData.setLength(0);
		jsonData.append(result);
	}

	private void formatBaseData(JSONObject template){
		long time = GenerateDataUtil.generateTime(startTime, endTime);
		template.put("createTime", time);
		template.put("sourceDvuid", uid);
	}

	private void updateJson(JSONObject template, String key, Object value){
		if(StringUtils.isBlank(key) || Objects.isNull(value) || Objects.isNull(template)) return;
		if(!key.contains(".")) template.put(key, value);
		String[] keys = key.split("\\.");
		Object result = null;
		for (int i = 0; i < keys.length; i++) {
			result = getValueFromJson(template, keys[i]);
			if(i < keys.length - 1){
				if(Objects.nonNull(result) && result instanceof JSONObject){
					template = (JSONObject)result;
				}else {
					throw new RuntimeException("更新json失败");
				}
				if(i == keys.length - 2){
					template.put(keys[i + 1], value);
				}
			}
		}
	}

	private Object getValueFromJson(JSONObject template, String key){
		if(StringUtils.isBlank(key) || Objects.isNull(template)) return null;
		return template.get(key);
	}

}
