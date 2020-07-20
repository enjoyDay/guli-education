package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.EmailCheckUtils;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginInfoVo;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author liukun
 * @since 2020-06-29
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录，使用jwt返回token
     *
     * @param loginVo
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        String email = loginVo.getEmail();
        String password = loginVo.getPassword();

        // 校验参数
        if (StringUtils.isEmpty(email) ||
                StringUtils.isEmpty(password) ||
                !EmailCheckUtils.checkEmail(email)) {
            throw new GuliException(20001, "登录参数无效");
        }

        //获取会员
        UcenterMember member = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("email", email));
        if (null == member) {
            throw new GuliException(20001, "该邮箱对应的用户不存在");
        }

        // 校验密码
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(20001, "密码不正确");
        }

        // 校验用户是否被禁用
        if (member.getIsDisabled()) {
            throw new GuliException(20001, "用户已经被禁止登录");
        }

        //使用JWT生成token字符串
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        return token;
    }

    /**
     * 用户注册，使用阿里云短信注册，可以使用邮箱替换
     *
     * @param registerVo
     */
    @Override
    public void register(RegisterVo registerVo) {
        System.out.println("注册信息：" + registerVo);
        //获取注册信息，进行校验
        String nickname = registerVo.getNickname();
        String email = registerVo.getEmail();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        //校验参数
        if (StringUtils.isEmpty(email) ||
                !EmailCheckUtils.checkEmail(email) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(nickname)) {
            throw new GuliException(20001, "用户注册参数无效");
        }

        //校验校验验证码
        //从redis获取发送的验证码
        String emailCode = redisTemplate.opsForValue().get(email);
        if (StringUtils.isEmpty(emailCode)) {
            throw new GuliException(20001, "验证码失效，请重新发送");
        }
        if (!code.equals(emailCode)) {
            throw new GuliException(20001, "验证码错误");
        }

        // 查询数据库中是否有相同的邮箱地址
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("email", email));
        if (count.intValue() > 0) {
            throw new GuliException(20001, "该邮箱已经被占用！");
        }

        // 添加注册信息
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setEmail(email);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://127.0.0.1:8002/upload/avatar/2020-06-30/e04a9a48c0034365ac2fb25d1cced298.jpg");
        baseMapper.insert(member);
    }

    @Override
    public LoginInfoVo getLoginInfo(String memberId) {
        UcenterMember member = baseMapper.selectById(memberId);
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        BeanUtils.copyProperties(member, loginInfoVo);
        return loginInfoVo;
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        return baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("openid", openid));
    }

    /**
     * 统计一天的注册量
     * @param day
     * @return
     */
    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.selectRegisterCount(day);
    }
}
