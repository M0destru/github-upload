package token;

public class Expression extends Token {
    private Variant operand;
    private Statement statement;

    public Expression(String textFragment, Variant operand, Statement statement) {
        super(textFragment);
        this.type = ETokenType.Expression;
        this.operand = operand;
        this.statement = statement;
    }

    public Variant getOperand() {
        return operand;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("\nStatement: %s\nOperand: %s", statement, operand);
    }
}
