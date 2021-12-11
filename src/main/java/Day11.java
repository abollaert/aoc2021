import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 {

    private record Position(int row, int col) {}

    private static int[][] readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day11.class.getResourceAsStream("/day11.txt")))) {
            final List<String> lines = reader.lines().toList();
            final int[][] data = new int[lines.size()][];

            for (int i = 0; i < lines.size(); i++) {
                final List<Integer> lineEnergyLevels = lines.get(i)
                                                            .chars()
                                                            .mapToObj(Character::getNumericValue)
                                                            .toList();

                data[i] = new int[lineEnergyLevels.size()];

                for (int j = 0; j < lineEnergyLevels.size(); j++) {
                    final int energyLevel = lineEnergyLevels.get(j);

                    data[i][j] = energyLevel;
                }
            }

            return data;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final void printData(final int[][] data) {
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                System.out.print(data[row][col]);
            }

            System.out.print("\n");
        }

        System.out.print("\n");
    }

    private static final List<Position> getNeighbors(final int[][] data, final Position position) {
        final int rowStart = position.row == 0 ? position.row : position.row - 1;
        final int colStart = position.col == 0 ? position.col : position.col - 1;
        final int rowEnd = position.row == data.length - 1 ? position.row : position.row + 1;
        final int colEnd = position.col == data[position.row].length - 1 ? position.col : position.col + 1;

        final List<Position> positions = new ArrayList<>();

        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                if (i != position.row || j != position.col) {
                    positions.add(new Position(i, j));
                }
            }
        }

        return positions;
    }

    private static final List<Position> update(final List<Position> positions,
                                              final int[][] data,
                                              final Set<Position> flashed) {
        final List<Position> currentFlashed = new ArrayList<>();

        for (final Position p : positions) {
            if (data[p.row][p.col] == 9) {
                if (!flashed.contains(p)) {
                    flashed.add(p);
                    currentFlashed.add(p);
                }
            } else {
                data[p.row][p.col]++;
            }
        }

        return currentFlashed;
    }

    private static List<Position> allPositions(final int[][] data) {
        final List<Position> positions = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                positions.add(new Position(i, j));
            }
        }

        return positions;
    }

    private static final Set<Position> step(final int[][] data) {
        final Set<Position> flashed = new HashSet<>();
        final List<Position> allPositions = allPositions(data);

        List<Position> currentFlashed = update(allPositions, data, flashed);

        while (currentFlashed.size() > 0) {
            final List<Position> neighbors = new ArrayList<>();

            for (final Position p : currentFlashed) {
                neighbors.addAll(getNeighbors(data, p));
            }

            currentFlashed = update(neighbors, data, flashed);
        }

        for (final Position p : flashed) {
            data[p.row][p.col] = 0;
        }

        return flashed;
    }

    public static void main(String[] args) {
        final int[][] data = readData();
        final int count = data.length * data[0].length;

        int step = 0;

        while (true) {
            step++;

            int number = step(data).size();

            if (number == count) {
                System.out.println(step);

                break;
            }
        }
    }
}
