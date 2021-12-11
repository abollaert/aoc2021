import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Day10 {

    private static record SymbolMapping(char start,
                                        char end,
                                        int score,
                                        int completionScore) {}

    private static final Map<Character, SymbolMapping> MAPPING_TABLE = new HashMap<>();

    static {
        MAPPING_TABLE.put('{', new SymbolMapping('{', '}', 1197, 3));
        MAPPING_TABLE.put('}', new SymbolMapping('{', '}', 1197, 3));
        MAPPING_TABLE.put('[', new SymbolMapping('[', ']', 57, 2));
        MAPPING_TABLE.put(']', new SymbolMapping('[', ']', 57, 2));
        MAPPING_TABLE.put('<', new SymbolMapping('<', '>', 25137, 4));
        MAPPING_TABLE.put('>', new SymbolMapping('<', '>', 25137, 4));
        MAPPING_TABLE.put('(', new SymbolMapping('(', ')', 3, 1));
        MAPPING_TABLE.put(')', new SymbolMapping('(', ')', 3, 1));
    }

    private static final class LineParser {

        private enum ResultCode {
            OK,
            INCOMPLETE,
            SYNTAX_ERROR;
        }

        private static record Result(ResultCode code,
                                     Character failingCharacter,
                                     String completion) {}

        private static Result parse(final String line) {
            final Stack<Character> characterStack = new Stack<>();
            final char[] chars = line.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                switch (chars[i]) {
                    case '{', '[', '<', '(' -> characterStack.push(chars[i]);
                    case '}', ']', '>', ')' -> {
                        final char previous = characterStack.pop();
                        final SymbolMapping mapping = MAPPING_TABLE.get(chars[i]);

                        if (previous != mapping.start()) {
                            return new Result(ResultCode.SYNTAX_ERROR, chars[i], null);
                        }
                    }
                }
            }

            if (characterStack.isEmpty()) {
                return new Result(ResultCode.OK, null, null);
            } else {
                final StringBuilder completionBuilder = new StringBuilder();

                while (!characterStack.isEmpty()) {
                    final char startChar = characterStack.pop();
                    completionBuilder.append(MAPPING_TABLE.get(startChar).end());
                }

                return new Result(ResultCode.INCOMPLETE, null, completionBuilder.toString());
            }
        }
    }

    private static final List<String> readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day10.class.getResourceAsStream("/day10.txt")))) {
            return reader.lines().toList();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final void part1(final List<String> data) {
        int score = 0;
        final List<String> completions = new ArrayList<>();

        for (final String line : data) {
            final LineParser.Result result = LineParser.parse(line);

            System.out.println(String.format("Line [%s], result [%s]", line, result));

            if (result.code == LineParser.ResultCode.SYNTAX_ERROR) {
                score += MAPPING_TABLE.get(result.failingCharacter).score;
            } else if (result.code == LineParser.ResultCode.INCOMPLETE) {
                completions.add(result.completion);
            }
        }

        System.out.println(String.format("Score is [%s]", score));

        final List<Long> completionScores = new ArrayList<>();

        for (final String completion : completions) {
            completionScores.add(scoreCompletion(completion));
        }

        Collections.sort(completionScores);
        System.out.println(completionScores);

        System.out.println(String.format("Completion score : [%s]", completionScores.get(completionScores.size() / 2)));
    }

    private static final long scoreCompletion(final String completion) {
        long score = 0;

        for (char c : completion.toCharArray()) {
            score = score * 5;
            score += MAPPING_TABLE.get(c).completionScore;
        }

        return score;
    }

    public static void main(String[] args) {
        final List<String> data = readData();

        part1(data);
    }
}
