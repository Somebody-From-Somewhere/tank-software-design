package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class Tree {

    public Tree() {}

    public Tree (String treePath, int x, int y) {
        greenTreeTexture = new Texture("images/greenTree.png");
        treeObstacleGraphics = new TextureRegion(greenTreeTexture);
        treeObstacleCoordinates = new GridPoint2(1, 3);
        treeObstacleRectangle = createBoundingRectangle(treeObstacleGraphics);
    }

    public void setGreenTreeTexture(Texture greenTreeTexture) {
        this.greenTreeTexture = greenTreeTexture;
    }

    public void setTreeObstacleCoordinates(GridPoint2 treeObstacleCoordinates) {
        this.treeObstacleCoordinates = treeObstacleCoordinates;
    }

    public void setTreeObstacleGraphics(TextureRegion treeObstacleGraphics) {
        this.treeObstacleGraphics = treeObstacleGraphics;
    }

    public void setTreeObstacleRectangle(Rectangle treeObstacleRectangle) {
        this.treeObstacleRectangle = treeObstacleRectangle;
    }

    public Texture getGreenTreeTexture() {
        return greenTreeTexture;
    }

    public TextureRegion getTreeObstacleGraphics() {
        return treeObstacleGraphics;
    }

    public GridPoint2 getTreeObstacleCoordinates() {
        return treeObstacleCoordinates;
    }

    public Rectangle getTreeObstacleRectangle() {
        return treeObstacleRectangle;
    }

    private Texture greenTreeTexture;
    private TextureRegion treeObstacleGraphics;
    private GridPoint2 treeObstacleCoordinates = new GridPoint2();
    private Rectangle treeObstacleRectangle = new Rectangle();
}
