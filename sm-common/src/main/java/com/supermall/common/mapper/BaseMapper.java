package com.supermall.common.mapper;


import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, InsertListMapper<T>, IdListMapper<T,Long> {
}
