package geneflorin.watersolver;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private static int numSolutions = 0;

    private static class PosCount {
        final Position position;
        int count;

        public PosCount(Position position) {
            this.position = position;
        }
    }

    private static final Map<Position, PosCount> winningPositions = new HashMap<>();

    public static final Position THE_ONE = new Position(
        new BottleState("gwmo"), // w = brown
        new BottleState("GBog"),
        new BottleState("yrpW"), // p = pink not purple
        new BottleState("mapm"), // a = gray
        new BottleState("wbBG"),
        new BottleState("wrrb"),
        new BottleState("abwr"),

        new BottleState("GBpy"),
        new BottleState("WpWg"),
        new BottleState("aboG"),
        new BottleState("yoWB"),
        new BottleState("yagm"),
        new BottleState("    "),
        new BottleState("    ")
    );

    public static final Position TEST = new Position(
        new BottleState("yyy "),
        new BottleState("    ")
    );

    public static void main(String[] args) {
        final var initialPosition = THE_ONE;

        System.out.println("Start:\n" + initialPosition);
        initialPosition.dumpStats();

        final var solver = new Solver(initialPosition);
        solver.solve(true, Main::winner);

        System.out.println("# winning positions: " + winningPositions.size());

        final var sortedWinners =
                winningPositions.values().stream().sorted((a, b) -> b.count - a.count).collect(Collectors.toList());

        for (int i = 0; i < 20; i++) {
            System.out.printf("Top %d count = %d%n", i, sortedWinners.get(i).count);
        }
    }

    private static void winner(final Position position) {
        //System.out.printf("Solution #%d:%n", ++numSolutions);
        //position.dumpHistory();

        for (var p = position; ; p = p.move.fromPosition) {
            winningPositions.computeIfAbsent(p, PosCount::new).count++;

            if (p.move == null) {
                break;
            }
        }
    }
}
