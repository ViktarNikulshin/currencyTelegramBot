package com.pst.currency.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pst.currency.models.Currency;
import com.pst.currency.models.News;
import com.pst.currency.service.BelarusProxy;
import com.pst.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final BelarusProxy belarusProxy;
    @Override
    public Currency getCurrencyByCityAndId(String city, String id) {
        String string = belarusProxy.setCurrencyByCity(city);
        Gson gson = new Gson();
        List<Currency> list= gson.fromJson(string, new TypeToken<List<Currency>>(){}.getType());
        Currency currency = list.stream().filter(c ->c.getSap_id().equals(id)).findFirst().orElse(new Currency());
        return currency;
    }

    @Override
    public List<News> getNews() {
        String strNews = belarusProxy.setNews("ru");
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Gson gson = new Gson();
        List<News> list = gson.fromJson(strNews, new TypeToken<List<News>>(){}.getType());
        List <News> news = list.stream().filter(n ->
                ChronoUnit.DAYS.between(now, LocalDate.parse(n.getStart_date(), formatter)) >= -20).collect(Collectors.toList());
        System.out.println("list news = " + list.size());
        return news;
    }
}
