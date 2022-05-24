package token;

import java.math.BigDecimal;

public class DecimalVariant extends Variant {
    private BigDecimal decimalValue;

    public DecimalVariant (BigDecimal decimalValue) {
        this.valueType = EValueType.Decimal;
        this.decimalValue = decimalValue;
    }

    public BigDecimal getDecimalValue() {
        return decimalValue;
    }

    @Override
    public String toString() {
        return String.format("(%s)", super.toString() + ", "
                + String.format("Value: %s", decimalValue));
    }
}

