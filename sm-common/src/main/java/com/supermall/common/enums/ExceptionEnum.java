package com.supermall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExceptionEnum {
    PRICE_CANNOT_BE_NULL(404,"价格不能为空！"),
    BRAND_NOT_FOUND(404,"没有找到品牌信息！"),
    BRAND_SAVE_ERROR(500,"新增品牌失败！"),
    INVAID_FILE_TYPE(400,"新增品牌失败！"),
    SPEC_GROUP_NOT_FOUND(404,"没有查到分类规格组数据！"),
    SPEC_GROUP_PARAM_NOT_FOUND(404,"没有查到分类规格组属性数据！"),
    GOOD_NOT_FOUND(404,"没有查到商品！"),
    GOOD_SAVE_ERROR(500,"商品保存失败！"),
    SPUDETAIL_NOT_FOUNT(404,"没有查询到SPU详细信息！"),
    SKU_NOT_FOUNT(404,"没有查询到SKU信息！"),
    SKU_STOCK_NOT_FOUNT(404,"没有查询到SKU库存信息！"),
    INVAID_DATA_TYPE_ERROR(400,"无效的数据类型！"),
    INVAID_VERIFY_CODE(400,"无效的验证码！"),
    INVAID_USERNAME_PASSWORD(400,"错误的用户名或密码！"),
    UNAUTHORIZED(403,"未通过登录认证"),
    CART_NOT_FOUND(404,"没有购物车商品存在！"),
    ORDER_CREATE_ERROR(404,"订单创建失败！"),
    ORDER_STATUS_UPLOAD_ERROR(404,"订单更新异常！"),
    INVALID_ORDER_PARAM(404,"支付订单参数异常！"),
    STOCK_IS_NOT_ENGOUGH(404,"库存不足！"),
    CATEGORY_NOT_FOUND(404,"没有分类信息");
    ;
    private int code;
    private String msg;
}
