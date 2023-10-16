package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.ah.A;
import com.gdutelc.common.constant.RoleConstant;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.*;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.framework.domain.R;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.GdutDayService;
import com.gdutelc.utils.*;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    /**
     * 获得用户信息
     *
     * @param cookies cookies
     * @return 用户dto
     */
    @Override
    public UserInfoDto getUserInfo(String cookies,Integer userType) {
        return null;
    }

    /**
     * 获得课表信息
     *
     * @param cookies cookies
     * @return 课表 Dto
     */
    @Override
    public Map<String,ArrayList<ScheduleInfoDto>> getScheduleInfo(String cookies, Integer userType, Integer termId) {
        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient(gdutDayCookieJar);
        if(userType.equals(RoleConstant.UNDER_GRADUATE)) {
            //本科生

            HashMap<String, String> paramMap = new HashMap<>(10);
            paramMap.put("xnxqdm", termId+"");
            paramMap.put("zc", "");
            paramMap.put("page", "1");
            paramMap.put("rows", "300");
            paramMap.put("sort", "kxh");
            paramMap.put("order", "asc");
            Response response = okHttpUtils.postByFormUrl(okHttpClient, UrlConstant.UNDER_CLAZZ, JsoupUtils.map2PostUrlCodeString(paramMap), "https://jxfw.gdut.edu.cn/",cookies);
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
            JSONObject jsonObject = JSON.parseObject(content);
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
                jcdm.replaceAll("0",",");
                String substring = jcdm.substring(0, jcdm.length() - 2);
                scheduleInfoDto.setCourseSection(substring);
                scheduleInfoDtos.add(scheduleInfoDto);
            }
            HashMap<String, ArrayList<ScheduleInfoDto>> ansMap = new HashMap<>();
            for(int i = 1;i<22;i++){
                ansMap.put(""+i,new ArrayList<ScheduleInfoDto>());
            }
            for (ScheduleInfoDto scheduleInfoDto : scheduleInfoDtos) {
                ansMap.get(scheduleInfoDto.getCourseWeek()+"").add(scheduleInfoDto);
            }
            return ansMap;
        }else if(userType.equals(RoleConstant.GRADUATE)){
            Response response1 = okHttpUtils.get(okHttpClient, "http://ehall.gdut.edu.cn/appShow?appId=4979568947762216", cookies);
            try {
                String string = response1.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //研究生
            Response response = okHttpUtils.get(okHttpClient, "http://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/modules/xskcb/xspkjgcx.do" + "?XNXQDM=" + termId + "&*order=-ZCBH");
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
            try {
                JSONObject object = JSONObject.parseObject(response.body().string());
                JSONObject datas = object.getJSONObject("datas");
                JSONObject jsonObject = datas.getJSONObject("xspkjgcx");
                JSONArray rows = jsonObject.getJSONArray("rows");
                for (int i = 0; i < rows.size(); i++) {
                    JSONObject jsonObject1 = rows.getJSONObject(i);
                    ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
                    scheduleInfoDto.setCourseName(jsonObject1.getString("KCMC"));
                    scheduleInfoDto.setCoursePlace("JASMC");
                    scheduleInfoDto.setCourseTeacher("JSXM");
                    scheduleInfoDto.setCourseWeek("ZCMC");
                    scheduleInfoDto.setCourseDay("XQ");
//                    scheduleInfoDto.setCourseSection();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
        return null;
    }

    /**
     * 获得考试信息
     *
     * @param cookies cookies
     */
    @Override
    public ArrayList<ExamScoreDto> getExamScore(String cookies,Integer userType) {
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
        return LiUtils.makeQRCode(libQrVO.getStuId(),libQrVO.getWidthStr(),libQrVO.getHeightStr());
    }

    /**
     * 获得考试安排
     *
     * @param cookies cookies
     * @return ArrayList
     */
    @Override
    public ArrayList<ExaminationDto> getExaminationInfo(String cookies,Integer userType) {
        return null;
    }

    @Override
    public VerCodeDto sendVerification(String jSessionId) {
        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient(gdutDayCookieJar);
        Response response = null;
        if(StringUtils.isEmpty(jSessionId)){
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis());
        }else {
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis(),jSessionId);
        }
        if(response.code()!=200){
            throw new ServiceException("获取验证码失败", response.code());
        }
        String header = response.header("Set-Cookie");
        if(StringUtils.isEmpty(header)){
            header = jSessionId;
        }
        String base64 = null;
        try {
            byte[] bytes = response.body().bytes();
            base64 = LiUtils.makeBase64(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new VerCodeDto(base64,header);
    }
}
