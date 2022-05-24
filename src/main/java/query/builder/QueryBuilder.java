package query.builder;

import model.Class;
import model.DataProperty;
import model.ObjectProperty;
import model.Ontology;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataRange;
import token.*;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryBuilder {
    private Ontology ont;
    private HashMap<String, ObjectProperty> classRelations = new HashMap<>();

    public QueryBuilder(Ontology ont) {
        this.ont = ont;
        for (var classFrom : ont.getOWLClasses()) {
            for (var classTo : ont.getOWLClasses()) {
                if (classFrom != classTo) {
                    /* составить и заполнить пути от каждого класса до каждого */
                    var path = GetPath(classFrom, classTo, null);
                    /* добавление пути */
                    if (path != null) {
                        classRelations.put(String.format("%s %s", classFrom, classTo), path.get(0));
                    }
                }
            }
        }
    }

    public Ontology getOnt() {
        return ont;
    }

    private ArrayList<ObjectProperty> GetPath(Class classFrom, Class classTo, HashSet<Class> usedClasses) {
        if (usedClasses == null)
            usedClasses = new HashSet<>();

        /* помечаем класс как использованный */
        usedClasses.add(classFrom);

        /* проверить, что есть прямая связь между текущими классами */
        ObjectProperty link = ont.GetObjectProperty(classFrom, classTo);
        if (link != null)
            return new ArrayList<ObjectProperty>() {
                {
                    add(link);
                }
            };

        /* для всех соседних классов */
        var neighborClasses = ont.GetNeighborClasses(classFrom);
        for (var neighborClass : neighborClasses) {
            if (!usedClasses.contains(neighborClass)) {
                /* получить путь от соседней вершины до конечной */
                ArrayList<ObjectProperty> foundPath = GetPath(neighborClass, classTo, usedClasses);

                /* если путь не пустой, то вернуть его, добавив objProp между текущим классом и соседом */
                if (foundPath != null) {
                    foundPath.add(0, ont.GetObjectProperty(classFrom, neighborClass));
                    return foundPath;
                }
            }
        }
        return null;
    }

    public HashMap<Class, List<Condition>> TransformTokensToConditions(List<Token> tokens) {
        HashMap<Class, List<Condition>> conditions = new HashMap<>();
        for (var token : tokens) {
            if (token.getType() == ETokenType.Keyword) {
                KeyWord kw = (KeyWord) token;
                Class foundedClass = ont.getOWLClasses().stream().
                        filter(c -> String.format("<%s>", c.getIRI()).equals(kw.getOwlClass())).findFirst().orElse(null);
                DataProperty foundedDp = ont.getDataProperties().stream().
                        filter(dp -> String.format("%s", dp.getIRI()).equals(kw.getDataProperty())).findFirst().orElse(null);

                if (foundedClass == null || foundedDp == null)
                    continue;

                Condition cond = new Condition(foundedClass, foundedDp, "=", kw.getTextFragment());
                conditions.put(foundedClass, List.of(cond));
            }

            else if (token.getType() == ETokenType.Expression) {
                Expression expr = (Expression) token;
                for (var c : ont.getOWLClasses()) {
                    List<DataProperty> lstDps = ont.getDataProperties().stream().filter(dp -> dp.getDomain().equals(c)).toList();
                    if (lstDps.size() == 0) continue;
                    for (var classDp : lstDps) {
                        var operandType = expr.getOperand().getValueType();
                        if (operandType == EValueType.Decimal && classDp.getRange().toString().equals("xsd:decimal")) {
                            var cond = new Condition(c, classDp, expr.getStatement().getOper(), ((DecimalVariant) expr.getOperand()).getDecimalValue().toString());
                            conditions.put(classDp.getDomain(), new ArrayList<Condition>() { { add (cond);} });
                        }
                        else if (operandType == EValueType.DateRange && classDp.getRange().toString().equals("xsd:date")) {
                            var cond = new Condition(c, classDp, expr.getStatement().getOper(), ((DateRangeVariant) expr.getOperand()).getStart().toString());
                            conditions.put(classDp.getDomain(), new ArrayList<Condition>() { { add (cond);} });
                        }
                    }
                }
            }
        }
        return conditions;
    }

    public String BuildQuery(List<Token> tokens) {
        var select = "SELECT DISTINCT " + BuildSelectClause();

        HashMap<Class, List<Condition>> conditions = TransformTokensToConditions(tokens);
        List<Class> classes = new ArrayList<>(conditions.keySet().stream().toList());

        Class diagnosticReportClass = ont.getOWLClasses().stream().
                filter(c -> c.getIRI().getFragment().equals("DiagnosticReport")).findFirst().orElse(null);
        classes.add(diagnosticReportClass);
        conditions.put(diagnosticReportClass, Arrays.asList(new Condition(diagnosticReportClass,
                        ont.getDataProperties().stream().filter(dp -> dp.getIRI().toString().equals("http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#createdAt")).findFirst().orElse(null)),
                new Condition(diagnosticReportClass,
                        ont.getDataProperties().stream().filter(dp -> dp.getIRI().toString().equals("http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#collectedAt")).findFirst().orElse(null)), new Condition(diagnosticReportClass,
                        ont.getDataProperties().stream().filter(dp -> dp.getIRI().toString().equals("http://www.semanticweb.org/rustam-pc/ontologies/2022/0/biogenom#idDiagnosticReport")).findFirst().orElse(null))));

        List<Condition> filters = new ArrayList<Condition>();
        for (var cond : conditions.values())
            filters.addAll(cond);
        var restrictions =  BuildFilterClause(filters);
        var filter = restrictions.length() > 0? "\nFILTER " + "(" + BuildFilterClause(filters) + ")" : "";

        var where = "\nWHERE {" + BuildWhereClause(classes, conditions) + "\t" + filter + "\n}";

        var orderby = "\nORDER BY " + BuildOrderClause();

        return select + where + orderby;
    }

    private String BuildSelectClause() {
        return "?iddiagnosticreport ?createdat ?collectedat";
    }

    private String BuildWhereClause(List<Class> classes, HashMap<Class, List<Condition>> conditions) {
        if (classes.size() == 0)
            return "";
        if (classes.size() == 1) {
            var classConds = conditions.getOrDefault(classes.get(0), null);
            String conditionWhere = String.format("?%s a <%s>",
                    classes.get(0).getIRI().getFragment().toLowerCase(), classes.get(0).getIRI());

            if (classConds != null) {
                for (var cond : classConds) {
                    conditionWhere += String.format(" ; <%s> ?%s", cond.getDataProperty().getIRI(),
                            cond.getDataPropertyName().toLowerCase());
                }
            }
            return String.format("\n\t%s .", conditionWhere);
        }

        List<ObjectProperty> pathPairs = new ArrayList<>();
        for (int i = 1; i < classes.size(); i++) {
            var curClass = classes.get(0);
            /* найти путь от класса classes.get(0) до classes.get(i), i = 1..class.size() */
            while (!curClass.equals(classes.get(i))) {
                /* если нет пути между классами */
                ObjectProperty link;
                if (classRelations.containsKey(String.format("%s %s", curClass, classes.get(i))))
                    link = classRelations.get(String.format("%s %s", curClass, classes.get(i)));
                else if (classRelations.containsKey(String.format("%s %s", classes.get(i), curClass)))
                    link = classRelations.get(String.format("%s %s", classes.get(i), curClass));
                else
                    return null;
                pathPairs.add(link);
                curClass = link.getSource() == curClass ? link.getTarget() : link.getSource();
            }
        }
        /* убрать повторяющиеся objectProperties */
        pathPairs = pathPairs.stream().distinct().toList();

        StringBuilder whereClause = new StringBuilder();
        for (var owlClass : conditions.keySet()) {
            whereClause.append(String.format("\n\t?%s", owlClass.getIRI().getFragment().toLowerCase()));

            var classConds = conditions.get(owlClass);
            StringBuilder strCond = new StringBuilder();
            for (var cond : classConds) {
                strCond.append(String.format(" <%s> ?%s ;", cond.getDataProperty().getIRI(),
                        cond.getDataPropertyName().toLowerCase()));
            }

            if (strCond.length() > 0)
                strCond = new StringBuilder(strCond.substring(0, strCond.length() - 1));
            whereClause.append(String.format("%s .", strCond));
        }

        for (var objProp : pathPairs) {
            var owlClass = objProp.getSource();
            whereClause.append(String.format("\n\t?%s <%s> ?%s .",
                owlClass.getIRI().getFragment().toLowerCase(), objProp.getIRI(), objProp.getTarget().getIRI().getFragment().toLowerCase()));
        }

        return whereClause.toString();
    }

    private String BuildFilterClause(List<Condition> conditions) {
        StringBuilder filterClause = new StringBuilder();
        String bunch = "&&";
        for (var cond : conditions) {
            if (cond.getOperator().length() > 0 && cond.getValue().length() > 0) {
                String value;
                if (cond.getDataProperty().getRange().toString().equals("rdfs:Literal")
                        || cond.getDataProperty().getRange().toString().equals("xsd:string")
                        || cond.getDataProperty().getRange().toString().equals("xsd:time"))
                    value = String.format("\"%s\"", cond.getValue());
                else
                    value = cond.getValue();
                filterClause.append(String.format(
                        "?%s %s %s %s ", cond.getVariableName(), cond.getOperator(), value, bunch));
            }
        }
        if (filterClause.length() == 0) return "";
        return filterClause.substring(0, filterClause.length() - bunch.length() - 2);
    }


    private String BuildOrderClause()
    {
        return "?createdat ?collectedat";
    }

}
