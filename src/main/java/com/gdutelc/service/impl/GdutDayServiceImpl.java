package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.beust.ah.A;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.gdutelc.common.constant.RoleConstant;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.*;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.GdutDayService;
import com.gdutelc.utils.*;
import jakarta.annotation.Resource;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:32
 * GdutDaServiceImpl
 */
@Service
public class GdutDayServiceImpl implements GdutDayService {

    @Resource
    private OkHttpUtils okHttpUtils;

    private final Logger LOGGER = LoggerFactory.getLogger(GdutDayServiceImpl.class);

    /**
     * 获得用户信息
     *
     * @param cookies cookies
     * @return 用户dto
     */
    @Override
    public UserInfoDto getUserInfo(String cookies, Integer userType) {
        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient(gdutDayCookieJar);
        String url = null;
        if (userType == RoleConstant.GRADUATE) {
            url = UrlConstant.GRADUATE_USER_INFO;
        }
        Response response = okHttpUtils.get(okHttpClient, url, cookies);

        return null;
    }

    /**
     * 获得课表信息
     *
     * @param cookies cookies
     * @return 课表 Dto
     */
    @Override
    public Map<String, ArrayList<ScheduleInfoDto>> getScheduleInfo(String cookies, Integer userType, Integer termId) {
        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient(gdutDayCookieJar);
        if (userType.equals(RoleConstant.UNDER_GRADUATE)) {
            return getUnderGraduateSchedule(okHttpClient,termId,cookies);
        } else if (userType.equals(RoleConstant.GRADUATE)) {
            return getGraduateSchedule(okHttpClient,termId,cookies);
        }
        return null;
    }

    /**
     * 获取研究生课表
     * @param okHttpClient
     * @param termId
     * @param cookies
     * @return
     */
    private Map<String, ArrayList<ScheduleInfoDto>> getGraduateSchedule(OkHttpClient okHttpClient, Integer termId, String cookies) {
        String url = "http://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/modules/xskcb/xspkjgcx.do?XNXQDM=20221&*order=<*order>";
        url = url.replace("20221", termId.toString());
        Response response = okHttpUtils.postByFormUrlWithCookie(okHttpClient,url,JsoupUtils.map2PostUrlCodeString(new HashMap<>()),cookies);
        //课程返回的数据结构
        //{
        //    "datas": {
        //        "xspkjgcx": {
        //            "totalSize": 19,
        //            "pageSize": 999,
        //            "rows": [
        //                {
        //                    "BY6": null,
        //                    "BY5": null,
        //                    "XS": 16,
        //                    "BY4": null,
        //                    "KSJCDM": 8,
        //                    "JASMC": "教2-425（专用课室）",
        //                    "BY3": null,
        //                    "WID": "405b2052-fe29-4f20-a484-7c8aab4b16e1",
        //                    "XQ": 2,
        //                    "BY2": null,
        //                    "BY1": null,
        //                    "KSSJ": 1630,
        //                    "SKFSDM_DISPLAY": "讲授",
        //                    "XH": null,
        //                    "ORDERFILTER": null,
        //                    "CZR": "00006869",
        //                    "BZ": null,
        //                    "KCMC": "新时代中国特色社会主义理论与实践",
        //                    "SKFSDM": "01",
        //                    "XM": null,
        //                    "ZCBH": "111111111111111100000000000000",
        //                    "XNXQDM": "20222",
        //                    "CZSJ": "2023-01-03 00:00:00",
        //                    "JSXM": "冯英",
        //                    "KBBZ": null,
        //                    "RZLBDM": null,
        //                    "BJDM": "43a7fed5095a45b389e578cffddf33f1",
        //                    "SFQZAP": null,
        //                    "BJMC": "新时代14",
        //                    "JSJCDM": 8,
        //                    "ZCMC": "1-16周",
        //                    "JSSJ": 1715,
        //                    "JCFADM": "01",
        //                    "QZAPYY": null,
        //                    "BY7": null,
        //                    "BY8": null,
        //                    "BY9": null,
        //                    "JASDM": "11020425",
        //                    "KCDM": "218029",
        //                    "BY10": null
        //                }]
        //                  }
        //          }
        //}
        HashMap<String, ArrayList<ScheduleInfoDto>> map = new HashMap<>();
        HashMap<Integer, Integer> timeToCourseSection = new HashMap<>();
        timeToCourseSection.put(830,1);
        timeToCourseSection.put(915,1);
        timeToCourseSection.put(920,2);
        timeToCourseSection.put(1005,2);
        timeToCourseSection.put(1025,3);
        timeToCourseSection.put(1110,3);
        timeToCourseSection.put(1115,4);
        timeToCourseSection.put(1200,4);
        timeToCourseSection.put(1350,5);
        timeToCourseSection.put(1435,5);
        timeToCourseSection.put(1440,6);
        timeToCourseSection.put(1525,6);
        timeToCourseSection.put(1530,7);
        timeToCourseSection.put(1615,7);
        timeToCourseSection.put(1630,8);
        timeToCourseSection.put(1715,8);
        timeToCourseSection.put(1720,9);
        timeToCourseSection.put(1805,9);
        timeToCourseSection.put(1830,10);
        timeToCourseSection.put(1915,10);
        timeToCourseSection.put(1920,11);
        timeToCourseSection.put(2005,11);
        timeToCourseSection.put(2010,12);
        timeToCourseSection.put(2055,12);
        for(int i = 1;i<22;i++){
            map.put(i+"",new ArrayList<>());
        }
        try {
            JSONObject object = JSONObject.parseObject(response.body().string());
            JSONObject datas = object.getJSONObject("datas");
            JSONObject jsonObject = datas.getJSONObject("xspkjgcx");
            JSONArray rows = jsonObject.getJSONArray("rows");
            for (int i = 0; i < rows.size(); i++) {
                JSONObject jsonObject1 = rows.getJSONObject(i);
                ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
                //课程名称
                scheduleInfoDto.setCourseName(jsonObject1.getString("KCMC"));
                //地点
                scheduleInfoDto.setCoursePlace(jsonObject1.getString("JASMC"));
                //老师
                scheduleInfoDto.setCourseTeacher(jsonObject1.getString("JSXM"));
                //星期
                scheduleInfoDto.setCourseDay(jsonObject1.getString("XQ"));
                //节次
                Integer startTime = jsonObject1.getInteger("KSSJ");
                Integer endTime = jsonObject1.getInteger("JSSJ");
                Integer i1 = timeToCourseSection.get(startTime);
                Integer i2 = timeToCourseSection.get(endTime);
                if(i1==i2){
                    scheduleInfoDto.setCourseSection(i1.toString());
                }else {
                    String cs = "";
                    for(int j = i1;j<=i2;j++){
                        cs += j;
                        if(j<i2){
                            cs+=",";
                        }
                    }
                    scheduleInfoDto.setCourseSection(cs);
                }
                //周次
                String zcmc = jsonObject1.getString("ZCMC");
                String[] strings = zcmc.split(",");
                for(String string:strings){
                    string = string.replace("周","");
                    String[] strings1 = string.split("-");
                    int startCd = Integer.parseInt(strings1[0]);
                    int endCd = Integer.parseInt(strings1[1]);
                    for(int j = startCd;j<=endCd;j++){
                        ArrayList<ScheduleInfoDto> scheduleInfoDtos = map.get(j + "");
                        ScheduleInfoDto scheduleInfoDto1 = new ScheduleInfoDto();
                        BeanUtils.copyProperties(scheduleInfoDto,scheduleInfoDto1);
                        scheduleInfoDto1.setCourseWeek(j+"");
                        scheduleInfoDtos.add(scheduleInfoDto1);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 本科生获取课表
     * @param okHttpClient
     * @param termId
     * @param cookies
     * @return
     */
    private Map<String, ArrayList<ScheduleInfoDto>> getUnderGraduateSchedule(OkHttpClient okHttpClient, Integer termId, String cookies) {
        //本科生
        HashMap<String, String> paramMap = new HashMap<>(10);
        paramMap.put("xnxqdm", termId + "");
        paramMap.put("zc", "");
        paramMap.put("page", "1");
        paramMap.put("rows", "300");
        paramMap.put("sort", "kxh");
        paramMap.put("order", "asc");
        Response response = okHttpUtils.postByFormUrl(okHttpClient, UrlConstant.UNDER_CLAZZ,
                JsoupUtils.map2PostUrlCodeString(paramMap),
                "https://jxfw.gdut.edu.cn/", cookies);
        String content = null;
        try {
            content = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (response.code() != 200 || StringUtils.isEmpty(content)) {
            throw new ServiceException("获取课表出现问题", response.code());
        }
        //返回数据结构（节选）
        //{
        //  "total": 175,
        //       "rows": [
        //        {
        //            "dgksdm": "2740423",
        //            "jxbmc": "电子信息类22(7),电子信息类22(8)",
        //            "pkrs": "90",
        //            "kcmc": "模拟电子技术",
        //            "teaxms": "潘晴",
        //            "xq": "3",
        //            "jcdm": "0102",
        //            "jxcdmc": "教3-307",
        //            "zc": "1",
        //            "kxh": "1",
        //            "jxhjmc": "理论教学",
        //            "flfzmc": "",
        //            "sknrjj": "毕业要求与课程目标；第1章导言；3.1半导体基础知识；3.2.1半导体二极管的几种常见结构",
        //            "pkrq": "2023-08-30",
        //            "rownum_": "1"
        //        }]
        //}
        //统一数据结构，转为与研究生相同的数据结构
        //"code": 4000,
        //    "data": {
        //        "1": [
        //            {
        //                  课程地点
        //                "ad": "教2-425",
        //                  课程名称
        //                "cn": "工程硕士英语",
        //课程老师
        //                "tn": "杨燕荣",
        //                  星期几
        //                "wd": 1,
        //                  周次
        //                "w": "1",
        //                  课程描述
        //                "cc": "工程硕士英语5",
        //                  节次
        //                "cs": "6,7",
        //
        //                "key": "工程硕士英语1-12周1",
        //                "xs": 12,
        //                "id": 2
        //            },
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(content);
            JSONArray jsonArray = JSON.parseArray(jsonObject.getString("rows"));
            ArrayList<ScheduleInfoDto> scheduleInfoDtos = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject clazz = jsonArray.getJSONObject(i);
                ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
                scheduleInfoDto.setCoursePlace(clazz.getString("jxcdmc"));
                scheduleInfoDto.setCourseName(clazz.getString("kcmc"));
                scheduleInfoDto.setCourseTeacher(clazz.getString("teaxms"));
                scheduleInfoDto.setCourseDay(clazz.getString("xq"));
                scheduleInfoDto.setCourseWeek(clazz.getString("zc"));
                scheduleInfoDto.setCourseDescription(clazz.getString("sknrjj"));
                String jcdm = clazz.getString("jcdm");
                char[] charArray = jcdm.toCharArray();
                String cs = "";
                for(int j = 0;j<charArray.length;j+=2){
                    String str = charArray[j]+"";
                    str+=charArray[j+1];
                    cs+=Integer.parseInt(str);
                    if(j<charArray.length-2){
                        cs+=",";
                    }
                }
                scheduleInfoDto.setCourseSection(cs);
                scheduleInfoDtos.add(scheduleInfoDto);
            }
            HashMap<String, ArrayList<ScheduleInfoDto>> ansMap = new HashMap<>();
            //假定最多21周
            for (int i = 1; i < 22; i++) {
                ansMap.put("" + i, new ArrayList<ScheduleInfoDto>());
            }
            for (ScheduleInfoDto scheduleInfoDto : scheduleInfoDtos) {
                ansMap.get(scheduleInfoDto.getCourseWeek() + "").add(scheduleInfoDto);
            }
            return ansMap;
        }catch (JSONException e){
            throw new ServiceException("出现未知问题，请重新登录，若再出现，请联系开发人员",400);
        }

    }

    /**
     * 获得考试信息
     *
     * @param cookies cookies
     */
    @Override
    public ArrayList<ExamScoreDto> getExamScore(String cookies, Integer userType) {
        return null;
    }

    /**
     * 获得图书馆二维码
     *
     * @param libQrVO 学号，宽度，高度
     * @return 返回图片的Base64编码
     */
    @Override
    public String getLibQr(LibQrVO libQrVO) {
        return LiUtils.makeQRCode(libQrVO.getStuId(), libQrVO.getWidthStr(), libQrVO.getHeightStr());
    }

    /**
     * 获得考试安排
     *
     * @param cookies cookies
     * @return ArrayList
     */
    @Override
    public ArrayList<ExaminationDto> getExaminationInfo(String cookies, Integer userType) {
        return null;
    }

    @Override
    public VerCodeDto sendVerification(String jSessionId) {
        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient(gdutDayCookieJar);
        Response response = null;
        if (StringUtils.isEmpty(jSessionId)) {
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis());
        } else {
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis(), jSessionId);
        }
        if (response.code() != 200) {
            response.close();
            throw new ServiceException("获取验证码失败", response.code());
        }
        String header = response.header("Set-Cookie");
        if (StringUtils.isEmpty(header)) {
            header = jSessionId;
        }
        String base64 = null;
        try {
            byte[] bytes = response.body().bytes();
            response.close();
            base64 = LiUtils.makeBase64(bytes);
        } catch (IOException e) {
            response.close();
            throw new RuntimeException(e);
        }
        return new VerCodeDto(base64, header);
    }

}
