package parsing;

import keywords.dictionary.Database;
import model.Class;
import model.Ontology;
import model.QuestOWL;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import query.builder.QueryBuilder;
import token.ETokenType;
import token.KeyWord;
import token.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StartParsing {
    /* данные предметной онтологии */
    final static String OWLFile = "src/main/resources/biogenom/biogenom.owl";
    final static String OBDAFile = "src/main/resources/biogenom/biogenom.obda";
    final static String PropertyFile = "src/main/resources/biogenom/biogenom.properties";
    /* строка подключение к БД */
    final static String USER = "postgres";
    final static String PASSWORD = "postgres";
    final static String URL = "jdbc:postgresql://localhost:5432/dictionary";

    public static void main(String[] args) throws Exception {
        /* инициализация парсера */
        Database dictDb = new Database(URL, USER, PASSWORD);
        Parser parser = new Parser(dictDb);

        Scanner in = new Scanner(System.in);
        System.out.print("Choose mode (1 - console input; 2 - test sample from file): ");
        String mode = in.nextLine();
        String nlQuery;
        /* инициализировать онтологию */
        Ontology ont = new Ontology();
        ont.InitializeOntology(OWLFile);

        /* запустить reasoner для выполнения SPARQL-запросов */
        QuestOWL quest = new QuestOWL(OWLFile, OBDAFile, PropertyFile);
        quest.StartReasoner();

        /* создание "построителя запросов" */
        QueryBuilder qb = new QueryBuilder(ont);

        System.out.println(quest.GetSQLQuery("SELECT DISTINCT ?iddiagnosticreport ?createdat ?collectedat\n" +
                "WHERE {\n" +
                "\t?diagnosticreport <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#createdAt> ?createdat ; <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#collectedAt> ?collectedat ; <http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#idDiagnosticReport> ?iddiagnosticreport  .\t\n" +
                "\tFILTER (?collectedat = \"2022-01-23\"^^xsd:date)\n" +
                "}\n" +
                "ORDER BY ?createdat ?collectedat"));

        if (mode.equals("1")) {
            do {
                System.out.print("Input a query (enter 0 if you want to exit): ");
                nlQuery = in.nextLine();
                if (nlQuery.equals("0")) break;
                parser.setQuery(nlQuery);
                var tokens = parser.RunParsing();
                var query = qb.BuildQuery(tokens);
                System.out.println(query + "\n");
                System.out.println(quest.GetSQLQuery(query));
                var result = quest.ExecuteSparqlQuery(query);
                for (var bindingSet : result) {
                    for (var binding : bindingSet) {
                        OWLObject value = binding.getValue();
                        String strOWLObject = ToStringRenderer.getInstance().getRendering(value);
                        /* убрать информацию о диапазоне значений */
                        int indexOfRange = strOWLObject.lastIndexOf("^^");
                        /* оставить только data properties строкового типа */
                        String strOWLValue = strOWLObject.substring(0, indexOfRange).replace("\"", "");
                    }
                }
            } while (true);
            in.close();
        }
        else if (mode.equals("2")) {
            List<List<Token>> results = new ArrayList<List<Token>>();
            List<String> queries = new ArrayList<>();
            /* чтение запросов */
            try {
                File inFile = new File("queries.txt");
                FileReader fr = new FileReader(inFile);
                BufferedReader reader = new BufferedReader(fr);
                nlQuery = reader.readLine();
                while (nlQuery != null) {
                    queries.add(nlQuery);
                    parser.setQuery(nlQuery);
                    List<Token> parsedQuery = parser.RunParsing();
                    results.add(parsedQuery);
                    nlQuery = reader.readLine();
                }
                reader.close();
                fr.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Исходный файл с ЕЯ-запросами не найден\n" + ex.getMessage());
                return;
            } catch (IOException ex) {
                System.out.println("Ошибка при чтении запроса\n" + ex.getMessage());
                return;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            /* запись в файл */
            try {
                FileWriter writer = new FileWriter("output.txt");
                for (int num = 0; num < results.size(); num++) {
                    writer.write(String.format("Запрос %d\nТекст запроса: %s\nРезультат:\n", num + 1, queries.get(num)));
                    for (var token : results.get(num))
                        writer.write(token + "\n");
                    writer.write("\n------------------\n");
                }
                writer.close();
            } catch (IOException ex) {
                System.out.println("Ошибка при выводе результата парсинга\n" + ex.getMessage());
                return;
            }
        }
    }
}
