package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class Player {

    public Player() {}

    public Player (String texturePath,
                   int playerCoordinatesX,
                   int playerCoordinatesY,
                   float playerRotation) {
        this.blueTankTexture = new Texture(texturePath);
        this.playerGraphics = new TextureRegion(blueTankTexture);
        this.playerRectangle = createBoundingRectangle(playerGraphics);
        this.playerProperties = new MovingObjectProperties(playerCoordinatesX, playerCoordinatesY, playerRotation);
    }

    public void setBlueTankTexture(Texture blueTankTexture) {
        this.blueTankTexture = blueTankTexture;
    }

    public void setPlayerGraphics(TextureRegion playerGraphics) {
        this.playerGraphics = playerGraphics;
    }

    public void setPlayerProperties(MovingObjectProperties playerProperties) {
        this.playerProperties = playerProperties;
    }

    public void setPlayerRectangle(Rectangle playerRectangle) {
        this.playerRectangle = playerRectangle;
    }

    public Texture getBlueTankTexture() {
        return blueTankTexture;
    }

    public TextureRegion getPlayerGraphics() {
        return playerGraphics;
    }

    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }

    public MovingObjectProperties getPlayerProperties() {
        return playerProperties;
    }

    private Texture blueTankTexture;
    private TextureRegion playerGraphics;
    private Rectangle playerRectangle;
    private MovingObjectProperties playerProperties;
}
