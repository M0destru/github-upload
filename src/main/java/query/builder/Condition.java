package query.builder;

import model.Class;
import model.DataProperty;

public class Condition {
    private String bunch;
    private Class owlClass;
    private DataProperty dataProperty;
    private String operator;
    private String value;

    public Condition() {
    }

    public Condition(Class owlClass, DataProperty dataProperty) {
        this.bunch = "";
        this.owlClass = owlClass;
        this.dataProperty = dataProperty;
        this.operator = "";
        this.value = "";
    }

    public Condition(Class owlClass, DataProperty dataProperty, String operator, String value) {
        this.bunch = "";
        this.owlClass = owlClass;
        this.dataProperty = dataProperty;
        this.operator = operator;
        this.value = value;
    }

    public Condition(String bunch, Class owlClass, DataProperty dataProperty, String operator, String value) {
        this.bunch = bunch;
        this.owlClass = owlClass;
        this.dataProperty = dataProperty;
        this.operator = operator;
        this.value = value;
    }

    public Class getOwlClass() {
        return owlClass;
    }

    public DataProperty getDataProperty() {
        return dataProperty;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public String getVariableName() {
        return dataProperty.getIRI().getFragment().toLowerCase();
    }

    public String getDataPropertyName() {
        return dataProperty.getIRI().getFragment().toLowerCase();
    }
}
