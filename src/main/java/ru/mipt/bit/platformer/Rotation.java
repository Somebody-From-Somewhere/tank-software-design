package ru.mipt.bit.platformer;

enum Rotation{
    RIGHT (0f),
    LEFT (-180f),
    UP (90f),
    DOWN (-90f);

    Rotation(float rotation) {
        this.rotation = rotation;
    }

    public float getFloatRotation() {
        return rotation;
    }

    private final float rotation;
}