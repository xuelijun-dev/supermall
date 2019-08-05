package com.supermall.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Table(name = "tb_spu")
public class Spu {
   @Id
   @KeySql(useGeneratedKeys = true)
    private Long id;
    private String title;//标题
    private String subTitle;//子标题
    private Long cid1;//1级类目id
    private Long cid2;//2级类目id
    private Long cid3;//3级类目id
    private Long brandId;//商品所属品牌id
    private Boolean saleable;//是否上架，0下架，1上架
    @JsonIgnore
    private Boolean valid;//是否有效，0已删除，1有效
    private Date createTime; //添加时间
    @JsonIgnore
    private Date lastUpdateTime; //最后修改时间
    @Transient
    private String bname;//品牌名
    @Transient
    private String cname;//类别名
    @Transient
    private List<Sku> skus;
    @Transient
    private SpuDetail spuDetail;

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getTitle() {
  return title;
 }

 public void setTitle(String title) {
  this.title = title;
 }

 public String getSubTitle() {
  return subTitle;
 }

 public void setSubTitle(String subTitle) {
  this.subTitle = subTitle;
 }

 public Long getCid1() {
  return cid1;
 }

 public void setCid1(Long cid1) {
  this.cid1 = cid1;
 }

 public Long getCid2() {
  return cid2;
 }

 public void setCid2(Long cid2) {
  this.cid2 = cid2;
 }

 public Long getCid3() {
  return cid3;
 }

 public void setCid3(Long cid3) {
  this.cid3 = cid3;
 }

 public Long getBrandId() {
  return brandId;
 }

 public void setBrandId(Long brandId) {
  this.brandId = brandId;
 }

 public Boolean getSaleable() {
  return saleable;
 }

 public void setSaleable(Boolean saleable) {
  this.saleable = saleable;
 }

 public Boolean getValid() {
  return valid;
 }

 public void setValid(Boolean valid) {
  this.valid = valid;
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

 public String getBname() {
  return bname;
 }

 public void setBname(String bname) {
  this.bname = bname;
 }

 public String getCname() {
  return cname;
 }

 public void setCname(String cname) {
  this.cname = cname;
 }

 public List<Sku> getSkus() {
  return skus;
 }

 public void setSkus(List<Sku> skus) {
  this.skus = skus;
 }

 public SpuDetail getSpuDetail() {
  return spuDetail;
 }

 public void setSpuDetail(SpuDetail spuDetail) {
  this.spuDetail = spuDetail;
 }

 public Spu() {
 }

 public Spu(Long id, String title, String subTitle, Long cid1, Long cid2, Long cid3, Long brandId, Boolean saleable, Boolean valid, Date createTime, Date lastUpdateTime, String bname, String cname, List<Sku> skus, SpuDetail spuDetail) {
  this.id = id;
  this.title = title;
  this.subTitle = subTitle;
  this.cid1 = cid1;
  this.cid2 = cid2;
  this.cid3 = cid3;
  this.brandId = brandId;
  this.saleable = saleable;
  this.valid = valid;
  this.createTime = createTime;
  this.lastUpdateTime = lastUpdateTime;
  this.bname = bname;
  this.cname = cname;
  this.skus = skus;
  this.spuDetail = spuDetail;
 }

 @Override
 public String toString() {
  return "Spu{" +
          "id=" + id +
          ", title='" + title + '\'' +
          ", subTitle='" + subTitle + '\'' +
          ", cid1=" + cid1 +
          ", cid2=" + cid2 +
          ", cid3=" + cid3 +
          ", brandId=" + brandId +
          ", saleable=" + saleable +
          ", valid=" + valid +
          ", createTime=" + createTime +
          ", lastUpdateTime=" + lastUpdateTime +
          ", bname='" + bname + '\'' +
          ", cname='" + cname + '\'' +
          ", spuDetail=" + spuDetail +
          '}';
 }
}
