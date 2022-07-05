package geneflorin.watersolver;

public class Main {

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

        new Solver(initialPosition).solve(true, Main::winner);
    }

    private static void winner(final Position position) {
        System.out.println("Solved:");
        position.dumpHistory();
        System.exit(0);
    }
}
