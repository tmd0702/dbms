package Utils;

public enum Role {
    UNKNOWN(0, "UNKNOWN"),
    MANAGER(1, "Manager"),
    CUSTOMER(2, "Customer");

    private final int value;
    private final String description;

    Role(int value, String description) {
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

    public static Role getByValue(int value) {
        for(Role role : values()) {
            if(role.value == value) return role;
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}
