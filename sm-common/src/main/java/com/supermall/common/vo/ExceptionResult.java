package com.supermall.common.vo;

import com.supermall.common.enums.ExceptionEnum;
import lombok.Data;

@Data
public class ExceptionResult {
    private int code;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum em){
        this.code = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
