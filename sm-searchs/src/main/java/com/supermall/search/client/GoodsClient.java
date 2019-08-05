package com.supermall.search.client;

import com.supermall.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-services")
public interface GoodsClient  extends GoodsApi {
}
