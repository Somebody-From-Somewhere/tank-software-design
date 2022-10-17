package ru.mipt.bit.platformer;

enum MovementDirection {
    RIGHT (1, 0),
    LEFT (-1, 0),
    UP (0, 1),
    DOWN (0, -1),
    NONE (0, 0);

    MovementDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private final int x;
    private final int y;
}
