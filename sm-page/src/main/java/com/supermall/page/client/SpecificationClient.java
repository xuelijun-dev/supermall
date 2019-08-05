package com.supermall.page.client;

import com.supermall.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-services")
public interface SpecificationClient extends SpecificationApi {
}
