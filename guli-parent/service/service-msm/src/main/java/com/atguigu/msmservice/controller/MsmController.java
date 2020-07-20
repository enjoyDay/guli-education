package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.EmailCheckUtils;
import com.atguigu.commonutils.response.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/msmservice/msm")
@CrossOrigin
@Api(description = "阿里云短信服务")
@Slf4j
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送短信的方法
    @ApiOperation(value = "获取短信验证码")
    @GetMapping("send-phone-message/{phone}")
    public R sendMsm(@PathVariable String phone) {
        //1 从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        //2 如果redis获取 不到，进行阿里云发送
        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用service发送短信的方法
        boolean isSend = msmService.sendMessage(param, phone);
        //todo 由于没开通阿里云短信，默认是发送成功的，可以模拟邮箱发送
        isSend = true;
        if (isSend) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }

    /**
     * 发送邮箱的方法
     *
     * @param to 接受邮箱的人
     * @return 发送成功
     */
    @ApiOperation(value = "获取邮箱验证码")
    @PostMapping("send-simple-mail/")
    public R sendSimpleMail(@RequestBody String to) {
        to = URLDecoder.decode(to);
        log.info("获取到的邮箱地址：" + to);
        to = to.replace("=", "");

        if (!EmailCheckUtils.checkEmail(to)) {
            return R.error().message("接收者邮箱地址错误");
        }

        //1 从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(to);
        if (!StringUtils.isEmpty(code)) {
            msmService.sendSimpleMail(to, code);
            return R.ok();
        }

        //2 如果redis获取不到，进行邮箱发送
        //生成随机值，传递邮箱进行发送
        code = RandomUtil.getFourBitRandom();
        //调用service发送短信的方法
        try{
            msmService.sendSimpleMail(to, code);
        }catch (Exception e) {
            throw new GuliException(20001, "发送邮件失败");
        }
        if (true) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(to, code, 5, TimeUnit.MINUTES);
            return R.ok();
        }

        return R.error();
    }
}
