package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.ExamScoreDto;
import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.ExamScoreService;
import com.gdutelc.utils.JsoupUtils;
import com.gdutelc.utils.OkHttpUtils;
import com.gdutelc.utils.StringUtils;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.gdutelc.common.constant.RoleConstant.GRADUATE;
import static com.gdutelc.common.constant.RoleConstant.UNDER_GRADUATE;

/**
 * @author Ymri
 * @version 1.0
 * @since 2024/2/1 00:03
 * ExamScoreServiceImpl
 */
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
     * @return
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

        String content = null;
        try (Response response = this.okHttpUtils.postByFormUrl(okHttpClient, url, JsoupUtils.map2PostUrlCodeString(paramMap), UrlConstant.UNDER_REFER, cookies)) {
            assert response.body() != null;
            content = response.body().string();
            if (response.code() != 200 || StringUtils.isEmpty(content)) {
                throw new ServiceException("获取课表出现问题", response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject object = JSON.parseObject(content);
        JSONArray rows = object.getJSONArray("rows");
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
            result.get(term).add(examScoreDto);
        }
        return result;
    }

    /**
     * 研究生获取成绩
     *
     * @param okHttpClient okClient
     * @param cookies      cookie
     * @return
     */

    public Map<String, ArrayList<ExamScoreDto>> getGraduateScore(OkHttpClient okHttpClient, String cookies) {
        return null;
    }


}
