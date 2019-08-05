package com.supermall.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_pay_log")
public class PayLog {
    @Id
    private Long orderId;
    private Long totalFee;
    private Long userId;
    private String transactionId;
    private Integer status;
    private Integer payType;
    private String bankType;
    private Date createTime;
    private Date payTime;
    private Date refundTime;
    private Date closedTime;
}