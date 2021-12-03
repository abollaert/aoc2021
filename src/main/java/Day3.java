import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Day3 {

    private static final List<boolean[]> readData()  {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day3.class.getResourceAsStream("/day3.txt")))) {
            return reader.lines()
                         .map(Day3::parse)
                         .toList();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final boolean[] parse(final String line) {
        final boolean[] data = new boolean[line.length()];
        int index = 0;

        for (final char character : line.toCharArray()) {
            if (character == '0') {
                data[index++] = false;
            } else {
                data[index++] = true;
            }
        }

        return data;
    }

    private static final boolean[] gamma(final List<boolean[]> data) {
        return determinePattern(data, true);
    }

    private static final boolean[] epsilon(final List<boolean[]> data) {
        return determinePattern(data, false);
    }

    private static final boolean[] determinePattern(final List<boolean[]> data,
                                                    final boolean gamma) {
        final int length = data.get(0).length;
        final boolean[] result = new boolean[length];

        for (int i = 0; i < length; i++) {
            int numTrue = 0;
            int numFalse = 0;

            for (final boolean[] dataPoint : data) {
                if (dataPoint[i]) {
                    numTrue++;
                } else {
                    numFalse++;
                }
            }

            if (numTrue > numFalse) {
                result[i] = gamma;
            } else if (numTrue == numFalse) {
                result[i] = gamma;
            } else {
                result[i] = !gamma;
            }
        }

        return result;
    }

    private static final long toLong(final boolean[] data) {
        final int length = data.length;
        long result = 0;

        for (int i = 0; i < data.length; i++) {
            result += ((data[i] ? 1l : 0l) << (data.length - 1 - i));
        }

        return result;
    }

    private static final void part1(final List<boolean[]> data) {
        final boolean[] gamma = gamma(data);
        final boolean[] epsilon = epsilon(data);

        final long gammaLong = toLong(gamma);
        final long epsilonLong = toLong(epsilon);
        final long result = gammaLong * epsilonLong;

        System.out.println(String.format("Gamma : [%s], Epsilon : [%s], multiplied [%s]",
                                         gammaLong,
                                         epsilonLong,
                                         result));
    }

    private static boolean[] selectMatching(final List<boolean[]> data,
                                            final Function<List<boolean[]>, boolean[]> patternFunction) {
        List<boolean[]> candidates = data;
        boolean[] pattern = patternFunction.apply(data);

        final int length = data.get(0).length;

        for (int i = 0; i < length; i++) {
            final List<boolean[]> filtered = new ArrayList<>();

            for (final boolean[] candidate : candidates) {
                if (candidate[i] == pattern[i]) {
                    filtered.add(candidate);
                }
            }

            candidates = filtered;
            pattern = patternFunction.apply(filtered);

            if (filtered.size() == 1) {
                return filtered.get(0);
            }
        }

        return null;
    }

    private static final void part2(final List<boolean[]> data) {
        final boolean[] oxygen = selectMatching(data, Day3::gamma);
        final boolean[] scrubbing = selectMatching(data, Day3::epsilon);

        final long oxygenRating = toLong(oxygen);
        final long scrubRating = toLong(scrubbing);

        final long value = oxygenRating * scrubRating;

        System.out.println(String.format("Oxygen [%s], scrubbing [%s], result [%s]",
                                         oxygenRating,
                                         scrubRating,
                                         value));
    }

    public static void main(String[] args) {
        final List<boolean[]> data = readData();

        part1(data);
        part2(data);
    }
}
