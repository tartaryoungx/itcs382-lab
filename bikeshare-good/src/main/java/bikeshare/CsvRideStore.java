package bikeshare;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class CsvRideStore implements RideStore {
    private final Path path;

    public CsvRideStore(Path path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public void save(Ride ride) throws IOException {
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(quote(ride.bikeId()) + "," + quote(ride.memberId()) + ","
                    + ride.minutes() + "," + ride.fee() + "," + ride.destination().name());
            writer.newLine();
        }
    }

    private static String quote(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
