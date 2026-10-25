package bikeshare;

import java.io.IOException;

public interface RideStore {
    void save(Ride ride) throws IOException;
}
