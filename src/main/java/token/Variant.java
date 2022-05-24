package token;

public abstract class Variant {
    protected EValueType valueType;

    public EValueType getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return String.format("Value Type: %s", valueType);
    }
}
