package bikeshare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {
    // Parallel arrays: index 0 is bike B1, index 1 is B2, and so on.
    // A null location means that the bike is currently rented.
    public static String[] locations = {"Library", "Library", "Canteen", "Canteen", "Dorm", "Dorm"};
    public static String[] members = new String[6];
    public static int[] memberTypes = new int[6]; // 1 = student, 2 = staff

    public static void main(String[] args) {
        System.out.println("rent <memberId> <student|staff> <station>");
        System.out.println("return <bike B1-B6> <minutes> <station>; quit");
        System.out.println("Stations: Library, Canteen, Dorm");
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] words = line.trim().split("\\s+");
                if (words.length == 1 && words[0].equals("quit")) {
                    break;
                }
                try {
                    if (words[0].equals("rent")) {
                        process(words, true);
                    } else if (words[0].equals("return")) {
                        process(words, false);
                    } else {
                        System.out.println("Unknown command.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Ride not saved; bike is still rented. Retry return: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Input error: " + e.getMessage());
        }
    }

    public static void process(String[] words, boolean renting) throws IOException {
        if (words.length != 4) {
            throw new IllegalArgumentException("Expected three arguments after the command.");
        }
        String station = words[3];
        if (!station.equals("Library") && !station.equals("Canteen") && !station.equals("Dorm")) {
            throw new IllegalArgumentException("Station must be Library, Canteen or Dorm.");
        }
        if (renting) {
            int type;
            if (words[2].equals("student")) {
                type = 1;
            } else if (words[2].equals("staff")) {
                type = 2;
            } else {
                throw new IllegalArgumentException("Member type must be student or staff.");
            }
            for (int i = 0; i < locations.length; i++) {
                if (station.equals(locations[i])) {
                    locations[i] = null;
                    members[i] = words[1];
                    memberTypes[i] = type;
                    System.out.println("Rented B" + (i + 1) + " to " + members[i] + " at " + station);
                    return;
                }
            }
            System.out.println("No bikes available at " + station);
        } else {
            if (!words[1].matches("B[1-6]")) {
                throw new IllegalArgumentException("Bike must be B1 through B6.");
            }
            int bike = words[1].charAt(1) - '1';
            int minutes = Integer.parseInt(words[2]);
            if (minutes < 0) {
                throw new IllegalArgumentException("Minutes must be non-negative.");
            }
            if (locations[bike] != null) {
                throw new IllegalArgumentException("That bike is not rented.");
            }
            long fee = minutes * (memberTypes[bike] == 1 ? 2L : 3L);
            // Append before releasing the bike so a failed save can be retried.
            try (var writer = Files.newBufferedWriter(Path.of("rides.csv"), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(words[1] + "," + minutes + "," + fee + "," + station);
                writer.newLine();
            }
            locations[bike] = station;
            members[bike] = null;
            memberTypes[bike] = 0;
            System.out.println("Receipt: " + words[1] + ", " + minutes + " minutes, " + fee + " baht");
        }
    }
}
