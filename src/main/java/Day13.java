import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {

    private record FoldInstruction(Orientation orientation, int rowCol) {
        enum Orientation {
            HORIZONTAL,
            VERTICAL
        }
    }

    private record Instructions(Sheet sheet,
                                List<FoldInstruction> folds) {
    }

    private record Sheet(List<BitSet> data,
                         int width) {}

    private static Sheet foldHorizontal(final Sheet sheet, final int y) {
        final List<BitSet> upperHalf = sheet.data.subList(0, y);
        final List<BitSet> lowerHalf = sheet.data.subList(y + 1, sheet.data.size());

        Collections.reverse(lowerHalf);

        final List<BitSet> result = new ArrayList<>();

        if (upperHalf.size() >= lowerHalf.size()) {
            final int sizeDifference = upperHalf.size() - lowerHalf.size();

            for (int i = 0; i < sizeDifference; i++) {
                result.add(upperHalf.get(i));
            }

            for (int i = sizeDifference; i < upperHalf.size(); i++) {
                final BitSet bits = upperHalf.get(i);
                bits.or(lowerHalf.get(i - sizeDifference));

                result.add(bits);
            }
        } else {
            final int sizeDifference = lowerHalf.size() - upperHalf.size();

            for (int i = 0; i < sizeDifference; i++) {
                result.add(lowerHalf.get(i));
            }

            for (int i = (sizeDifference); i < lowerHalf.size(); i++) {
                final BitSet bits = lowerHalf.get(i);
                bits.or(upperHalf.get(i - sizeDifference));

                result.add(bits);
            }
        }

        return new Sheet(result, sheet.width);
    }

    private static Sheet rotate(final Sheet data) {
        final int width = data.width;
        final int length = data.data.size();

        final List<BitSet> rotated = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            final BitSet set = new BitSet(length);

            for (int j = 0; j < length; j++) {
                final BitSet current = data.data.get(j);

                if (current.get(i)) {
                    set.set(j);
                }
            }

            rotated.add(set);
        }

        return new Sheet(rotated, length);
    }

    private static Sheet foldVertical(final Sheet sheet, final int x) {
        return rotate(foldHorizontal(rotate(sheet), x));
    }

    private record Coordinate(int x, int y) {};

    private static Instructions readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day13.class.getResourceAsStream("/day13.txt")))) {
            String line = reader.readLine();

            final List<Coordinate> coordinates = new ArrayList<>();
            final List<FoldInstruction> folds = new ArrayList<>();

            while (line != null) {
                while (!line.trim().equals("")) {
                    final String[] position = line.split(",");

                    coordinates.add(new Coordinate(Integer.parseInt(position[0]), Integer.parseInt(position[1])));

                    line = reader.readLine();
                }

                final Pattern pattern = Pattern.compile("fold along ([xy])=([0-9]+)");

                while (line != null) {
                    if (!line.trim().equals("")) {
                        if (line.startsWith("fold along")) {
                            final Matcher matcher = pattern.matcher(line);

                            if (matcher.find()) {
                                final String parameter = matcher.group(1);
                                final int location = Integer.parseInt(matcher.group(2));

                                if (parameter.equals("x")) {
                                    folds.add(new FoldInstruction(FoldInstruction.Orientation.VERTICAL, location));
                                } else {
                                    folds.add(new FoldInstruction(FoldInstruction.Orientation.HORIZONTAL, location));
                                }
                            }
                        }
                    }

                    line = reader.readLine();
                 }
            }

            final int maxHorizontal = coordinates.stream()
                                                 .mapToInt(Coordinate::x)
                                                 .max()
                                                 .orElse(0);

            final int maxVertical = coordinates.stream()
                                               .mapToInt(Coordinate::y)
                                               .max()
                                               .orElse(0);

            final List<BitSet> sheet = new ArrayList<>(maxVertical + 1);

            for (int i = 0; i <= maxVertical; i++) {
                sheet.add(new BitSet(maxHorizontal + 1));
            }

            for (final Coordinate coordinate : coordinates) {
                sheet.get(coordinate.y).set(coordinate.x);
            }

            return new Instructions(new Sheet(sheet, maxHorizontal + 1), folds);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int countDots(final List<BitSet> data) {
        int dots = 0;

        for (final BitSet bits : data) {
            for (int i = 0; i < bits.size(); i++) {
                if (bits.get(i)) {
                    dots += 1;
                }
            }
        }

        return dots;
    }

    private static void printCode(final Sheet sheet) {
        for (int i = 0; i < sheet.data.size(); i++) {
            final BitSet set = sheet.data().get(i);

            for (int j = 0; j < sheet.width; j++) {
                if (set.get(j)) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }

            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        final Instructions data = readData();

        final FoldInstruction firstFold = data.folds().get(0);

        Sheet sheet = data.sheet;

        for (final FoldInstruction fold : data.folds) {
            sheet = switch (fold.orientation) {
                case HORIZONTAL -> foldHorizontal(sheet, fold.rowCol);
                case VERTICAL -> foldVertical(sheet, fold.rowCol);
            };

            System.out.println(countDots(sheet.data));
        }

        printCode(sheet);
    }
}
