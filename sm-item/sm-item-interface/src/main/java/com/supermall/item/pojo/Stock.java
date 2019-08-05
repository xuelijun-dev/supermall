package com.supermall.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;//库存对应的商品sku id
    private Integer seckillStock;//可秒杀库存
    private Integer seckillTotal;//秒杀总数量
    private Integer stock;//库存数量

    public Stock() {
    }

    public Stock(Long skuId, Integer seckillStock, Integer seckillTotal, Integer stock) {
        this.skuId = skuId;
        this.seckillStock = seckillStock;
        this.seckillTotal = seckillTotal;
        this.stock = stock;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Integer seckillStock) {
        this.seckillStock = seckillStock;
    }

    public Integer getSeckillTotal() {
        return seckillTotal;
    }

    public void setSeckillTotal(Integer seckillTotal) {
        this.seckillTotal = seckillTotal;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "skuId=" + skuId +
                ", seckillStock=" + seckillStock +
                ", seckillTotal=" + seckillTotal +
                ", stock=" + stock +
                '}';
    }
}
