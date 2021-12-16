import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Day15 {

    private static Grid grid;
    private static Point start;
    private static Point end;

    private final record Point(int row, int col) {}

    private static record Grid(int[][] costs) {

        public final String toString() {
            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < this.costs.length; i++) {
                for (int j = 0; j < this.costs[i].length; j++){
                    builder.append(this.costs[i][j]).append(" ");
                }

                builder.append("\n");
            }

            return builder.toString();
        }

        private Set<Point> neighbors(final Point p) {
            final Set<Point> neighbors = new HashSet<>();

            if (p.row > 0) {
                neighbors.add(new Point(p.row - 1, p.col));
            }

            if (p.row < this.costs.length - 1) {
                neighbors.add(new Point(p.row + 1, p.col));
            }

            if (p.col > 0) {
                neighbors.add(new Point(p.row, p.col - 1));
            }

            if (p.col < this.costs[0].length - 1) {
                neighbors.add(new Point(p.row, p.col + 1));
            }

            return neighbors;
        }

        private final Grid nextGrid() {
            final int[][] nextGrid = new int[this.costs.length][];

            for (int i = 0; i < this.costs.length; i++) {
                final int[] row = new int[this.costs[i].length];

                for (int j = 0; j < this.costs[i].length; j++) {
                    row[j] = this.costs[i][j] == 9 ? 1 : this.costs[i][j] + 1;
                }

                nextGrid[i] = row;
            }

            return new Grid(nextGrid);
        }

        private int getCost(final Point p) {
            return this.costs[p.row][p.col];
        }
    }

    private static final Grid generatePart2Grid(final Grid originalGrid) {
        final int[][] costs = new int[5 * originalGrid.costs.length][5 * originalGrid.costs[0].length];

        Grid g = originalGrid;
        Grid startGrid = originalGrid;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int row = 0; row < g.costs.length; row++) {
                    for (int col = 0; col < g.costs[0].length; col++) {
                        costs[i * g.costs.length + row][j * g.costs[0].length + col] = g.costs[row][col];
                    }
                }

                g = g.nextGrid();
            }

            g = startGrid.nextGrid();
            startGrid = g;
        }

        return new Grid(costs);
    }

    private static final double euclidianDistance(Point start, Point end) {
        final double distanceX = Math.pow(start.row - end.row, 2);
        final double distanceY = Math.pow(start.col - end.col, 2);

        return Math.sqrt(distanceY + distanceX);
    }

    private static final Grid readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day15.class.getResourceAsStream("/day15.txt")))) {
            String line = reader.readLine();

            final List<int[]> data = new ArrayList<>();

            while (line != null) {
                final int[] row = new int[line.length()];

                final char[] chars = line.toCharArray();

                for (int i = 0; i < chars.length; i++) {
                    row[i] = Character.getNumericValue(chars[i]);
                }

                data.add(row);

                line = reader.readLine();
            }

            return new Grid(data.toArray(new int[0][]));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final int calculateCost(final List<Point> path, final Grid g) {
        int cost = 0;

        for (final Point p : path) {
            cost += g.getCost(p);
        }

        return cost;
    }

    private static final double estimateCost(final List<Point> path) {
        final int cost = calculateCost(path, grid);

        return (double)cost;
    }

    private static final int compare(final List<Point> p1, final List<Point> p2) {
        final double estimateP1 = estimateCost(p1);
        final double estimateP2 = estimateCost(p2);

        return (int)(estimateP1 - estimateP2);
    }

    private static List<Point> astar() {
        final PriorityQueue<List<Point>> candidatePaths = new PriorityQueue<>(Day15::compare);

        final Set<Point> visited = new HashSet<>();

        final List<Point> startPath = new ArrayList<>();
        startPath.add(start);

        candidatePaths.add(startPath);

        while (!candidatePaths.isEmpty()) {
            final List<Point> path = candidatePaths.poll();

            final Point lastPoint = path.get(path.size() - 1);

            if (lastPoint.equals(end)) {
                return path;
            }

            final Set<Point> nextPoints = grid.neighbors(lastPoint);

            for (final Point p : nextPoints) {
                if (!path.contains(p) && !visited.contains(p)) {
                    final List<Point> newPath = new ArrayList<>();
                    newPath.addAll(path);
                    newPath.add(p);

                    visited.add(p);

                    candidatePaths.add(newPath);
                }
            }
        }

        throw new IllegalStateException();
    }

    public static void main(String[] args) {
        grid = generatePart2Grid(readData());

        start = new Point(0, 0);
        end = new Point(grid.costs[0].length - 1, grid.costs.length - 1);

        final List<Point> path = astar();

        System.out.println(calculateCost(path, grid) - grid.getCost(start));
    }
}
