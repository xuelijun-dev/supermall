package com.supermall.search.client;

import com.supermall.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-services")
public interface BrandClient extends BrandApi {
}
