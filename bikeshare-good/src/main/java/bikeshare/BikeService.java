package bikeshare;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class BikeService {
    private final Map<String, Bike> bikes = new LinkedHashMap<>();
    private final RideStore store;

    public BikeService(RideStore store) {
        this.store = Objects.requireNonNull(store);
        int number = 1;
        for (Station station : Station.values()) {
            for (int i = 0; i < 2; i++) {
                String id = "B" + number++;
                bikes.put(id, new Bike(id, station));
            }
        }
    }

    public String rent(String memberId, MemberType type, Station station) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(station);
        for (Bike bike : bikes.values()) {
            if (bike.isAvailableAt(station)) {
                bike.rent(memberId, type);
                return bike.id();
            }
        }
        throw new IllegalArgumentException("No bikes available at " + station);
    }

    public Ride returnBike(String bikeId, int minutes, Station destination) throws IOException {
        Bike bike = bikes.get(bikeId);
        if (bike == null) {
            throw new IllegalArgumentException("Unknown bike: " + bikeId);
        }
        Ride ride = bike.quoteReturn(minutes, destination);
        store.save(ride);
        bike.finishReturn(destination);
        return ride;
    }
}
