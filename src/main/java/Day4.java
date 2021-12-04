import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day4 {

    private record WinningState(int selectedNumber, Board winningBoard) {

        private final int getScore() {
            return selectedNumber * winningBoard.getSumUnmarked();
        }
    };

    private record Game(List<Integer> selectedNumbers,
                        List<Board> boards) {

        private final WinningState runGame() {
            for (final int selectedNumber : this.selectedNumbers) {
                for (final Board board : this.boards) {
                    board.selected(selectedNumber);

                    if (board.wins()) {
                        return new WinningState(selectedNumber, board);
                    }
                }
            }

            return null;
        }

        private final WinningState runGameLastBoard() {
            for (final int selectedNumber : this.selectedNumbers) {
                for (final Board board : this.boards) {
                    board.selected(selectedNumber);

                    if (board.wins()) {
                        boolean last = true;

                        for (final Board otherBoard : this.boards) {
                            if (!otherBoard.wins()) {
                                last = false;
                            }
                        }

                        if (last) {
                            return new WinningState(selectedNumber, board);
                        }
                    }
                }
            }

            return null;
        }
    }

    private static final class Board {

        private final int[][] data;
        private final List<Integer> markedIndices;

        private Board() {
            this.data = new int[5][5];
            this.markedIndices = new ArrayList<>();
        }

        public final String toString() {
            final StringBuilder builder = new StringBuilder("[");

            for (int i = 0; i < this.data.length; i++) {
                for (int j = 0; j < this.data[i].length; j++) {
                    builder.append(this.data[i][j]).append(isSelected(i, j) ? "*" : "").append(" ");
                }

                builder.append("\n");
            }

            builder.append("]");

            return builder.toString();
        }

        private final void selected(final int selected) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    if (data[i][j] == selected) {
                        this.markedIndices.add(i * 5 + j);
                    }
                }
            }
        }

        private final boolean isSelected(final int row, final int col) {
            return this.markedIndices.contains(row * 5 + col);
        }

        private final boolean wins() {
            for (int row = 0; row < this.data.length; row++) {
                boolean allSelected = true;

                for (int col = 0; col < this.data[row].length; col++) {
                    if (!this.isSelected(row, col)) {
                        allSelected = false;
                    }
                }

                if (allSelected) {
                    return true;
                }
            }

            for (int col = 0; col < this.data.length; col++) {
                boolean allSelected = true;

                for (int row = 0; row < this.data.length; row++) {
                    if (!this.isSelected(row, col)) {
                        allSelected = false;
                    }
                }

                if (allSelected) {
                    return true;
                }
            }

            return false;
        }

        private int getSumUnmarked() {
            int sum = 0;

            for (int row = 0; row < this.data.length; row++) {
                for (int col = 0; col < this.data.length; col++) {
                    if (!this.isSelected(row, col)) {
                        sum += this.data[row][col];
                    }
                }
            }

            return sum;
        }
    }

    private static Game readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day4.class.getResourceAsStream("/day4.txt")))) {
            final List<Integer> selectedNumbers = Arrays.stream(reader.readLine().split(","))
                                                            .map(Integer::parseInt)
                                                            .toList();

            // Skip one line.
            reader.readLine();

            final List<Board> boards = new ArrayList<>();

            String line = reader.readLine();
            Board currentBoard = new Board();
            int lineIndex = 0;

            while (line != null) {
                if (!line.trim().equals("")) {
                    final int[] numbers = Arrays.stream(line.split("\s+"))
                                                .map(String::trim)
                                                .filter(s -> !s.isBlank())
                                                .mapToInt(Integer::parseInt)
                                                .toArray();

                    currentBoard.data[lineIndex++] = numbers;
                } else {
                    boards.add(currentBoard);

                    currentBoard = new Board();
                    lineIndex = 0;
                }

                line = reader.readLine();
            }

            boards.add(currentBoard);

            return new Game(selectedNumbers, boards);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        final Game game = readData();
        final WinningState winningState = game.runGame();

        System.out.println(winningState);
        System.out.println(winningState.getScore());

        final WinningState last = game.runGameLastBoard();

        System.out.println(last);
        System.out.println(last.getScore());
    }
}
