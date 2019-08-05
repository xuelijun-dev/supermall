package com.supermall.order.client;

import com.supermall.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-services")
public interface GoodClient extends GoodsApi {
}
