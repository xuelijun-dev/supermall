package com.supermall.user.service;

import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.common.utils.NumberUtils;
import com.supermall.user.mapper.UserMapper;
import com.supermall.user.pojo.User;
import com.supermall.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private static final String KEY_PREFIX ="user:verify:phone:";
    @Autowired
    UserMapper userMapper;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    public Boolean checkData(String data, Integer type) {
        User record = new User();
        //判断数据类型
        if(StringUtils.isBlank(data))
            throw new SmException(ExceptionEnum.INVAID_DATA_TYPE_ERROR);
        switch (type){
            case 1 :
                record.setUsername(data);
                break;
            case 2 :
                record.setPhone(data);
                break;
            default:
                throw new SmException(ExceptionEnum.INVAID_DATA_TYPE_ERROR);
        }
        return userMapper.selectCount(record) == 0;
    }

    public void sendCode(String phone) {
        //生成key
        String key = KEY_PREFIX+phone;
        //生成验证码
        String code = NumberUtils.generateCode(6);
        //封装发送数据
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        //发送验证码
        amqpTemplate.convertAndSend("sm.sms.exchange","sms.verify.code",msg);
        //存储到缓存
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
    }

    public void register(User user, String code) {
        //从redis中取出验证码
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //校验验证码
        if (!StringUtils.equals(code,cacheCode)) {
            throw new SmException(ExceptionEnum.INVAID_VERIFY_CODE);
        }
        //生成盐
        String salt=CodecUtils.generateSalt();
        user.setSalt(salt);
        // 对密码加密
        user.setPassword( CodecUtils.md5Hex(user.getPassword(),salt));
        //写入数据库
        user.setCreated(new Date());
        userMapper.insert(user);
    }
    public User queryUserByUsernameAndPassword(String username,String password){
        //查询用户
        User recode = new User();
        recode.setUsername(username);
        User user = userMapper.selectOne(recode);
        //校验
        if(user==null){
            throw new SmException(ExceptionEnum.INVAID_USERNAME_PASSWORD);
        }
        //校验密码
        if (!StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password,user.getSalt()))) {
            throw new SmException(ExceptionEnum.INVAID_USERNAME_PASSWORD);
        }

        //用户名和密码正确
        return user;
    }
}
