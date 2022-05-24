package parsing;

import keywords.dictionary.Database;
import token.KeyWord;
import token.Token;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
    private Database dictionary;
    private String query;
    private ArrayList<KeyWord> keyWords;

    public Parser (Database dictionary)
    {
        keyWords = new ArrayList<>();
        this.dictionary = dictionary;
        LoadDictionary();
    }

    public Parser (Database dictionary, String query) {
        this.dictionary = dictionary;
        this.query = query;
        keyWords = new ArrayList<KeyWord>();
        LoadDictionary();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery (String query) {
        this.query = query;
    }

    public boolean LoadDictionary () {
        try (Connection conn = dictionary.GetConnection()) {
            String query =
                    "SELECT * FROM keywords";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String owlclass = rs.getString("owlclass");
                    String dataProperty = rs.getString("data_property");
                    String name = rs.getString("name");
                    String initialForm = rs.getString("initial_form");
                    KeyWord newKeyword = new KeyWord(name, id, owlclass,
                            dataProperty, initialForm);
                    keyWords.add(newKeyword);
                }
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public List<Token> RunParsing () {
        /*String[] words = query.toLowerCase().replaceAll("(?<!\\d)[.,;:](?!\\d)", "").split(" ");*/
        String[] words = query.toLowerCase().replaceAll("/[^[a-zA-Z][0-9]*[.,]?[0-9]+$]|_/g", "").split(" ");
        Lexer lex = new Lexer(keyWords, words);
        List<Token> lstTokens = new ArrayList<Token>();
        while (!lex.getIsAnalyzeEnd()) {
            Token cToken = lex.Analyze();
            if (cToken != null) {
                lstTokens.add(cToken);
            }
        }
        return lstTokens;
    }

}
