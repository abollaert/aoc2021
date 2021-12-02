import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class Day2 {

    private enum Direction {
        FORWARD,
        DOWN,
        UP;

        private static Direction forValue(final String value) {
            for (final Direction direction : Direction.values()) {
                if (direction.name().equalsIgnoreCase(value)) {
                    return direction;
                }
            }

            return null;
        }
    }

    private record Move(Direction direction,
                        int amount) {

        private static Move parse(final String line) {
            final String[] splitLine = line.split(" ");
            final Direction direction = Direction.forValue(splitLine[0]);
            final int amount = Integer.parseInt(splitLine[1]);

            return new Move(direction, amount);
        }
    }

    private static List<Move> readInput() throws IOException {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Day1.class.getResourceAsStream("day2.txt"))))) {
            return reader.lines()
                         .map(Move::parse)
                         .toList();
        }
    }

    private static final void calculateCourse(final List<Move> data) {
        int horizontalPosition = 0;
        int depth = 0;

        for (final Move move : data) {
            switch (move.direction) {
                case UP -> depth = depth - move.amount;
                case DOWN -> depth = depth + move.amount;
                case FORWARD -> horizontalPosition = horizontalPosition + move.amount;
            }
         }

        System.out.println(String.format("Horizontal position : [%s], depth : [%s], multiplied : [%s]", horizontalPosition, depth, horizontalPosition * depth));
    }

    private static final void calculateCourseWithAim(final List<Move> data) {
        int horizontalPosition = 0;
        int depth = 0;
        int aim = 0;

        for (final Move move : data) {
            switch (move.direction) {
                case UP -> aim = aim - move.amount;
                case DOWN -> aim = aim + move.amount;
                case FORWARD -> {
                    horizontalPosition = horizontalPosition + move.amount;
                    depth = depth + (move.amount * aim);
                }
            }
        }

        System.out.println(String.format("Horizontal position : [%s], aim: [%s], depth : [%s], multiplied : [%s]", horizontalPosition, aim, depth, horizontalPosition * depth));
    }

    public static void main(String[] args) throws IOException {
        final List<Move> data = readInput();

        calculateCourse(data);
        calculateCourseWithAim(data);
    }

}
