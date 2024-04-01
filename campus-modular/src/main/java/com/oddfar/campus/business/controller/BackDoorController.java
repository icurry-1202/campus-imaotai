package com.oddfar.campus.business.controller;

import com.oddfar.campus.business.api.PushPlusApi;
import com.oddfar.campus.business.mapper.IUserMapper;
import com.oddfar.campus.business.service.IUserService;
import com.oddfar.campus.common.annotation.Anonymous;
import com.oddfar.campus.common.annotation.ApiResource;
import com.oddfar.campus.common.annotation.Log;
import com.oddfar.campus.common.domain.R;
import com.oddfar.campus.common.enums.ResBizTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backdoor")
@ApiResource(name = "后门接口", appCode = "backdoor", resBizType = ResBizTypeEnum.BUSINESS)
@Log(openLog = false)
@Slf4j
public class BackDoorController {

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private IUserService iUserService;

    @Anonymous
    @GetMapping(value = "/checkDB", name = "校验数据库连接状态")
    public R checkDB() {
        try {
            iUserMapper.selectReservationUser();
        } catch (Exception e) {
            //异常推送，写死token
            String token = "51270455f0de428d9f320ec1856713c1";
            PushPlusApi.sendNoticeNow(token, "i茅台数据库读取异常！", "异常信息：" + e.getMessage(), "txt");
            log.error("error", e);
            return R.error();
        }
        return R.ok();
    }

    @Anonymous
    @GetMapping(value = "/sendDeadMsg", name = "sendDeadMsg")
    public R sendDeadMsg() {
        //异常推送，写死token
        String token = "51270455f0de428d9f320ec1856713c1";
        PushPlusApi.sendNoticeNow(token, "i茅台服务dead", "i茅台服务端口8160 is dead，重启服务", "txt");
        return R.ok();
    }
}
