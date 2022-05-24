package token;

public class KeyWord extends Token {
    private final int id;
    private final String owlclass; // ?Class
    private final String dataProperty; // ?DataProperty
    private final String initialForm;

    public String getDataProperty () {
        return dataProperty;
    }

    public String getOwlClass() {
        return owlclass;
    }

    public String getInitialForm() {
        return initialForm;
    }

    public KeyWord(String textFragment, int id, String owlclass,
                   String dataProperty, String initialForm) {
        super(textFragment);
        this.type = ETokenType.Keyword;
        this.id = id;
        this.owlclass = owlclass;
        this.dataProperty = dataProperty;
        this.initialForm = initialForm;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nId: %d\nOWL Class: %s\nData Property: %s\nInitial Form: %s",
                id, owlclass, dataProperty, initialForm);
    }
}
