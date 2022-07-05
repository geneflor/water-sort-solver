package geneflorin.watersolver;

public class Move {
    public final char color;
    public final int fromBottle;
    public final int toBottle;
    public final Position fromPosition;

    public Move(final char color, final int fromBottle, final int toBottle, final Position fromPosition) {
        this.color = color;
        this.fromBottle = fromBottle;
        this.toBottle = toBottle;
        this.fromPosition = fromPosition;
    }
}
