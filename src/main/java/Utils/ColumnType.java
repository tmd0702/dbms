package Utils;

public enum ColumnType {
    ARRAY(2003, "Array"),
    BIG_INT(-5, "Big int"),
    BINARY(-2, "Binary"),
    BIT(-7, "Bit"),
    BLOB(2004, "Blob"),
    BOOLEAN(16, "Boolean"),
    CHAR(1, "Char"),
    CLOB(2005, "Clob"),
    DATE(91, "Date"),
    DOUBLE(8, "Double"),
    FLOAT(6, "Float"),
    INTEGER(4, "Integer"),
    VARCHAR(12, "Varchar"),
    NCHAR(-15, "Nchar"),
    NVARCHAR(-9, "Nvarchar"),
    NUMERIC(2, "Numeric"),
    TIMESTAMP(93, "Timestamp");



    private final int value;
    private final String description;

    ColumnType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return value + " " + description;
    }
    public static ColumnType getByDescription(String description) {
        for(ColumnType columnType : values()) {
            if(columnType.description == description) return columnType;
        }
        return ColumnType.VARCHAR;
//        throw new IllegalArgumentException("Invalid column type: " + description);
    }
    public static ColumnType getByValue(int value) {
        for(ColumnType columnType : values()) {
            if(columnType.value == value) return columnType;
        }
        return ColumnType.VARCHAR;
//        throw new IllegalArgumentException("Invalid column type: " + value);
    }
}

