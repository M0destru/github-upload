package token;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Token {
    public static Map<String, String> statementDecimalMap = new HashMap<String, String>();
    {
        statementDecimalMap.put(">", ">");
        statementDecimalMap.put("больше", ">");
        statementDecimalMap.put("более", ">");
        statementDecimalMap.put("<", "<");
        statementDecimalMap.put("меньше", "<");
        statementDecimalMap.put("менее", ">");
        statementDecimalMap.put("=", "=");
        statementDecimalMap.put("равно", "=");
        statementDecimalMap.put("равный", "=");
    }

    public static Map<String, String> statementDateMap = new HashMap<String, String>();
    {
        statementDateMap.put("позднее", ">");
        statementDecimalMap.put("после", ">");
        statementDateMap.put("от", ">");
        statementDateMap.put("раньше", "<");
        statementDateMap.put("ранее", "<");
        statementDecimalMap.put("до", "<");
    }

    public static Map<String, Callable<LocalDate>> dateTimeMap = new HashMap<String, Callable<LocalDate>>();
    {
        dateTimeMap.put("сегодня", LocalDate::now);
        dateTimeMap.put("сегодняшний", LocalDate::now);
        dateTimeMap.put("вчера", () -> LocalDate.now().minusDays(1));
        dateTimeMap.put("вчерашний", () -> LocalDate.now().minusDays(1));
        dateTimeMap.put("позавчера", () -> LocalDate.now().minusDays(2));
        dateTimeMap.put("позавчерашний", () -> LocalDate.now().minusDays(2));
        dateTimeMap.put("послезавтра", () -> LocalDate.now().plusDays(1));
        dateTimeMap.put("послезавтрашний", () -> LocalDate.now().plusDays(1));
    }

    public static Map<String, Double> constValMap = new HashMap<String, Double>();
    {
        constValMap.put("ноль", 0.);
        constValMap.put("один", 1.);
        constValMap.put("два", 2.);
        constValMap.put("три", 3.);
        constValMap.put("четыре", 4.);
        constValMap.put("пять", 5.);
        constValMap.put("шесть", 6.);
        constValMap.put("семь", 7.);
        constValMap.put("восемь", 8.);
        constValMap.put("девять", 9.);
        constValMap.put("десять", 10.);
        constValMap.put("одиннадцать", 11.);
        constValMap.put("двенадцать", 12.);
        constValMap.put("тринадцать", 13.);
        constValMap.put("четырнадцать", 14.);
        constValMap.put("пятнадцать", 15.);
        constValMap.put("шестнадцать", 16.);
        constValMap.put("семнадцать", 17.);
        constValMap.put("восемнадцать", 18.);
        constValMap.put("девятнадцать", 19.);
        constValMap.put("двадцать", 20.);
        constValMap.put("тридцать", 30.);
        constValMap.put("сорок", 40.);
        constValMap.put("пятьдесят", 50.);
        constValMap.put("шестьдесят", 60.);
        constValMap.put("семьдесят", 70.);
        constValMap.put("восемьдесят", 80.);
        constValMap.put("девяносто", 90.);
        constValMap.put("сто", 100.);
        constValMap.put("двести", 200.);
        constValMap.put("триста", 300.);
        constValMap.put("четыреста", 400.);
        constValMap.put("пятьсот", 500.);
        constValMap.put("шестьсот", 600.);
        constValMap.put("семьсот", 700.);
        constValMap.put("восемьсот", 800.);
        constValMap.put("девятьсот", 900.);
        constValMap.put("тысяча", 1000.);
        constValMap.put("миллион", 1000000.);
        constValMap.put("миллиард", 1000000000.);
    }

    protected ETokenType type;
    protected String textFragment;

    public ETokenType getType () {
        return type;
    }

    public String getTextFragment () {return textFragment; }
    public void setTextFragment (String value) {
        textFragment = value;
    }

    public Token (String textFragment) {
        this.textFragment = textFragment;
    }

    @Override
    public String toString() {
        return String.format("\nType: %s\nTextFragment: %s", type, textFragment);
    }
}

