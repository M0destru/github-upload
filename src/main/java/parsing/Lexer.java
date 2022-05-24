package parsing;

import org.apache.commons.lang3.StringUtils;
import token.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Lexer {
    private Stemmer stemmer;
    private String[] words;
    private int pos;
    private String curWord;
    private Token curToken;
    private ArrayList<KeyWord> keyWords;
    private boolean isAnalyzeEnd;

    public int getPos() {
        return pos;
    }

    public boolean getIsAnalyzeEnd() { return isAnalyzeEnd; }

    public void setPos(int value) {
        pos = value - 1;
        curWord = words[pos];
        GetNextWord();
    }

    public Lexer(ArrayList<KeyWord> keyWords, String[] words) {
        this.keyWords = keyWords;
        this.words = words;
        pos = 0;
        curWord = words[pos++];
        stemmer = new Stemmer();
        stemmer.InitializeStemmer();
        isAnalyzeEnd = false;
    }

    public void GetNextWord() {
        if (pos == words.length ) {
            curWord = "\0";
            isAnalyzeEnd = true;
        }
        else {
            curWord = words[pos++];
            isAnalyzeEnd = false;
        }
    }

    public LocalDate ParseDate () {
        LocalDate date = null;
        String strDate = StringUtils.join(stemmer.RunAnalysis(curWord), " ");
        if (Token.dateTimeMap.containsKey(strDate)) {
            try {
                date = Token.dateTimeMap.get(strDate).call();
            }
            catch (Exception ex) { }
            GetNextWord();
        }
        else {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
            try {
                date = LocalDate.parse(curWord, format);
                GetNextWord();
            }
            catch (DateTimeParseException ex) { }
        }
        return date;
    }

    private BigDecimal ParseDecimal () {
        BigDecimal value = new BigDecimal(-1);
        /* проверить, что значение записано в числовом виде */
        try {
            value = new BigDecimal(Double.parseDouble(curWord));
            GetNextWord();
        }
        catch (NumberFormatException e) { }

        /* проверить, что значение записано в виде слов (пятьдесят, сорок шесть, сто) */
        if (value.equals(-1)) {
            String strValue = "";
            do {
                List<String> strValueLst = stemmer.RunAnalysis(curWord);
                String strDigit = StringUtils.join(strValueLst, " ");
                strValue = (strValue + " " + strDigit).trim();
                if (Token.constValMap.containsKey(strDigit)) { // TODO: работает только до тысяч и не работает с веществ. знач.
                    value = value.equals(-1) ? new BigDecimal(Token.constValMap.get(strDigit)) : value.add(new BigDecimal(Token.constValMap.get(strDigit)));
                    GetNextWord();
                }
                else
                    break;
            } while (true);
        }
        return value;
    }

    private Expression GetDateStatement ()
    {
        Statement st;
        boolean isNegation = false;
        /* если есть отрицание перед оператором сравнения */
        if (curWord.equals("не")) {
            isNegation = true;
            GetNextWord();
        }
        String oper = "=", foundStatement = "";
        /* найден оператор сравнения */
        if (Token.statementDateMap.containsKey(curWord)) {
            oper = Token.statementDateMap.get(curWord);
            foundStatement = isNegation? "не " + curWord: curWord;
            GetNextWord();
        }
        st =  new Statement(foundStatement, oper, isNegation);

        LocalDate date = ParseDate();
        if (date != null) {
            foundStatement += foundStatement.equals("")? date: " " + date;
            return new Expression(foundStatement, new DateRangeVariant(date, date), st);
        }
        return null;
    }

    private Expression GetDecimalStatement () {
        Statement st;
        boolean isNegation = false;
        /* если есть отрицание перед оператором сравнения */
        if (curWord.equals("не")) {
            isNegation = true;
            GetNextWord();
        }
        String oper = "=", foundStatement = "";
        /* найден оператор сравнения */
        if (Token.statementDecimalMap.containsKey(curWord)) {
            oper = Token.statementDecimalMap.get(curWord);
            foundStatement = isNegation? "не " + curWord: curWord;
            GetNextWord();
        }
        st =  new Statement(foundStatement, oper, isNegation);
        BigDecimal value = ParseDecimal();
        if (value.compareTo(BigDecimal.valueOf(-1)) == 1) {
            foundStatement += foundStatement.equals("")? value: " " + value;
            return new Expression(foundStatement, new DecimalVariant(value), st);
        }
        return null;
    }

    private Expression StatementParser () {
        Expression ex = GetDecimalStatement();;
        if (ex == null)
            ex = GetDateStatement();
        return ex;
    }

    public KeyWord SearchKeyWord(String str) {
        String[] strWordsLst = str.split(" ");
        List<String> initFormWordsLst = stemmer.RunAnalysis(str);
        String initForm = StringUtils.join(initFormWordsLst, " ");
        if (initForm.length() == 0  || initFormWordsLst.size() < strWordsLst.length) return null;
        List <KeyWord> matches = keyWords.stream().filter(w -> w.getInitialForm().contains(initForm)).toList();
        return matches.stream().filter(m -> m.getInitialForm().equals(initForm)).findFirst().orElse(matches.stream().findFirst().orElse(null));
    }

    /* парсер ключевых слов */
    private KeyWord KeyWordParser() {
        KeyWord findedKeyWord = null, curKeyWord;
        String textFragment = curWord;
        do {
            curKeyWord = SearchKeyWord(textFragment);
            if (curKeyWord != null) {
                findedKeyWord = curKeyWord;
                GetNextWord();
                textFragment += " " + curWord;
            }
        } while (curKeyWord != null);

        return findedKeyWord;
    }

    /* анализ следующего слова */
    public Token Analyze() {
        /* проверить, что слово является термином онтологии */
        KeyWord foundKeyWord = KeyWordParser();
        if (foundKeyWord != null) {
            curToken = foundKeyWord;
            return curToken;
        }
        /* проверить, что слово является выражением (частью выражения) */
        Expression foundExpr = StatementParser();
        if (foundExpr != null) {
            curToken = foundExpr;
            return curToken;
        }
        GetNextWord();
        return curToken = null;
    }
}
