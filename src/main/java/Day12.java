import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Day12 {

    private static final class Graph {

        private final Map<String, Set<String>> adjacent = new HashMap<>();

        private final void addEdge(final String start, final String end) {
            this.adjacent.computeIfAbsent(start, vertex -> new HashSet<>()).add(end);
            this.adjacent.computeIfAbsent(end, vertex -> new HashSet<>()).add(start);
        }

        @Override
        public String toString() {
            return "Graph{" +
                   "adjacent=" + adjacent +
                   '}';
        }

        private final Set<String> getAdjacent(final String vertex) {
            return this.adjacent.get(vertex);
        }
    }

    private static final Graph readData() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Day12.class.getResourceAsStream("/day12.txt")))) {
            final Graph graph = new Graph();

            for (final String line : reader.lines().toList()) {
                final String[] path = line.split("-");

                graph.addEdge(path[0], path[1]);
            }

            return graph;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final int findPaths(final Graph graph,
                                       final BiPredicate<Deque<String>, String> canVisit) {
        final Deque<Deque<String>> candidates = new ArrayDeque<>();
        final Deque<Deque<String>> paths = new ArrayDeque<>();

        candidates.push(new ArrayDeque<>(Collections.singletonList("start")));

        while (!candidates.isEmpty()) {
            final Deque<String> visited = candidates.pop();

            final String cave = visited.peek();

            if (cave != null && cave.equals("end")) {
                paths.add(visited);
                continue;
            }

            for (final String adjacent : graph.getAdjacent(cave)) {
                if (canVisit.test(visited, adjacent)) {
                    final Deque<String> candidate = new ArrayDeque<>(visited);

                    candidate.push(adjacent);
                    candidates.push(candidate);
                }
            }
        }

        return paths.size();
    }

    private static final boolean canVisitPart1(final Deque<String> visited, final String cave) {
        return !visited.contains(cave) || isBig(cave);
    }

    private static final boolean canVisitPart2(final Deque<String> visited, final String cave) {
        final Map<String, Long> numVisits = visited.stream()
                                                   .filter(Day12::isSmall)
                                                   .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return !"start".equals(cave)
               && (isBig(cave)
                   || !visited.contains(cave)
                   || numVisits.values().stream().noneMatch(visits -> visits > 1));
    }

    private static final boolean isBig(final String cave) {
        return !cave.equals("start") && !cave.equals("end") && Character.isUpperCase(cave.charAt(0));
    }

    private static final boolean isSmall(final String cave) {
        return !cave.equals("start") && !cave.equals("end") && Character.isLowerCase(cave.charAt(0));
    }

    public static void main(String[] args) {
        final Graph g = readData();

        System.out.println(findPaths(g, Day12::canVisitPart1));
        System.out.println(findPaths(g, Day12::canVisitPart2));
    }


}
