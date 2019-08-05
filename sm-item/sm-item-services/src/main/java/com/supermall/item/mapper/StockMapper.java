package com.supermall.item.mapper;

import com.supermall.common.mapper.BaseMapper;
import com.supermall.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface StockMapper extends BaseMapper<Stock> {
    @Update("UPDATE tb_stock SET stock = stock - #{num} WHERE sku_id = #{id} AND stock >= #{num}")
    int descreaseStock(@Param("id") Long id,@Param("num")Integer num);
}
