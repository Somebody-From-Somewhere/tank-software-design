package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class BaseObject {

    public BaseObject() {}

    public BaseObject (String texturePath) {
        baseObjectTexture = new Texture(texturePath);
        baseObjectGraphics = new TextureRegion(baseObjectTexture);
        baseObjectRectangle = createBoundingRectangle(baseObjectGraphics);
    }

    public void setBaseObjectTexture(Texture baseObjectTexture) {
        this.baseObjectTexture = baseObjectTexture;
    }

    public void setBaseObjectGraphics(TextureRegion baseObstacleGraphics) {
        this.baseObjectGraphics = baseObstacleGraphics;
    }

    public void setBaseObjectRectangle(Rectangle baseObstacleRectangle) {
        this.baseObjectRectangle = baseObstacleRectangle;
    }

    public Texture getBaseObjectTexture() {
        return baseObjectTexture;
    }

    public TextureRegion getBaseObjectGraphics() {
        return baseObjectGraphics;
    }

    public Rectangle getBaseObjectRectangle() {
        return baseObjectRectangle;
    }

    private Texture baseObjectTexture;
    private TextureRegion baseObjectGraphics;
    private Rectangle baseObjectRectangle = new Rectangle();


}
