import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Day9 {

    record Point(int row, int col) {}

    private static record HeatMap(int[][] map) {

        private static HeatMap parse(final List<String> lines) {
            final int rows = lines.size();
            final int cols = lines.get(0).length();

            final int[][] map = new int[rows][];

            for (int i = 0; i < lines.size(); i++) {
                final String line = lines.get(i);
                final int[] rowData = new int[line.length()];

                for (int j = 0; j < line.length(); j++) {
                    rowData[j] = Integer.parseInt(String.valueOf(line.charAt(j)));
                }

                map[i] = rowData;
            }

            return new HeatMap(map);
        }

        public final String toString() {
            final StringBuilder builder = new StringBuilder();

            for (int row = 0; row < this.map.length; row++) {
                for (int col = 0; col < this.map[row].length; col++) {
                    builder.append(this.map[row][col] + " ");
                }

                builder.append("\n");
            }

            return builder.toString();
        }

        private final boolean isLowPoint(final Point p) {
            final int height = this.getHeight(p);

            for (final Point neighbor : this.neighbors(p)) {
                if (this.getHeight(neighbor) <= height) {
                    return false;
                }
            }
            return true;
        }

        private final List<Point> findLowPoints() {
            final List<Point> lowPoints = new ArrayList<>();

            for (int row = 0; row < this.map.length; row++) {
                for (int col = 0; col < this.map[row].length; col++) {
                    final Point p = new Point(row, col);

                    if (this.isLowPoint(p)) {
                        lowPoints.add(p);
                    }
                }
            }

            return lowPoints;
        }

        private final int getHeight(final Point point) {
            return this.map[point.row()][point.col()];
        }

        private final List<Point> neighbors(final Point p) {
            final List<Point> neighbors = new ArrayList<>();

            if (p.row > 0) {
                neighbors.add(new Point(p.row - 1, p.col));
            }

            if (p.col > 0) {
                neighbors.add(new Point(p.row , p.col - 1));
            }

            if (p.row < this.map.length - 1) {
                neighbors.add(new Point(p.row + 1, p.col));
            }

            if (p.col < this.map[p.row].length - 1) {
                neighbors.add(new Point(p.row, p.col + 1));
            }

            return neighbors;
        }

        private final void findAdjacentBasinPoints(final Point p, final List<Point> basin) {
            for (final Point neighbor : this.neighbors(p)) {
                if (this.getHeight(neighbor) != 9 && this.getHeight(neighbor) >= this.getHeight(p)) {
                    if (!basin.contains(neighbor)) {
                        basin.add(neighbor);
                        findAdjacentBasinPoints(neighbor, basin);
                    }
                }
            }
        }
    }

    private static final HeatMap readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day9.class.getResourceAsStream("/day9.txt")))) {
            return HeatMap.parse(reader.lines().toList());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int calculateRisk(final int height) {
        return height + 1;
    }

    private static int sumRiskLevel(final HeatMap map, final List<Point> lowPoints) {
        return lowPoints.stream()
                        .mapToInt(map::getHeight)
                        .map(Day9::calculateRisk)
                        .sum();
    }

    public static void main(String[] args) {
        final HeatMap map = readData();
        final List<Point> lowPoints = map.findLowPoints();

        final List<Integer> basinSizes = new ArrayList<>();

        for (Point p : lowPoints) {
            final List<Point> basin = new ArrayList<>();
            basin.add(p);

            map.findAdjacentBasinPoints(p, basin);

            basinSizes.add(basin.size());
        }

        Collections.sort(basinSizes, Comparator.reverseOrder());

        System.out.println(basinSizes);

        System.out.println(basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));
    }
}
