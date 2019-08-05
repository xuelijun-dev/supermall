package com.supermall.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


@Table(name = "tb_sku")
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;//sku id
    private Long spuId;//spu id
    private String title;//商品标题
    private String images;//商品的图片，多个图片以‘,’分割
    private Long price;//销售价格，单位为分
    private String ownSpec;//特有规格属性在spu属性模板中的对应下标组合
    private String indexes;//sku的特有规格参数键值对，json格式，反序列化时
    private Boolean enable;//是否有效，0无效，1有效
    private Date createTime;//添加时间
    private Date lastUpdateTime;//最后修改时间
    @Transient
    private Integer stock;//库存

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }

    public String getIndexes() {
        return indexes;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Sku() {
    }

    public Sku(Long id, Long spuId, String title, String images, Long price, String ownSpec, String indexes, Boolean enable, Date createTime, Date lastUpdateTime, Integer stock) {
        this.id = id;
        this.spuId = spuId;
        this.title = title;
        this.images = images;
        this.price = price;
        this.ownSpec = ownSpec;
        this.indexes = indexes;
        this.enable = enable;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "id=" + id +
                ", spuId=" + spuId +
                ", title='" + title + '\'' +
                ", images='" + images + '\'' +
                ", price=" + price +
                ", ownSpec='" + ownSpec + '\'' +
                ", indexes='" + indexes + '\'' +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", stock=" + stock +
                '}';
    }
}
