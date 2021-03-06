package parsing;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stemmer {
    private Process proc;
    private List<String> stopwords = Arrays.stream(new String[]{"а", "е", "и", "ж", "м", "о", "на", "ни", "об", "но", "он", "мне", "мои", "мож", "она", "они", "оно", "мной", "много", "многочисленное", "многочисленная", "многочисленные", "многочисленный", "мною", "мой", "мог", "могут", "можно", "может", "можхо", "мор", "моя", "моё", "мочь", "над", "нее", "оба", "нам", "нем", "нами", "ними", "мимо", "немного", "одной", "одного", "однажды", "однако", "меня", "нему", "ней", "наверху", "него", "мало", "надо", "наиболее", "недавно", "миллионов", "недалеко", "между", "низко", "меля", "нельзя", "нибудь", "непрерывно", "наконец", "никогда", "никуда", "нас", "наш", "нет", "нею", "неё", "них", "мира", "наша", "наше", "наши", "ничего", "начала", "нередко", "несколько", "обычно", "опять", "около", "мы", "ну", "нх", "от", "отовсюду", "особенно", "нужно", "очень", "отсюда", "в", "во", "вон", "вниз", "внизу", "вокруг", "вот",
            "вверх", "вам", "вами", "важное", "важная", "важные", "важный", "вдали", "везде", "ведь", "вас", "ваш", "ваша", "ваше", "ваши", "впрочем", "весь", "вдруг", "вы", "все", "всем", "всеми", "времени", "время", "всему", "всего", "всегда", "всех", "всею", "всю", "вся", "всё", "всюду", "г", "говорил", "говорит", "где", "да", "ее", "за", "из", "ли", "же", "им", "до", "по", "ими", "под", "иногда", "довольно", "именно", "долго", "должно", "пожалуйста", "значит", "иметь", "пока", "ему", "имя", "пор", "пора", "потом", "потому", "после", "почему", "почти", "посреди", "ей", "его", "дел", "или", "без", "день", "занят", "занята", "занято", "заняты", "действительно", "давно", "даже", "алло", "жизнь", "далеко", "близко", "здесь", "дальше", "для", "лет", "зато", "даром", "перед", "затем", "зачем", "лишь", "ею", "её",
            "их", "бы", "еще", "при", "был", "про", "процентов", "против", "просто", "бывает", "бывь", "если", "была", "были", "было", "будем", "будет", "будете", "будешь", "прекрасно", "буду", "будь", "будто", "будут", "ещё", "друго", "другое", "другой", "другие", "другая", "других", "есть", "быть", "лучше", "ком", "к", "конечно", "кому", "кого", "когда", "которой", "которого", "которая", "которые", "который", "которых", "кем", "каждое", "каждая", "каждые", "каждый", "кажется", "как", "какой", "какая", "кто", "кроме", "куда", "кругом", "с", "т", "у", "я", "та", "те", "уж", "со", "то", "том", "снова", "тому", "совсем", "того", "тогда", "тоже", "собой", "тобой", "собою", "тобою", "сначала", "только", "уметь", "тот", "тою", "хорошо", "хотеть", "хочешь", "хоть", "хотя", "свое", "свои", "твой", "своей", "своего", "своих", "свою", "твоя", "твоё", "раз", "уже", "сам", "там", "тем", "чем", "сама", "сами", "теми", "само", "рано", "самом", "самому", "самой", "самого",
            "самим", "самими", "самих", "саму", "чему", "сейчас", "чего", "себе", "тебе", "сеаой", "разве", "теперь", "себя", "тебя", "спасибо", "слишком", "так", "такое", "такой", "такие", "также", "такая", "сих", "тех", "чаще", "через", "часто", "сколько", "сказал", "сказала", "сказать", "ту", "ты", "эта", "эти", "что", "это", "чтоб", "этом", "этому", "этой", "этого", "чтобы", "этот", "стал", "туда", "этим", "этими", "рядом", "этих", "тут", "эту", "суть", "чуть", "тысяч", "без", "более", "бы", "был", "была", "были", "было", "быть", "вам", "вас", "ведь", "весь", "вдоль", "вместо", "вне", "вниз", "внизу", "внутри", "во", "вокруг", "вот", "все", "всегда", "всего", "всех", "вы", "где", "да", "давай", "давать", "даже", "для", "до", "достаточно", "его", "ее", "её", "если", "есть", "ещё", "же", "за", "за исключением", "здесь", "из", "из-за", "или", "им",
            "иметь", "их", "как", "как-то", "кто", "когда", "кроме", "кто", "ли", "либо", "мне", "может", "мои", "мой", "мы", "на", "навсегда", "над", "надо", "наш", "него", "неё", "нет", "ни", "них", "но", "ну", "об", "однако", "он", "она", "они", "оно", "от", "отчего", "очень", "по", "под", "после", "потому", "потому что", "почти", "при", "про", "снова", "со", "так", "также", "такие", "такой", "там", "те", "тем", "то", "того", "тоже", "той", "только", "том", "тут", "ты", "уже", "хотя", "чего", "чего-то", "чей", "чем", "что", "чтобы", "чьё", "чья", "эта", "эти", "это"}).toList();
    private boolean isStemmerLoaded;

    public Stemmer() {
        InitializeStemmer();
    }

    public boolean getIsStemmerLoaded() {
        return isStemmerLoaded;
    }

    public boolean InitializeStemmer() {
        isStemmerLoaded = true;
        try {
            proc = Runtime.getRuntime().exec("mystem -nld");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            isStemmerLoaded = false;
        }
        return isStemmerLoaded;
    }

    private String Analyze(String word) {
        synchronized (proc) {
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
            } catch (IOException ex) {
                return "";
            }
            var outStream = new BufferedOutputStream(proc.getOutputStream());
            var wordForms = new ArrayList<String>();
            try {
                outStream.write((word + " м\n").getBytes("UTF-8"));
                outStream.flush();
                var nextLine = br.readLine();
                while (!nextLine.equals("м")) {
                    wordForms.add(nextLine.trim());
                    nextLine = br.readLine();
                }
            } catch (IOException ex) {
                return "";
            }

            if (wordForms.size() != 1)
                return word.toLowerCase();
            return wordForms.get(0).replace("?", "").toLowerCase();
        }
    }

    public List<String> RunAnalysis(String str) {
        if (!isStemmerLoaded) return null;
        return Arrays.stream(str.split(" | +")).toList().stream().map(this::Analyze).filter(word -> !stopwords.contains(word)).toList();
    }
}