package com.pst.currency.service;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "BELARUS-BANK", url = "https://belarusbank.by/api", configuration = FeignAutoConfiguration.class)
public interface BelarusProxy {
    @GetMapping(value = "/kursExchange", params = "city")
    public  String setCurrencyByCity(@RequestParam("city") String city);

    @GetMapping(value = "/news_info", params = "lang")
    public String setNews(@RequestParam("lang") String lang);
}
