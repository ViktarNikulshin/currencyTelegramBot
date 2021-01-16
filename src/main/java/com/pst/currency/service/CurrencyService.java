package com.pst.currency.service;


import com.pst.currency.models.Currency;
import com.pst.currency.models.News;

import java.util.List;


public interface CurrencyService {

    Currency getCurrencyByCityAndId(String city, String Id);

    List<News> getNews();
}
