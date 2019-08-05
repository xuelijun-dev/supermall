package com.supermall.item.service;

import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.item.mapper.SpuDetailMapper;
import com.supermall.item.pojo.SpuDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuDetailService {
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    public SpuDetail queryDetailById(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if(spuDetail == null){
            throw new SmException(ExceptionEnum.SPUDETAIL_NOT_FOUNT);
        }
        return spuDetail;
    }
}
