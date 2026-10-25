package bikeshare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;

public final class Main {
    private final BikeService service;

    private Main(BikeService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        Main menu = new Main(new BikeService(new CsvRideStore(Path.of("rides.csv"))));
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            menu.run(input);
        } catch (IOException e) {
            System.err.println("Input error: " + e.getMessage());
        }
    }

    private void run(BufferedReader input) throws IOException {
        System.out.println("rent <memberId> <student|staff> <station>");
        System.out.println("return <bike B1-B6> <minutes> <station>; quit");
        System.out.println("Stations: Library, Canteen, Dorm");
        String line;
        while ((line = input.readLine()) != null) {
            if (line.trim().equals("quit")) {
                break;
            }
            if (!line.isBlank()) {
                execute(line);
            }
        }
    }

    private void execute(String line) {
        try {
            String[] words = line.trim().split("\\s+");
            if (words.length != 4) {
                throw new IllegalArgumentException("Expected a command and three arguments.");
            }
            Station station = Station.valueOf(words[3].toUpperCase(Locale.ROOT));
            switch (words[0]) {
                case "rent" -> rent(words[1], words[2], station);
                case "return" -> returnBike(words[1], words[2], station);
                default -> System.out.println("Unknown command.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ride not saved; bike is still rented. Retry return: " + e.getMessage());
        }
    }

    private void rent(String memberId, String type, Station station) {
        MemberType memberType = MemberType.valueOf(type.toUpperCase(Locale.ROOT));
        String bikeId = service.rent(memberId, memberType, station);
        System.out.println("Rented " + bikeId + " to " + memberId + " at " + station);
    }

    private void returnBike(String bikeId, String minutes, Station station) throws IOException {
        Ride ride = service.returnBike(bikeId, Integer.parseInt(minutes), station);
        System.out.println("Receipt: " + ride.bikeId() + ", " + ride.minutes()
                + " minutes, " + ride.fee() + " baht");
    }
}
