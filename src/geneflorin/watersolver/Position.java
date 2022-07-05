package geneflorin.watersolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Position {
    public final BottleState[] bottleStates;
    public final Move move;
    private final int hashCode;
    private final boolean isComplete;

    public Position(final BottleState[] bottleStates, final Move move) {
        this.bottleStates = bottleStates;
        this.move = move;
        this.hashCode = Arrays.hashCode(bottleStates);

        boolean complete = true;

        for (final var bs : bottleStates) {
            if (!bs.isEmpty() && !bs.isComplete()) {
                complete = false;
                break;
            }
        }

        this.isComplete = complete;
    }

    public Position(final BottleState... bottleStates) {
        this(bottleStates, null);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void generateMoves(final Consumer<Position> callback) {
        final var numBottles = bottleStates.length;

        for (int fromIdx = 0; fromIdx < numBottles; fromIdx++) {
            final var fromBottle = bottleStates[fromIdx];

            if (fromBottle.isEmpty()) {
                continue;
            }

            if (fromBottle.isComplete()) {
                continue;
            }

            final var top = fromBottle.top();
            final var fromMixed = fromBottle.isMixed();

            for (int toIdx = 0; toIdx < numBottles; toIdx++) {
                if (fromIdx == toIdx) {
                    continue;
                }

                final var toBottle = bottleStates[toIdx];

                if (toBottle.isFull()) {
                    continue;
                }

                if (toBottle.isEmpty() && !fromMixed) { // meaningless
                    continue;
                }

                if (toBottle.isEmpty() || toBottle.top() == top) {
                    callback.accept(move(fromIdx, toIdx));
                }
            }
        }
    }

    private Position move(final int fromIdx, final int toIdx) {
        final var fromBottle = bottleStates[fromIdx];
        final var toBottle = bottleStates[toIdx];
        final var color = fromBottle.top();
        final var toAvail = toBottle.available();
        final var fromAvail = fromBottle.topDepth();
        final var numPour = Math.min(fromAvail, toAvail);
        final var newStates = bottleStates.clone();

        newStates[fromIdx] = fromBottle.remove(numPour);
        newStates[toIdx] = toBottle.add(numPour, color);

        return new Position(newStates, new Move(color, fromIdx, toIdx, this));
    }

    @Override
    public String toString() {
        return Arrays.stream(bottleStates)
            .map(BottleState::toString)
            .collect(Collectors.joining(" "));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Position position = (Position) o;
        return Arrays.equals(bottleStates, position.bottleStates);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public void dumpHistory() {
        final var history = new ArrayList<Position>();

        for (var p = this; ; p = p.move.fromPosition) {
            history.add(0, p); // inefficient, fix later

            if (p.move == null) {
                break;
            }
        }

        int numMove = 0;

        for (final var p : history) {
            if (p.move != null) {
                System.out.printf("#%d %s%n", ++numMove, p.dumpMove());
            }

            System.out.println(p);
        }
    }

    public void dumpTransition() {
        if (move != null) {
            System.out.println("from " + move.fromPosition);
            System.out.println("via  " + dumpMove());
        }

        System.out.println("to   " + this);
    }


    public String dumpMove() {
        return String.format("%s %d -> %d", move.color, move.fromBottle, move.toBottle);
    }

    public void dumpStats() {
        final var colors = new HashMap<Character, int[]>();

        for (final var bs : bottleStates) {
            for (final var c : bs.layers) {
                colors.computeIfAbsent(c, $ -> new int[] {0})[0]++;
            }
        }

        for (final var e : colors.entrySet()) {
            System.out.printf("%c: %d%n", e.getKey(), e.getValue()[0]);
        }
    }
}
