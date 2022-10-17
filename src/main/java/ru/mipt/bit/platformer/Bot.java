package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public class Bot extends BaseObject {

    public Bot() {
        super("images/tank_grey.png");
        botProperties = new MovingObjectProperties();
    }

    public Bot(int x, int y) {
        super("images/tank_grey.png");
        botProperties = new MovingObjectProperties(x, y);
    }

    public void setBotProperties(MovingObjectProperties playerProperties) {
        this.botProperties = playerProperties;
    }

    public MovingObjectProperties getBotProperties() {
        return botProperties;
    }

    private MovingObjectProperties botProperties;
}
