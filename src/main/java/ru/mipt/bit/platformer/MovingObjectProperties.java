package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import static com.badlogic.gdx.math.MathUtils.random;

public class MovingObjectProperties {

    public MovingObjectProperties() {
        this.objectDestinationCoordinates = createMovingObjectWithRandomCoordinates();
        this.objectCoordinates = new GridPoint2(objectDestinationCoordinates);
        this.objectRotation = randomRotation();
    }

    public MovingObjectProperties(int x, int y) {
        this.objectDestinationCoordinates = new GridPoint2(x, y);
        this.objectCoordinates = new GridPoint2(objectDestinationCoordinates);
        this.objectRotation = randomRotation();
    }

    private GridPoint2 createMovingObjectWithRandomCoordinates() {
        return new GridPoint2(random(9), random(7));

    }

    private Rotation randomRotation() {
        int i = random(4);
        Rotation randomRotation = Rotation.DOWN;
        if(i == 1) {randomRotation = Rotation.RIGHT;}
        if(i == 2) {randomRotation = Rotation.LEFT;}
        if(i == 3) {randomRotation = Rotation.UP;}
        return randomRotation;
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

    public void setObjectRotation(Rotation rotation) {
        this.objectRotation = rotation;
    }

    public Rotation getObjectRotation() {
        return objectRotation;
    }

    private GridPoint2 objectCoordinates;           // which tile the object want to go next
    private GridPoint2 objectDestinationCoordinates;
    private float objectMovementProgress = 1f;
    private Rotation objectRotation;

}
