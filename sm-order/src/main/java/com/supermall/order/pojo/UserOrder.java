package com.supermall.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_order_user")
public class UserOrder {
    @Id
    private Long id;
    private Long skuid;
    private Long orderid;
    private Long uid;
}