package com.pst.currency.controller;

import com.pst.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/currency/{city}/{id}")
        public ResponseEntity<?> getCityById(@PathVariable(value = "city") String city,
                                             @PathVariable(value = "id") String id) {
            return new ResponseEntity<>(currencyService.getCurrencyByCityAndId(city, id), HttpStatus.OK);
    }
    @GetMapping("/news")
        public ResponseEntity<?> getNews(){
        return new ResponseEntity<>(currencyService.getNews(),HttpStatus.OK);
    }
}
