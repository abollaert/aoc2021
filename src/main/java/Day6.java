import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 {



    private static Map<Integer, Long> readData() {
        final Map<Integer, Long> population = new HashMap<>();

        for (int i = 0; i <= 8; i++) {
            population.put(i, 0l);
        }

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day6.class.getResourceAsStream("/day6.txt")))) {
            final String line = reader.readLine();

            final List<Integer> timers = Arrays.stream(line.split(","))
                                               .map(Integer::parseInt)
                                               .toList();

            for (final Integer timer : timers) {
                population.put(timer, population.get(timer) + 1);
            }

            return population;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Map<Integer, Long> simulate(final int days, final Map<Integer, Long> initialPopulation) {
        Map<Integer, Long> population = initialPopulation;

        for (int i = 0; i < days; i++) {
            final Map<Integer, Long> newPopulation = new HashMap<>();

            newPopulation.put(8, population.get(0));

            for (int j = 7; j >= 0; j--) {
                newPopulation.put(j, population.get(j + 1));
            }

            newPopulation.put(6, newPopulation.get(6) + population.get(0));

            population = newPopulation;
        }

        return population;
    }

    public static void main(String[] args) {
        final Map<Integer, Long> population = readData();

        final Map<Integer, Long> finalPopulation = simulate(256, population);

        long sum = 0;

        for (long number : finalPopulation.values()) {
            sum += number;
        }

        System.out.println(sum);
    }
}
