import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day5 {

    record Point(int x, int y) {

        private static Point fromString(final String s) {
            final String[] split = s.split(",");

            final int x = Integer.parseInt(split[0].trim());
            final int y = Integer.parseInt(split[1].trim());

            return new Point(x, y);
        }

    }

    private static final class Line {

        private static Line fromString(final String s) {
            final String[] split = s.split("->");

            final Point start = Point.fromString(split[0]);
            final Point end = Point.fromString(split[1]);

            return new Line(start, end);
        }

        private final Point start;
        private final Point end;
        private final List<Point> pointsOnLine;

        private Line(final Point start, final Point end) {
            this.start = start;
            this.end = end;
            this.pointsOnLine = this.calculatePointsOnLine();
        }

        private boolean isHorizontal() {
            return start.y() == end.y();
        }

        private boolean isVertical() {
            return start.x() == end.x();
        }

        private boolean isDiagonal() {
            return Math.abs(start.x - end.x) == Math.abs(start.y - end.y);
        }

        private List<Point> calculatePointsOnLine() {
            final List<Point> points = new ArrayList<>();

            if (this.isVertical()) {
                if (start.y() < end.y()) {
                    for (int i = start.y(); i <= end.y(); i++) {
                        points.add(new Point(start.x(), i));
                    }
                } else {
                    for (int i = end.y(); i <= start.y(); i++) {
                        points.add(new Point(start.x(), i));
                    }
                }
            } else if (this.isHorizontal()) {
                if (start.x() < end.x()) {
                    for (int i = start.x(); i <= end.x(); i++) {
                        points.add(new Point(i, start.y()));
                    }
                } else {
                    for (int i = end.x(); i <= start.x(); i++) {
                        points.add(new Point(i, start.y()));
                    }
                }
            } else if (this.isDiagonal()) {
                final int difference = Math.abs(start.x - end.x);

                if (start.x() < end.x()) {
                    if (start.y() < end.y()) {
                        for (int i = 0; i <= difference; i++) {
                            points.add(new Point(start.x + i, start.y + i));
                        }
                    } else {
                        for (int i = 0; i <= difference; i++) {
                            points.add(new Point(start.x + i, start.y - i));
                        }
                    }
                } else {
                    if (start.y() < end.y()) {
                        for (int i = 0; i <= difference; i++) {
                            points.add(new Point(start.x - i, start.y + i));
                        }
                    } else {
                        for (int i = 0; i <= difference; i++) {
                            points.add(new Point(start.x - i, start.y - i));
                        }
                    }
                }
            }

            return points;
        }

        private Set<Point> getOverlappingPoints(final Line otherLine) {
            final Set<Point> points = new HashSet<>(this.pointsOnLine);

            points.retainAll(otherLine.calculatePointsOnLine());

            return points;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Line line = (Line) o;
            return Objects.equals(start, line.start) && Objects.equals(end, line.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

    private static List<Line> readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day5.class.getResourceAsStream("/day5.txt")))) {
            return reader.lines().map(Line::fromString).toList();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Set<Point> getOverlaps(final List<Line> data) {
        final Set<Point> overlappingPoints = new HashSet<>();

        final List<Line> linesLeft = new ArrayList<>(data);

        for (final Line line : data) {
            for (final Line otherLine : linesLeft) {
                if (!line.equals(otherLine)) {
                    overlappingPoints.addAll(line.getOverlappingPoints(otherLine));
                }
            }

            linesLeft.remove(line);
        }

        return overlappingPoints;
    }

    public static void main(String[] args) {
        final List<Line> data = readData();

        System.out.println(getOverlaps(data).size());
    }
}
