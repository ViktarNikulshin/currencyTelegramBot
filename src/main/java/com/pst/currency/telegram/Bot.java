package com.pst.currency.telegram;


import com.pst.currency.models.Currency;
import com.pst.currency.models.News;
import com.pst.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    private final CurrencyService currencyService;


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        if(update.getMessage() == null) {
            System.out.println(update.getCallbackQuery().getData());
            if(update.getCallbackQuery().getData().equals("news")){
                List<News> news = currencyService.getNews();
                message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                StringBuffer str = new StringBuffer();
                if(news.isEmpty()){
                    str.append("Новостей за последние дни нет");
                } else {
                    for (News itemNew : news) {
                        str.append(itemNew.getName_ru() + "\n " +
                                "Ссылка:  " + itemNew.getLink() + "\n");
                    }
                }
                message.setText(str.toString());
                setButtons(message);
            }else {
                String[] array = update.getCallbackQuery().getData().split(" ");
                Currency currency = currencyService.getCurrencyByCityAndId(array[0], array[1]);
                message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                StringBuffer str = new StringBuffer("Отделение: " + currency.getFilials_text() + "\n " +
                        "Режим работы: " + currency.getInfo_worktime() + "\n" +
                        "USD покупка | продажа:  " + currency.getUSD_in() + " | " + currency.getUSD_out() + "\n" +
                        "EUR покупка | продажа:  " + currency.getEUR_in() + " | " + currency.getEUR_out());
                message.setText(str.toString());
                setButtons(message);
            }
        }else {
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
            message.setText("Hi");
            setButtons(message);
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {

            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        List<InlineKeyboardButton> keyboardButtons1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons3 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        inlineKeyboardButton1.setText("Брест. пр. Партизанский");
        inlineKeyboardButton1.setCallbackData("Брест 50011756");
        keyboardButtons1.add(inlineKeyboardButton1);
        inlineKeyboardButton2.setText("Брест. ул. Московская");
        inlineKeyboardButton2.setCallbackData("Брест 50010806");
        keyboardButtons1.add(inlineKeyboardButton2);
        inlineKeyboardButton3.setText("Минск. ТЦ Столица");
        inlineKeyboardButton3.setCallbackData("Минск 50000437");
        keyboardButtons2.add(inlineKeyboardButton3);
        inlineKeyboardButton4.setText("Минск. ул. Сухаревская");
        inlineKeyboardButton4.setCallbackData("Минск 50000100");
        keyboardButtons2.add(inlineKeyboardButton4);
        inlineKeyboardButton5.setText("Новости");
        inlineKeyboardButton5.setCallbackData("news");
        keyboardButtons1.add(inlineKeyboardButton5);
        rowList.add(keyboardButtons1);
        rowList.add(keyboardButtons2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
