package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public class Tree extends BaseObject {

    public Tree() {
        super();
    }

    public Tree (int x, int y) {
        super("images/greenTree.png");
        treeObstacleCoordinates = new GridPoint2(x, y);
    }

    public void setGreenTreeTexture(Texture greenTreeTexture) {
        this.setBaseObjectTexture(greenTreeTexture);
    }

    public void setTreeObstacleGraphics(TextureRegion treeObstacleGraphics) {
        this.setBaseObjectGraphics(treeObstacleGraphics);
    }

    public void setTreeObstacleRectangle(Rectangle treeObstacleRectangle) {
        this.setBaseObjectRectangle(treeObstacleRectangle);
    }

    public void setTreeObstacleCoordinates(GridPoint2 treeObstacleCoordinates) {
        this.treeObstacleCoordinates = treeObstacleCoordinates;
    }

    public Texture getGreenTreeTexture() {
        return  this.getBaseObjectTexture();
    }

    public TextureRegion getTreeObstacleGraphics() {
        return this.getBaseObjectGraphics();
    }

    public Rectangle getTreeObstacleRectangle() {
        return this.getBaseObjectRectangle();
    }

    public GridPoint2 getTreeObstacleCoordinates() {
        return this.treeObstacleCoordinates;
    }

    private GridPoint2 treeObstacleCoordinates = new GridPoint2();

}
