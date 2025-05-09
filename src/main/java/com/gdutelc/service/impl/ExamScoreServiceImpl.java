package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.ExamScoreDto;
import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.framework.common.HttpStatus;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.ExamScoreService;
import com.gdutelc.utils.JsoupUtils;
import com.gdutelc.utils.OkHttpUtils;
import com.gdutelc.utils.StringUtils;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gdutelc.common.constant.RoleConstant.GRADUATE;
import static com.gdutelc.common.constant.RoleConstant.UNDER_GRADUATE;

/**
 * @author Ymri
 * @version 1.0
 * @since 2024/2/1 00:03
 * ExamScoreServiceImpl
 */
@Service
public class ExamScoreServiceImpl implements ExamScoreService {

    @Resource
    private OkHttpUtils okHttpUtils;

    /**
     * 查询成绩
     *
     * @param baseRequestDto queryBody
     * @return map
     */
    @Override
    public Map<String, ArrayList<ExamScoreDto>> getExamScore(BaseRequestDto baseRequestDto) {
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        if (baseRequestDto.getUserType().equals(UNDER_GRADUATE)) {
            return getUnderGraduateScore(okHttpClient, baseRequestDto.getCookies());
        } else if (baseRequestDto.getUserType().equals(GRADUATE)) {
            return getGraduateScore(okHttpClient, baseRequestDto.getCookies());
        }
        return null;
    }


    /**
     * 本科生获取成绩
     *
     * @param okHttpClient okClient
     * @param cookies      cookie
     * @return map
     */
    public Map<String, ArrayList<ExamScoreDto>> getUnderGraduateScore(OkHttpClient okHttpClient, String cookies) {
        String url = UrlConstant.UNDER_EXAME_SCORE;
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("xnxqdm", "");
        paramMap.put("jhlxdm", "");
        paramMap.put("sort", "xnxqdm");
        paramMap.put("page", "1");
        paramMap.put("rows", "200");
        paramMap.put("order", "asc");
        JSONArray rows = getRows(okHttpClient,url,paramMap,cookies);
        HashMap<String, ArrayList<ExamScoreDto>> result = new HashMap<>();
        for (int i = 0; i < rows.size(); i++) {
            JSONObject jsonObject = rows.getJSONObject(i);
            String term = jsonObject.getString("xnxqmc");
            if (!result.containsKey(term)) {
                // 存在中文汉字...
                result.put(term, new ArrayList<>());
            }
            ExamScoreDto examScoreDto = new ExamScoreDto();
            examScoreDto.setTerm(term);
            examScoreDto.setGpa(jsonObject.getString("cjjd"));
            examScoreDto.setResult(jsonObject.getString("zcj"));
            examScoreDto.setCredit(jsonObject.getString("xf"));
            examScoreDto.setType(jsonObject.getString("kcdlmc"));
            examScoreDto.setCourseType(jsonObject.getString("kcflmc"));
            examScoreDto.setOption(jsonObject.getString("xdfsmc"));
            examScoreDto.setCourseName(jsonObject.getString("kcmc"));
            if(examScoreDto.getCourseName().equals("劳动教育")){
                //20240814 修复劳动教育成绩显示问题，问题原因xnxqdm为空时，劳动教育不显示成绩，是教务处的问题
                //但我们还是抹平教务处的问题。。。
                paramMap.put("xnxqdm", "202302");
                JSONArray rows1 = getRows(okHttpClient, url, paramMap, cookies);
                for(int j = 0;j<rows1.size();j++){
                    JSONObject object = rows1.getJSONObject(j);
                    examScoreDto.setGpa(object.getString("cjjd"));
                    examScoreDto.setResult(object.getString("zcj"));
                }
            }
            result.get(term).add(examScoreDto);
        }
        return result;
    }

    public JSONArray getRows(OkHttpClient okHttpClient,String url,Map<String,String> paramMap,String cookies){
        String content = null;
        try (Response response = this.okHttpUtils.postByFormUrl(okHttpClient, url, JsoupUtils.map2PostUrlCodeString(paramMap), UrlConstant.UNDER_REFER, cookies)) {
            assert response.body() != null;
            content = response.body().string();
            if (response.code() != 200 || StringUtils.isEmpty(content)) {
                throw new ServiceException("请求成绩异常，请重试！", HttpStatus.f008);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject object;
        try {
            object = JSON.parseObject(content);
        } catch (JSONException e) {
            throw new ServiceException("身份信息过期，请重新登录！", HttpStatus.f007);
        }
        JSONArray rows = object.getJSONArray("rows");
        return rows;
    }

    /**
     * 研究生获取成绩
     *
     * @param okHttpClient okClient
     * @param cookies      cookie
     * @return map
     */

    public Map<String, ArrayList<ExamScoreDto>> getGraduateScore(OkHttpClient okHttpClient, String cookies) {
        String content = null;
        JSONObject object = null;
        try (Response response = okHttpUtils.getAddCookie(okHttpClient, UrlConstant.GRADUATE_EXAM, cookies)) {
            assert response.body() != null;
            content = response.body().string();
        } catch (IOException e) {
            throw new ServiceException("网络请求异常，请重试！", HttpStatus.f5001);
        }
        try {
            object = JSONObject.parseObject(content);
        } catch (Exception e) {
            throw new ServiceException("身份信息过期，请重新登录！", HttpStatus.BAD_REQUEST);
        }
        JSONObject datas = object.getJSONObject("datas");
        JSONObject jsonObject = datas.getJSONObject("xscjcx");
        JSONArray rows = jsonObject.getJSONArray("rows");
        List<ExamScoreDto> arrayList = JSONObject.parseArray(rows.toString(), ExamScoreDto.class);
        // stream 流过滤Result字段为空的
        arrayList.removeIf(e -> StringUtils.isEmpty(e.getResult()));
        // 筛选过滤 兼容本科生写法..
        Map<String, ArrayList<ExamScoreDto>> retMap = new HashMap<>();
        arrayList.forEach(e -> {
            e.setGpa(String.valueOf((Double.parseDouble(e.getResult()) - 50.0) / 10));
            if (retMap.containsKey(e.getTerm())) {
                retMap.get(e.getTerm()).add(e);
            } else {
                ArrayList<ExamScoreDto> temp = new ArrayList<>();
                temp.add(e);
                retMap.put(e.getTerm(), temp);
            }
        });
        return retMap;
    }


}
