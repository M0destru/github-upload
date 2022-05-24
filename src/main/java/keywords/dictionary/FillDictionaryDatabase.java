package keywords.dictionary;

import model.Ontology;
import model.QuestOWL;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import parsing.Stemmer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class FillDictionaryDatabase {
    /* данные предметной онтологии */
    final static String OWLFile = "src/main/resources/biogenom/biogenom.owl";
    final static String OBDAFile = "src/main/resources/biogenom/biogenom.obda";
    final static String PropertyFile = "src/main/resources/biogenom/biogenom.properties";

    /* строка подключение к БД */
    final static String USER = "postgres";
    final static String PASSWORD = "postgres";
    final static String URL = "jdbc:postgresql://localhost:5432/dictionary";

    public static void main (String[] args) {
        /* инициализировать онтологию */
        Ontology ont = new Ontology();
        ont.InitializeOntology(OWLFile);

        /* получить список dataProperties */
        var dataProperties = ont.getDataProperties();

        /* запустить reasoner для выполнения SPARQL-запросов */
        QuestOWL quest = new QuestOWL(OWLFile, OBDAFile, PropertyFile);
        quest.StartReasoner();

        /* запустить myStem.exe */
        Stemmer myStem = new Stemmer();

        /* подключение к БД, в которой будет хранится словарь ключевых слов */
        Database db = new Database(URL, USER, PASSWORD);

        try (Connection conn = db.GetConnection()) {
            for (var dp : dataProperties) {
                /* получить значения для dataProperty */
                String sparqlQuery =
                        "SELECT DISTINCT ?y {" +
                        "?x <" + dp.getIRI() + "> ?y . }";
                var bindings = quest.ExecuteSparqlQuery(sparqlQuery);
                for (var bindingSet : bindings) {
                    for (var binding : bindingSet) {
                        OWLObject value = binding.getValue();
                        String strOWLObject = ToStringRenderer.getInstance().getRendering(value);
                        /* убрать информацию о диапазоне значений */
                        int indexOfRange = strOWLObject.lastIndexOf("^^");
                        /* оставить только data properties строкового типа */
                        if (!strOWLObject.substring(indexOfRange).equals("^^xsd:string"))
                            continue;
                        String strOWLValue = strOWLObject.substring(0, indexOfRange).replace("\"", "");

                        /* получить начальную форму */
                        List<String> stemResult = myStem.RunAnalysis(strOWLValue);
                        String resultVal = "";
                        for (String str : stemResult) {
                            resultVal = resultVal.concat(" " + str);
                        }
                        String sqlQuery =
                                "INSERT INTO keywords (owlclass, data_property, name, initial_form) " +
                                "VALUES (?, ?, ?, ?);";
                        try (PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {
                            stmt.setString(1, dp.getDomain().toString());
                            stmt.setString(2, dp.getIRI().toString());
                            stmt.setString(3, strOWLValue.trim());
                            stmt.setString(4, resultVal.trim());
                            stmt.executeUpdate();
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        quest.StopReasoner();
    }
}
