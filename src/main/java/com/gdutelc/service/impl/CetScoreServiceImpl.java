package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.CetScoreDto;
import com.gdutelc.domain.query.BaseRequestDto;
import com.gdutelc.framework.common.HttpStatus;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.CetScoreService;
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

import static com.gdutelc.common.constant.RoleConstant.UNDER_GRADUATE;

/**
 * @author xb2555
 * @version 1.0
 * @since 2026/4/15
 * CetScoreServiceImpl 英语考级成绩服务
 */
@Service
public class CetScoreServiceImpl implements CetScoreService {

    @Resource
    private OkHttpUtils okHttpUtils;

    @Override
    public List<CetScoreDto> getCetScore(BaseRequestDto baseRequestDto) {
        if (!UNDER_GRADUATE.equals(baseRequestDto.getUserType())) {
            throw new ServiceException("该接口仅支持本科生账号！", HttpStatus.BAD_REQUEST);
        }
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("page", "1");
        paramMap.put("rows", "100");
        paramMap.put("sort", "xnxqdm");
        paramMap.put("order", "asc");

        JSONArray rows = getRows(okHttpClient, paramMap, baseRequestDto.getCookies());
        List<CetScoreDto> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            JSONObject jsonObject = rows.getJSONObject(i);
            CetScoreDto dto = new CetScoreDto();
            dto.setTerm(jsonObject.getString("xnxqmc"));
            dto.setCetLevel(jsonObject.getString("kjkcmc"));
            dto.setTotalScore(jsonObject.getString("zcj"));
            dto.setTicketNo(jsonObject.getString("zkzh"));
            dto.setPart1Score(jsonObject.getString("xm1cj"));
            dto.setPart2Score(jsonObject.getString("xm2cj"));
            dto.setPart3Score(jsonObject.getString("xm3cj"));
            result.add(dto);
        }
        return result;
    }

    private JSONArray getRows(OkHttpClient okHttpClient, Map<String, String> paramMap, String cookies) {
        String content;
        try (Response response = okHttpUtils.postByFormUrl(
                okHttpClient,
                UrlConstant.UNDER_CET_SCORE,
                JsoupUtils.map2PostUrlCodeString(paramMap),
                UrlConstant.UNDER_CET_REFER,
                cookies)) {
            assert response.body() != null;
            content = response.body().string();
            if (response.code() != 200 || StringUtils.isEmpty(content)) {
                throw new ServiceException("请求英语考级成绩异常，请重试！", HttpStatus.f008);
            }
        } catch (IOException e) {
            throw new ServiceException("网络请求异常，请重试！", HttpStatus.f5001);
        }

        if (isPermissionDeniedPage(content)) {
            throw new ServiceException("你没有英语考级成绩查询权限！", HttpStatus.FORBIDDEN);
        }

        JSONObject object;
        try {
            object = JSON.parseObject(content);
        } catch (JSONException e) {
            throw new ServiceException("身份信息过期，请重新登录！", HttpStatus.f007);
        }
        JSONArray rows = object.getJSONArray("rows");
        return rows == null ? new JSONArray() : rows;
    }

    private boolean isPermissionDeniedPage(String content) {
        return content.contains("你没有该权限") || content.contains("非法访问");
    }
}
