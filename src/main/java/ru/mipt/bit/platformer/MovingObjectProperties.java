package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class MovingObjectProperties {

    public MovingObjectProperties() {}

    public MovingObjectProperties(int objectCoordinatesX,
                                  int objectCoordinatesY,
                                  float objectRotation) {
        this.objectDestinationCoordinates = new GridPoint2(objectCoordinatesX, objectCoordinatesY);
        this.objectCoordinates = new GridPoint2(objectDestinationCoordinates);
        this.objectRotation = objectRotation;
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
