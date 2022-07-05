package geneflorin.watersolver;

import java.util.Arrays;

public class BottleState {
    public final char[] layers;
    public final int height;
    private final boolean isComplete;

    public BottleState(final int height, char[] layers) {
        if (layers.length > height) {
            throw new IllegalArgumentException("height mismatch");
        }

        this.height = height;
        this.layers = layers;
        this.isComplete = isFull() && !isMixed();
    }

    public BottleState(String s) {
        this.height = s.length();
        this.layers = s.trim().toCharArray();
        this.isComplete = isFull() && !isMixed();
    }

    public boolean isMixed() {
        final var firstColor = layers[0];

        for (int idx = 1; idx < layers.length; idx++) {
            if (layers[idx] != firstColor) {
                return true;
            }
        }

        return false;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public boolean isEmpty() {
        return layers.length == 0;
    }

    public boolean isFull() {
        return available() == 0;
    }

    public int available() {
        return height - layers.length;
    }

    public char top() {
        if (isEmpty()) {
            throw new IllegalStateException("empty");
        }

        return layers[layers.length - 1];
    }

    public int topDepth() {
        if (isEmpty()) {
            throw new IllegalStateException("empty");
        }

        int depth = 1;
        final var topColor = top();

        for (int idx = layers.length - 2; idx >= 0; idx--) {
            if (layers[idx] != topColor) {
                break;
            }

            depth++;
        }

        return depth;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder("[");

        for (int idx = 0; idx < height; idx++) {
            sb.append(idx < layers.length ? layers[idx] : " ");
        }

        sb.append(']');

        return sb.toString();
    }

    public BottleState remove(final int numPour) {
        final var curLength = layers.length;
        final var newLength = curLength - numPour;

        if (newLength < 0) {
            throw new IllegalArgumentException("n=" + numPour);
        }

        final var newLayers = new char[newLength];

        System.arraycopy(layers, 0, newLayers, 0, newLength);

        return new BottleState(height, newLayers);
    }

    public BottleState add(final int numPour, final char color) {
        final var curLength = layers.length;
        final var newLength = curLength + numPour;

        if (newLength > height) {
            throw new IllegalArgumentException("n=" + numPour);
        }

        final var newLayers = new char[newLength];

        System.arraycopy(layers, 0, newLayers, 0, curLength);
        Arrays.fill(newLayers, curLength, newLength, color);

        return new BottleState(height, newLayers);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BottleState that = (BottleState) o;

        if (that.height != this.height) {
            throw new IllegalStateException("Height mismatch");
        }

        return Arrays.equals(layers, that.layers);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(layers);
    }
}
