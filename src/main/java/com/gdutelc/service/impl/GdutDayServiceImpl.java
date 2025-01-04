package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.RoleConstant;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.*;
import com.gdutelc.domain.VO.LibQrVO;
import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.domain.query.ScheduleInfoQueryDto;
import com.gdutelc.framework.common.HttpStatus;
import com.gdutelc.framework.config.GdutConfig;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.GdutDayService;
import com.gdutelc.utils.*;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gdutelc.common.constant.RoleConstant.GRADUATE;
import static com.gdutelc.common.constant.RoleConstant.UNDER_GRADUATE;

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

    private static final HashMap<Integer, Integer> timeToCourseSection;

    @Resource
    private ScheduleInfoServiceImpl scheduleInfoService;
    private static final Pattern regex;

    @Resource
    private GdutConfig gdutConfig;

    //
    static {
        // 正则匹配学号
        regex = Pattern.compile("<option value='(\\d+)' selected>");
        timeToCourseSection = new HashMap<>();
        timeToCourseSection.put(830, 1);
        timeToCourseSection.put(915, 1);
        timeToCourseSection.put(920, 2);
        timeToCourseSection.put(1005, 2);
        timeToCourseSection.put(1025, 3);
        timeToCourseSection.put(1110, 3);
        timeToCourseSection.put(1115, 4);
        timeToCourseSection.put(1200, 4);
        timeToCourseSection.put(1350, 5);
        timeToCourseSection.put(1435, 5);
        timeToCourseSection.put(1440, 6);
        timeToCourseSection.put(1525, 6);
        timeToCourseSection.put(1530, 7);
        timeToCourseSection.put(1615, 7);
        timeToCourseSection.put(1630, 8);
        timeToCourseSection.put(1715, 8);
        timeToCourseSection.put(1720, 9);
        timeToCourseSection.put(1805, 9);
        timeToCourseSection.put(1830, 10);
        timeToCourseSection.put(1915, 10);
        timeToCourseSection.put(1920, 11);
        timeToCourseSection.put(2005, 11);
        timeToCourseSection.put(2010, 12);
        timeToCourseSection.put(2055, 12);

    }

    /**
     * 获得用户信息
     *
     * @param cookies cookies
     * @return 用户dto
     */
    @Override
    public UserInfoDto getUserInfo(String cookies, Integer userType) {
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        String url = null;
        if (userType == RoleConstant.GRADUATE) {
            url = UrlConstant.GRADUATE_USER_INFO;
        }
        Response response = okHttpUtils.get(okHttpClient, url, cookies);
        return null;
    }

    /**
     * 获得课表信息
     * 默认使用研究生的学期信息
     * 研究生的学期信息：20231、20232 分表表示两个学期
     * 本科生：202301、202302
     *
     * @param queryDto queryData
     * @return 课表 Dto
     */
    @Override
    public Map<String, ArrayList<ScheduleInfoDto>> getScheduleInfo(ScheduleInfoQueryDto queryDto) {
//        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        if (queryDto.getUserType().equals(UNDER_GRADUATE)) {
            // 学期转换，见注释
            Integer termId = (queryDto.getTermId() / 10) * 100 + queryDto.getTermId() % 10;
            return getUnderGraduateSchedule(okHttpClient, termId, queryDto.getCookies());
        } else if (queryDto.getUserType().equals(RoleConstant.GRADUATE)) {
            return getGraduateSchedule(okHttpClient, queryDto.getTermId(), queryDto.getCookies());
        }
        return null;
    }

    /**
     * 获取研究生课表
     *
     * @param okHttpClient
     * @param termId
     * @param cookies
     * @return
     */
    private Map<String, ArrayList<ScheduleInfoDto>> getGraduateSchedule(OkHttpClient okHttpClient, Integer termId, String cookies) {
        String url = UrlConstant.GRADUATE_KB;
        url = url.replace("20221", termId.toString());
        String content = null;
        try (Response response = okHttpUtils.postByFormUrlWithCookie(okHttpClient, url, JsoupUtils.map2PostUrlCodeString(new HashMap<>()), cookies)) {
            assert response.body() != null;
            content = response.body().string();
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException(" 网络请求异常，请重试！", HttpStatus.f5001);
        }

        return getGraduateScheduleDataClear(content);
    }

    /**
     * 研究生课表数据清洗
     *
     * @param content 请求返回的数据
     * @return Map
     */
    public Map<String, ArrayList<ScheduleInfoDto>> getGraduateScheduleDataClear(String content) {
        LinkedHashMap<String, ArrayList<ScheduleInfoDto>> map = new LinkedHashMap<>();
        for (int i = 1; i < 22; i++) {
            map.put(String.valueOf(i), new ArrayList<>());
        }
        try {
            JSONObject object = JSONObject.parseObject(content);
            JSONObject datas = object.getJSONObject("datas");
            JSONObject jsonObject = datas.getJSONObject("xspkjgcx");
            JSONArray rows = jsonObject.getJSONArray("rows");
            for (int i = 0; i < rows.size(); i++) {
                JSONObject jsonObject1 = rows.getJSONObject(i);
                ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();
                //课程名称
                scheduleInfoDto.setCourseName(jsonObject1.getString("KCMC"));
                //地点
                String classroom = jsonObject1.getString("JASMC").replace("(", "")
                        .replace(")", "").replace("专用课室", "")
                        .replace("（", "").replace("）", "").replace(" ", "");
                scheduleInfoDto.setCoursePlace(classroom);
                //老师
                scheduleInfoDto.setCourseTeacher(jsonObject1.getString("JSXM"));
                //星期
                scheduleInfoDto.setCourseDay(jsonObject1.getString("XQ"));
                // 课程描述
                scheduleInfoDto.setCourseDescription(jsonObject1.getString("BJMC"));
                //节次
                Integer startTime = jsonObject1.getInteger("KSSJ");
                Integer endTime = jsonObject1.getInteger("JSSJ");
                // 根据开始时间获得结束时间
                Integer i1 = GdutDayServiceImpl.timeToCourseSection.get(startTime);
                Integer i2 = GdutDayServiceImpl.timeToCourseSection.get(endTime);
                if (i1.compareTo(i2) == 0) {
                    scheduleInfoDto.setCourseSection(i1.toString());
                } else {
                    String cs = "";
                    for (int j = i1; j <= i2; j++) {
                        cs += j;
                        if (j < i2) {
                            cs += ",";
                        }
                    }
                    scheduleInfoDto.setCourseSection(cs);
                }
                //周次
                String zcmc = jsonObject1.getString("ZCMC");
                zcmc = zcmc.replaceAll(" ", "");
                String[] strings = zcmc.split(",");
                for (String string : strings) {
                    string = string.replace("周", "");
                    if (string.contains("-")) {
                        // 正常的 1-11周
                        String[] strings1 = string.split("-");
                        int startCd = Integer.parseInt(strings1[0]);
                        int endCd = Integer.parseInt(strings1[1]);
                        for (int j = startCd; j <= endCd; j++) {
                            ArrayList<ScheduleInfoDto> scheduleInfoDtos = map.get(String.valueOf(j));
                            ScheduleInfoDto scheduleInfoDto1 = new ScheduleInfoDto();
                            // 使用浅拷贝，原来的引用不会被修改
                            BeanUtils.copyProperties(scheduleInfoDto, scheduleInfoDto1);
                            scheduleInfoDto1.setCourseWeek(String.valueOf(j));
                            scheduleInfoDtos.add(scheduleInfoDto1);
                        }
                    } else {
                        // 只有单周情况
                        ArrayList<ScheduleInfoDto> scheduleInfoDtos = map.get(string);
                        ScheduleInfoDto scheduleInfoDto1 = new ScheduleInfoDto();
                        BeanUtils.copyProperties(scheduleInfoDto, scheduleInfoDto1);
                        scheduleInfoDto1.setCourseWeek(string);
                        scheduleInfoDtos.add(scheduleInfoDto1);
                    }
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException(" 网络请求异常，请重试！", HttpStatus.f5001);
        } catch (JSONException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException(" 网络请求异常，请重试！", HttpStatus.f5001);
        }
        return map;
    }

    /**
     * 本科生获取课表
     *
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
        String content = null;
        try (Response response = okHttpUtils.postByFormUrl(okHttpClient, UrlConstant.UNDER_CLAZZ,
                JsoupUtils.map2PostUrlCodeString(paramMap),
                "https://jxfw.gdut.edu.cn/", cookies)) {
            assert response.body() != null;
            content = response.body().string();
            if (response.code() != 200 || StringUtils.isEmpty(content)) {
                throw new ServiceException("请求课表异常，请重试！", HttpStatus.f006);
            }
        } catch (IOException e) {
            throw new ServiceException("IO异常！", HttpStatus.f5001);
        }
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
                for (int j = 0; j < charArray.length; j += 2) {
                    String str = charArray[j] + "";
                    str += charArray[j + 1];
                    cs += Integer.parseInt(str);
                    if (j < charArray.length - 2) {
                        cs += ",";
                    }
                }
                scheduleInfoDto.setCourseSection(cs);
                scheduleInfoDtos.add(scheduleInfoDto);
            }
            HashMap<String, ArrayList<ScheduleInfoDto>> ansMap = new HashMap<>();
            //假定最多21周
            for (int i = 1; i < 22; i++) {
                ansMap.put(String.valueOf(i), new ArrayList<>());
            }
            for (ScheduleInfoDto scheduleInfoDto : scheduleInfoDtos) {
                ansMap.get(String.valueOf(scheduleInfoDto.getCourseWeek())).add(scheduleInfoDto);
            }
            return ansMap;
        } catch (JSONException e) {
            throw new ServiceException("JSON处理异常！", HttpStatus.f5001);
        }

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
     * @param term
     * @return ArrayList
     */
    @Override
    public ArrayList<ExaminationDto> getExaminationInfo(String cookies, Integer userType, Integer term) {
        term = (term / 10) * 100 + term % 10;
        HashMap<String, String> map = new HashMap<>();
        map.put("xnxqdm", term + "");
        map.put("page", "1");
        map.put("rows", "200");
        map.put("sort", "zc,xq,jcdm2");
        map.put("order", "asc");
        String url = "https://jxfw.gdut.edu.cn/xsksap!getDataList.action";
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        Response response = okHttpUtils.postByFormUrl(okHttpClient, url, JsoupUtils.map2PostUrlCodeString(map), "https://jxfw.gdut.edu.cn/", cookies);
        assert response.body() != null;
        String content = null;
        try {
            content = response.body().string();
        } catch (IOException e) {
            throw new ServiceException("内部IO异常！", HttpStatus.f5001);
        }
        if (response.code() != 200 || StringUtils.isEmpty(content)) {
            throw new ServiceException("请求考试安排异常，请重试！", HttpStatus.f009);
        }
        JSONObject object = JSON.parseObject(content);
        JSONArray rows = object.getJSONArray("rows");
        HashMap<String, Integer> idMap = new HashMap<>();
        ArrayList<ExaminationDto> arrayList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            JSONObject jsonObject02 = rows.getJSONObject(i);

            if (idMap.get(jsonObject02.getString("kcbh")) == null) {
                idMap.put(jsonObject02.getString("kcbh"), i);
            }
            ExaminationDto examination = new ExaminationDto(jsonObject02.getString("kscdmc"), jsonObject02.getString("kslbmc"), idMap.get(jsonObject02.getString("kcbh")), jsonObject02.getString("kssj"), jsonObject02.getString("ksaplxmc"), jsonObject02.getString("ksrq"), jsonObject02.getString("xqmc"), jsonObject02.getString("kcmc"));
            arrayList.add(examination);
        }
        return arrayList;
    }

    @Override
    public VerCodeDto sendVerification(String jSessionId) {
//        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        Response response = null;
        if (StringUtils.isEmpty(jSessionId)) {
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis());
        } else {
            response = okHttpUtils.get(okHttpClient, "https://jxfw.gdut.edu.cn/yzm" + "?d=" + System.currentTimeMillis(), jSessionId);
        }
        if (response.code() != 200) {
            response.close();
            throw new ServiceException("请求验证码失败，请重试！", HttpStatus.f001);
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
            throw new ServiceException("请求验证码失败，请重试！", HttpStatus.f001);
        }
        return new VerCodeDto(base64, header);
    }


    @Override
    public String getTerm(BaseRequestDto baseRequestDto) {
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        if (baseRequestDto.getUserType() == GRADUATE) {
            return "" + scheduleInfoService.getGraduateTermId(baseRequestDto.getCookies(), okHttpClient);
        }
        try (Response response = okHttpUtils.get(okHttpClient, UrlConstant.UNDER_CLAZZ_TERM, baseRequestDto.getCookies(), UrlConstant.UNDER_REFER)) {
            String content = response.body().string();
            Matcher matcher = regex.matcher(content);
            if (!matcher.find()) {
                throw new ServiceException("身份信息过期，请重新登录！", HttpStatus.f007);
            }
            String termId = matcher.group(0).replace("<option value='", "").replace("' selected>", "");
            return termId.substring(0, termId.length() - 2) + termId.charAt(termId.length() - 1);
        } catch (ServiceException e) {
            throw new ServiceException("身份信息过期，请重新登录！", HttpStatus.f007);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException("内部异常！", HttpStatus.f5001);
        }

    }

    /**
     * 开学时间
     *
     * @return
     */
    @Override
    public String getAdmissionDate() {
        return gdutConfig.getAdmissionDate().toString();
    }

    /**
     * 修改开学时间，加密后
     *
     * @param date date
     */
    @Override
    public boolean changeAdmissionDate(String date) {
        //
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, ft);
        } catch (Exception e) {
            throw new ServiceException("Please enter the correct date！", HttpStatus.f010);
        }
        // 内部使用 懒得加锁
        gdutConfig.setAdmissionDate(date);
        LOGGER.info("更新开学时间：" + date);
        return true;
    }


}
