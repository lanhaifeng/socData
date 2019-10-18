package com.mchz.socData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mchz.socData.util.CreateFileUtil;
import com.mchz.socData.util.GenerateDataUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * generateData
 * 2019/10/17 9:29
 * main函数入口类
 *
 * @author lanhaifeng
 * @since
 **/
public class SocData {

    public static void main(String[] args) {
        try {
            for (int i = 0; i < args.length; ++i) {
                System.out.println(args[i]);
            }
            long beginTime = Long.parseLong(args[0]);//1567267200000L
            long endTime = Long.parseLong(args[1]); //1569859200000L
            int deviceSize = Integer.parseInt(args[2]); //10
            int size = Integer.parseInt(args[3]); //5
            String fileUrl = args[4]; //"/fileStorage/download/json"
            String fileName = args[5]; //data

            SocData socData = new SocData();
            String json = socData.generateJson(beginTime, endTime, deviceSize, size).toJSONString();
            System.out.println(json);

            CreateFileUtil.createJsonFile(json, fileUrl, fileName);
        } catch (Exception e) {
            System.out.println("生成json数据文件错误：" + ExceptionUtils.getFullStackTrace(e));
        }
    }


    public JSON generateJson(long beginTime, long endTime, int deviceSize, int size) {
        if (endTime <= beginTime) {
            System.err.println("endTime <=  beginTime endTime:" + endTime+" beginTime:"+beginTime);
        }

        try {
            JSONObject json = this.getFile();
            List<String> dvUids = getDvuids(deviceSize);
            JSONArray array = new JSONArray();
            for (int i = 0; i < size; i++) {
                JSONObject back = generateBaseData(json, beginTime, endTime, dvUids);
                generateExtendData(back, json);
                array.add(back);
            }
            return array;
        } catch (IOException e) {
            System.out.println("生成json数据错误：" + ExceptionUtils.getFullStackTrace(e));
        }
        return null;
    }

    private static List<String> getDvuids(final int size) {
        List<String> dvUids = new ArrayList<String>() {{
            for (int i = 0; i < size; i++) {
                add(UUID.randomUUID().toString());
            }
        }};
        return dvUids;
    }

    private final static List<String> actionNames = new ArrayList<String>() {
        {
            add("delete");
            add("write");
            add("move");
            add("invoke");
            add("run");
        }
    };

    private JSONObject generateBaseData(JSONObject json, long beginTime, long endTime, List<String> dvUids) {
        JSONObject back = new JSONObject();
        JSONObject baseDatajson = json.getJSONObject("baseData");
        //随机获取风险类型
        back.put("type", GenerateDataUtil.generateType());
        //随机获取发生设备UID
        back.put("sourceDvuid", dvUids.get(new Random().nextInt(dvUids.size())));
        //发生时间初始化
        long createTime = beginTime + (((long) (new Random().nextDouble() * (endTime - beginTime))));
        back.put("createTime", createTime);
        return back;

    }

    private JSONObject generateExtendData(JSONObject targetJson, JSONObject json) {
        JSONObject extendDatajson = json.getJSONObject("extendData");
        targetJson.put("appname", RandomStringUtils.randomAlphanumeric(10));
        targetJson.put("ip_address", GenerateDataUtil.generateIp());
        targetJson.put("content", RandomStringUtils.randomAlphanumeric(10));
        targetJson.put("dbuser", RandomStringUtils.randomAlphanumeric(5));
        targetJson.put("level", GenerateDataUtil.generateLevel());
        targetJson.put("sourceDvName", RandomStringUtils.randomAlphanumeric(10));
        targetJson.put("sourceDvPort", new Random().nextInt(65534 - 1025) + 1025);
        targetJson.put("source", RandomStringUtils.randomAlphanumeric(10));
        targetJson.put("target", RandomStringUtils.randomAlphanumeric(10));
        targetJson.put("sqlid", RandomStringUtils.randomAlphanumeric(10));
        targetJson.put("sqltextFormate", RandomStringUtils.randomAlphanumeric(30));

        JSONObject detail = new JSONObject();
        JSONObject deviceInfo = new JSONObject();
        deviceInfo.put("deivceMac", RandomStringUtils.randomAlphanumeric(10));
        deviceInfo.put("deviceName", RandomStringUtils.randomAlphanumeric(20));
        deviceInfo.put("deviceOs", RandomStringUtils.randomAlphanumeric(5));
        deviceInfo.put("internalIp", GenerateDataUtil.generateIp());
        deviceInfo.put("externalIp", GenerateDataUtil.generateIp());
        detail.put("deviceInfo", deviceInfo);

        JSONObject riskInfo = new JSONObject();

        riskInfo.put("actionName", actionNames.get(new Random().nextInt(actionNames.size())));
        riskInfo.put("processName", RandomStringUtils.randomAlphanumeric(20));
        detail.put("riskInfo", riskInfo);

        targetJson.put("detail", detail);
        return targetJson;
    }



    public JSONObject getFile() throws IOException {

        //读取文件
        String input =  readJsonDataByInputStream();

        if(StringUtils.isBlank(input)){
           return null;
        }
        //将读取的数据转换为JSONObject
        JSONObject jsonObject = JSONObject.parseObject(input);

        return jsonObject;

    }

    /**
     * 读取json文件并且转换成字符串
     *
     * @return
     * @throws IOException
     */
    public  String readJsonDataByInputStream() throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        try {

            //返回读取指定资源的输入流

            InputStream is=this.getClass().getResourceAsStream("/dataTemplate.json");
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                strbuffer.append(str);  //new String(str,"UTF-8")
            }
            br.close();
        } catch (IOException e) {
            System.out.println("读取json模板文件错误：" + ExceptionUtils.getFullStackTrace(e));
        }
        return strbuffer.toString();
    }
}
