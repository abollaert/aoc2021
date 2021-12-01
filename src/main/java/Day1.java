import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

public final class Day1 {

    private static final int[] readInput() throws IOException  {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Day1.class.getResourceAsStream("day1.txt"))))) {
            return reader.lines()
                         .mapToInt(Integer::parseInt)
                         .toArray();
        }
    }

    private static final int part1(final int[] data) {
        int numDeeper = 0;
        int current = Integer.MAX_VALUE;

        for (final int depth : data) {
            if (depth > current) {
                numDeeper++;
            }

            current = depth;
        }

        return numDeeper;
    }

    private static final int part2(final int[] data) {
        int startIndex = 0;
        int endIndex = 3;

        int[] window = null;
        int currentSum = Integer.MAX_VALUE;
        int numLarger = 0;

        while (endIndex <= data.length) {
            window = Arrays.copyOfRange(data, startIndex++, endIndex++);

            final int sum = window[0] + window[1] + window[2];

            if (sum > currentSum) {
                numLarger++;
            }

            currentSum = sum;
        }

        return numLarger;
    }

    public static void main(String[] args) throws IOException {
        final int[] data = readInput();

        System.out.println(String.format("Part 1 : [%s], Part 2 : [%s]",
                                         part1(data),
                                         part2(data)));
    }
}
