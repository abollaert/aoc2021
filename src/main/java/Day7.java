import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day7 {

    private static final int[] readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day7.class.getResourceAsStream("/day7.txt")))) {
            return Arrays.stream(reader.readLine().split(","))
                         .mapToInt(Integer::parseInt)
                         .toArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final int align(int[] positions, final Function<Integer, Integer> fuelCostFunction) {
        final IntStream stream = IntStream.of(positions);

        final int min = IntStream.of(positions).min().orElse(0);
        final int max = IntStream.of(positions).max().orElse(0);

        int minFuel = Integer.MAX_VALUE;

        for (int i = min; i < max; i++) {
            int fuelUsed = 0;

            for (int j = 0; j < positions.length; j++) {
                fuelUsed += fuelCostFunction.apply(Math.abs(positions[j] - i));
            }

            if (fuelUsed < minFuel) {
                minFuel = fuelUsed;
            }
        }

        return minFuel;
    }

    public static void main(String[] args) {
        final int[] data = readData();

        System.out.println(String.format("Part 1 : min fuel : [%d]", align(data, distance -> distance)));
        System.out.println(String.format("Part 2 : min fuel : [%d]", align(data, distance -> (distance * (distance + 1)) / 2)));

    }


}
