package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player extends BaseObject {

    public Player() {
        super();
    }

    public Player (String texturePath,
                   int playerCoordinatesX,
                   int playerCoordinatesY,
                   float playerRotation) {
        super(texturePath);
        this.playerProperties = new MovingObjectProperties(playerCoordinatesX, playerCoordinatesY, playerRotation);
    }

    public void setBlueTankTexture(Texture blueTankTexture) {
        this.setBaseObjectTexture(blueTankTexture);
    }

    public void setPlayerGraphics(TextureRegion playerGraphics) {
        this.setBaseObjectGraphics(playerGraphics);
    }

    public void setPlayerProperties(MovingObjectProperties playerProperties) {
        this.playerProperties = playerProperties;
    }

    public void setPlayerRectangle(Rectangle playerRectangle) {
        this.setBaseObjectRectangle(playerRectangle);
    }

    public Texture getBlueTankTexture() {
        return this.getBaseObjectTexture();
    }

    public TextureRegion getPlayerGraphics() {
        return this.getBaseObjectGraphics();
    }

    public Rectangle getPlayerRectangle() {
        return this.getBaseObjectRectangle();
    }

    public MovingObjectProperties getPlayerProperties() {
        return playerProperties;
    }

//    private Rectangle playerRectangle;
    private MovingObjectProperties playerProperties;
}
