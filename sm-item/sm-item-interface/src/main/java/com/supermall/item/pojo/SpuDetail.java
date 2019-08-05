package com.supermall.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述信息
    private String specialSpec;// 全部规格参数数据
    private String genericSpec;// 特有规格参数及可选值信息
    private String packingList;// 包装清单
    private String afterService;// 售后服务

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(String specialSpec) {
        this.specialSpec = specialSpec;
    }

    public String getGenericSpec() {
        return genericSpec;
    }

    public void setGenericSpec(String genericSpec) {
        this.genericSpec = genericSpec;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }

    public SpuDetail() {
    }

    public SpuDetail(Long spuId, String description, String specialSpec, String genericSpec, String packingList, String afterService) {
        this.spuId = spuId;
        this.description = description;
        this.specialSpec = specialSpec;
        this.genericSpec = genericSpec;
        this.packingList = packingList;
        this.afterService = afterService;
    }

    @Override
    public String toString() {
        return "SpuDetail{" +
                "spuId=" + spuId +
                ", description='" + description + '\'' +
                ", specialSpec='" + specialSpec + '\'' +
                ", genericSpec='" + genericSpec + '\'' +
                ", packingList='" + packingList + '\'' +
                ", afterService='" + afterService + '\'' +
                '}';
    }
}