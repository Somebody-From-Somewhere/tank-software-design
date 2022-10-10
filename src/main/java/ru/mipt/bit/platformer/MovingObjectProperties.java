package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.random;

public class MovingObjectProperties {

    public MovingObjectProperties() {
        GenerateRandomCoordinates();
    }

    public MovingObjectProperties(int x, int y) {
        this.objectDestinationCoordinates = new GridPoint2(x, y);
        this.objectCoordinates = new GridPoint2(objectDestinationCoordinates);
        this.objectRotation = 0f;
    }

    private void GenerateRandomCoordinates() {
        this.objectDestinationCoordinates = new GridPoint2(random(7), random(7));
        this.objectCoordinates = new GridPoint2(objectDestinationCoordinates);
        this.objectRotation = 0f;
    }

    public void setObjectCoordinates(GridPoint2 objectCoordinates) {
        this.objectCoordinates = objectCoordinates;
    }

    public GridPoint2 getObjectCoordinates() {
        return objectCoordinates;
    }

    public void setObjectDestinationCoordinatesY(int objectDestinationCoordinatesY) {
        this.objectDestinationCoordinates.y = objectDestinationCoordinatesY;
    }

    public void setObjectDestinationCoordinatesX(int objectDestinationCoordinatesX) {
        this.objectDestinationCoordinates.x = objectDestinationCoordinatesX;
    }

    public GridPoint2 getObjectDestinationCoordinates() {
        return objectDestinationCoordinates;
    }

    public void setObjectMovementProgress(float objectMovementProgress) {
        this.objectMovementProgress = objectMovementProgress;
    }

    public float getObjectMovementProgress() {
        return objectMovementProgress;
    }

    public void setObjectRotation(float objectRotation) {
        this.objectRotation = objectRotation;
    }

    public float getObjectRotation() {
        return objectRotation;
    }

    private GridPoint2 objectCoordinates;           // which tile the player want to go next
    private GridPoint2 objectDestinationCoordinates;
    private float objectMovementProgress = 1f;
    private float objectRotation;

}
