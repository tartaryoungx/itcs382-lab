package bikeshare;

import java.util.Objects;

public final class Bike {
    private final String id;
    private Station station;
    private String memberId;
    private MemberType memberType;

    public Bike(String id, Station station) {
        this.id = Objects.requireNonNull(id);
        this.station = Objects.requireNonNull(station);
    }

    public String id() {
        return id;
    }

    public boolean isAvailableAt(Station location) {
        return memberId == null && station == location;
    }

    public void rent(String renterId, MemberType type) {
        if (memberId != null) {
            throw new IllegalArgumentException("That bike is already rented.");
        }
        if (renterId == null || renterId.isBlank()) {
            throw new IllegalArgumentException("Member ID is required.");
        }
        memberType = Objects.requireNonNull(type);
        memberId = renterId;
    }

    public Ride quoteReturn(int minutes, Station destination) {
        if (memberId == null) {
            throw new IllegalArgumentException("That bike is not rented.");
        }
        Objects.requireNonNull(destination);
        return new Ride(id, memberId, minutes, memberType.fee(minutes), destination);
    }

    public void finishReturn(Station destination) {
        if (memberId == null) {
            throw new IllegalArgumentException("That bike is not rented.");
        }
        station = Objects.requireNonNull(destination);
        memberId = null;
        memberType = null;
    }
}
