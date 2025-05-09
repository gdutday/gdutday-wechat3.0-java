package com.gdutelc.service.impl;

import com.gdutelc.framework.domain.HiGdutDays;
import com.gdutelc.service.NotificationService;
import org.springframework.stereotype.Service;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/27 21:58
 * NotificationServiceImpl
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    /***
     * test接口显示
     * @return
     */
    @Override
    public HiGdutDays getHiMessage() {
        int arr[][] = new int[10][10];
        return new HiGdutDays("GdutDaysV3");
    }
}
