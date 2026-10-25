package bikeshare;

public enum MemberType {
    STUDENT(2), STAFF(3);

    private final int rate;

    MemberType(int rate) {
        this.rate = rate;
    }

    public long fee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes must be non-negative.");
        }
        return (long) minutes * rate;
    }
}
