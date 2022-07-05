package geneflorin.watersolver;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Solver {
    private static final boolean dumpMoves = false;
    private static final boolean dumpDeadEnds = false;

    private final Position initialPosition;
    private final Set<Position> traverse = new HashSet<>();
    private final Deque<Position> queue = new ArrayDeque<>();

    public Solver(final Position initialPosition) {
        this.initialPosition = initialPosition;
    }

    public void solve(final boolean bfs, final Consumer<Position> callback) {
        consider(initialPosition, bfs, callback);

        while (!queue.isEmpty()) {
            final var cur = queue.pop();

            final boolean[] deadEnd = { true };

            cur.generateMoves(p -> {
                deadEnd[0] = false;
                consider(p, bfs, callback);
            });

            if (deadEnd[0] && dumpDeadEnds) {
                System.out.println("Dead end: " + cur);
            }
        }
    }

    private void consider(final Position pos, final boolean bfs, final Consumer<Position> callback) {
        if (traverse.add(pos)) {
            if (dumpMoves) {
                System.out.println("Considering:");
                pos.dumpTransition();
            }

            if (pos.isComplete()) {
                callback.accept(pos);
            } else if (bfs) {
                queue.addLast(pos);
            } else {
                queue.addFirst(pos);
            }
        } else {
            // System.out.println("Already seen" + pos);
        }
    }
}
