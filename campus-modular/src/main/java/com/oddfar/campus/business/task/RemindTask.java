package com.oddfar.campus.business.task;

import cn.hutool.core.date.DateUtil;
import com.oddfar.campus.business.api.FeishuMessageApi;
import com.oddfar.campus.business.entity.IUser;
import com.oddfar.campus.business.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Comparator;
import java.util.List;

/**
 * 提醒定时任务
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class RemindTask {
    private static final Logger logger = LoggerFactory.getLogger(RemindTask.class);

    private final IUserService iUserService;


    /**
     * 每天10点检测token是否快过期了
     */
    @Async
    @Scheduled(cron = "0 0 10 ? * * ")
//    @Scheduled(cron = "0 0/10 * * * ? ")
    public void checkToken() {
        logger.info("【检测token过期任务】定时任务开始");
        List<IUser> iUsers = iUserService.selectReservationUser();
        IUser user = iUsers.stream().min(Comparator.comparing(IUser::getExpireTime)).orElse(null);
        if (null != user) {
            if (user.getExpireTime().getTime() - System.currentTimeMillis() < 1000 * 60 * 60 * 24 * 5) {
                logger.info("【检测token过期任务】token快过期了，尽快更换");
                /*
                //异常推送，写死token
                String token = "51270455f0de428d9f320ec1856713c1";
                PushPlusApi.sendNoticeNow(token, "i茅台token快过期了", "i茅台token快过期了，尽快更换", "txt");
                */
                String expireTime = DateUtil.formatDate(user.getExpireTime());
                FeishuMessageApi.sendMessage("i茅台token快过期了，过期时间：" + expireTime + "，尽快更换");
            }
        }
        logger.info("【检测token过期任务】定时任务结束");
    }

}