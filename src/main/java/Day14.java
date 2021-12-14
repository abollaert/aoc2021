import javax.swing.plaf.metal.MetalInternalFrameTitlePane;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {

    private static record Input(String template, Map<String, String> rules) {}
    private static record IterationData(Map<String, Long> pairs, Map<Character, Long> letters) {}

    private static final Input readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day14.class.getResourceAsStream("/day14.txt")))) {
            final String template = reader.readLine();

            reader.readLine();

            final Map<String, String> rules = new HashMap<>();

            String rule = reader.readLine();

            final Pattern pattern = Pattern.compile("([A-Z]+) -> ([A-Z+])");

            while (rule != null) {
                final Matcher matcher = pattern.matcher(rule);

                if (matcher.matches()) {
                    final String input = matcher.group(1);
                    final String output = matcher.group(2);

                    rules.put(input, output);
                }

                rule = reader.readLine();
            }

            return new Input(template, rules);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static IterationData iterate(final IterationData iterationData, final Map<String, String> rules) {
        final Map<String, Long> newPairs = new HashMap<>();
        final Map<Character, Long> newCount = new HashMap<>();

        newCount.putAll(iterationData.letters);

        for (final String p : iterationData.pairs.keySet()) {
            final long number = iterationData.pairs.get(p);

            if (number > 0) {
                final String letter = rules.get(p);

                final String newPair1 = p.charAt(0) + letter;
                final String newPair2 = letter + p.charAt(1);

                newPairs.put(newPair1, newPairs.computeIfAbsent(newPair1, k -> 0l) + number);
                newPairs.put(newPair2, newPairs.computeIfAbsent(newPair2, k -> 0l) + number);

                newCount.put(letter.charAt(0), newCount.computeIfAbsent(letter.charAt(0), k -> 0l) + number);
            }
        }

        return new IterationData(newPairs, newCount);
    }

    public static void main(String[] args) {
        final Input input = readData();

        final Map<String, Long> pairs = splitInPairs(input.template);
        final Map<Character, Long> letterCount = countElements(input.template);

        IterationData data = new IterationData(pairs, letterCount);

        for (int i = 0; i < 40; i++) {
            data = iterate(data, input.rules);
        }

        System.out.println(findMostCommon(data.letters) - findLeastCommon(data.letters));
    }

    private static final Map<Character, Long> countElements(final String polymer) {
        final Map<Character, Long> frequencies = new HashMap<>();

        for (final char c : polymer.toCharArray()) {
            frequencies.put(c, frequencies.computeIfAbsent(c, k -> 0l) + 1);
        }

        return frequencies;
    }

    private static final Map<String, Long> splitInPairs(final String polymer) {
        final Map<String, Long> pairs = new HashMap<>();

        int windowStart = 0;

        while (windowStart + 2 <= polymer.length()) {
            final String pair = polymer.substring(windowStart, windowStart + 2);

            pairs.put(pair, pairs.computeIfAbsent(pair, k -> 0l) + 1);

            windowStart++;
        }

        return pairs;
    }

    private static final long findMostCommon(final Map<Character, Long> frequencies) {
        return frequencies.values().stream().mapToLong(Long::longValue).max().orElse(0);
    }

    private static final long findLeastCommon(final Map<Character, Long> frequencies) {
        return frequencies.values().stream().mapToLong(Long::longValue).min().orElse(0);
    }
}
