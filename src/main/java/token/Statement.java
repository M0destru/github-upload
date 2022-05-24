package token;

public class Statement extends Token {
    private String oper;
    private Boolean isNegation;

    public Statement(String textFragment, String oper, Boolean isNegation) {
        super(textFragment);
        type = ETokenType.Statement;
        this.oper = oper;
        this.isNegation = isNegation;
    }

    public String getOper() {
        return oper;
    }

    @Override
    public String toString() {
        return oper;
    }

}
