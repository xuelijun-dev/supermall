package com.supermall.cart.service;

import com.supermall.auth.pojo.UserInfo;
import com.supermall.cart.interceptort.UserInterceptor;
import com.supermall.cart.pojo.Cart;
import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uid:";
    public void addCart(Cart cart){
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //haskey
        String skuid = cart.getSkuId().toString();
        //记录num
        Integer num = cart.getNum();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        //判断购物车商品是否存在
        if(operation.hasKey(skuid)){
            //存在修改数量
            String json = operation.get(skuid).toString();
            cart = JsonUtils.toBean(json, Cart.class);
            cart.setNum(cart.getNum()+num);
        }
        //写回redis
        operation.put(skuid,JsonUtils.toString(cart));

    }

    public List<Cart> queryCartList() {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)) {
            throw new SmException(ExceptionEnum.CART_NOT_FOUND);
        }
        //获取登录用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        List<Cart> carts = operation.values().stream().map(o -> JsonUtils.toBean(o.toString(), Cart.class)).collect(Collectors.toList());
        return carts;
    }

    public void updateCartNum(Long skuId, Integer num) {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        String skuidKey = skuId.toString();
        if (!redisTemplate.hasKey(key)) {
            throw new SmException(ExceptionEnum.CART_NOT_FOUND);
        }
        //获取登录用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        //判断商品是否存在
        if (!operation.hasKey(skuidKey)) {
            throw new SmException(ExceptionEnum.CART_NOT_FOUND);
        }
        //查购物物车
        Cart cart = JsonUtils.toBean(operation.get(skuidKey).toString(), Cart.class);
        cart.setNum(num);
        //写回redis
        operation.put(skuidKey,JsonUtils.toString(cart));

    }

    public void deleteCart(Long skuId) {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        String skuidKey = skuId.toString();
        redisTemplate.opsForHash().delete(key,skuidKey);
    }
}
