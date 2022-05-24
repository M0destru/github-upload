package token;

import java.time.LocalDate;

public class DateRangeVariant extends Variant {
    private LocalDate start;
    private LocalDate end;

    public DateRangeVariant(LocalDate start, LocalDate end) {
        this.valueType = EValueType.DateRange;
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    @Override
    public String toString() {
        return String.format("(%s)", super.toString() + ", "
                + String.format("Start: %s, End: %s", start.toString(), end.toString()));
    }
}
