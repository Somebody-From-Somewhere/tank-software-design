package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Batch batch;

    Map map;
    Player player;
    Tree tree;


    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles

        map = new Map("level.tmx", batch);
        player = new Player("images/tank_blue.png", 1, 1, 0f);
        tree = new Tree("images/greenTree.png", 1, 3);
        moveRectangleAtTileCenter(map.getGroundLayer(), tree.getTreeObstacleRectangle(), tree.getTreeObstacleCoordinates());
    }

    public void clean() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render() {
        clean();

        movePlayer();

        // render each tile of the level
        map.getLevelRenderer().render();

        // start recording all drawing commands
        batch.begin();

        // render player
        drawTextureRegionUnscaled(
                batch,
                player.getPlayerGraphics(),
                player.getPlayerRectangle(),
                player.getPlayerProperties().getObjectRotation()
        );

        // render tree obstacle
        drawTextureRegionUnscaled(batch, tree.getTreeObstacleGraphics(), tree.getTreeObstacleRectangle(), 0f);

        // submit all drawing requests
        batch.end();
    }

    private void movePlayer() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            if (isEqual(player.getPlayerProperties().getObjectMovementProgress(), 1f)) {
                // check potential player destination for collision with obstacles
                if (isThereCollision(true, true)) {
                    updateCoordinate(true, 1);
                }
                player.getPlayerProperties().setObjectRotation(90f);
            }
        }
        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            if (isEqual(player.getPlayerProperties().getObjectMovementProgress(), 1f)) {
                if (isThereCollision(false, false)) {
                    updateCoordinate(false, -1);
                }
                player.getPlayerProperties().setObjectRotation(-180f);
            }
        }
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            if (isEqual(player.getPlayerProperties().getObjectMovementProgress(), 1f)) {
                if (isThereCollision(true, false)) {
                    updateCoordinate(true, -1);
                }
                player.getPlayerProperties().setObjectRotation(-90f);
            }
        }
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            if (isEqual(player.getPlayerProperties().getObjectMovementProgress(), 1f)) {
                if (isThereCollision(false, true)) {
                    updateCoordinate(false, 1);

                }
                player.getPlayerProperties().setObjectRotation(0f);
            }
        }

        // calculate interpolated player screen coordinates
        calculatePlayerScreenCoordinates();

        player.getPlayerProperties().setObjectMovementProgress(continueProgress(
                player.getPlayerProperties().getObjectMovementProgress(), deltaTime, MOVEMENT_SPEED));
        if (isEqual(player.getPlayerProperties().getObjectMovementProgress(), 1f)) {
            // record that the player has reached his/her destination
            player.getPlayerProperties().getObjectCoordinates().set(
                    player.getPlayerProperties().getObjectDestinationCoordinates()
            );
        }
    }

    private void updateCoordinate(boolean direction, int diff) {
        if (direction)
            player.getPlayerProperties().setObjectDestinationCoordinatesY(
                    player.getPlayerProperties().getObjectCoordinates().y + diff
            );
        else
            player.getPlayerProperties().setObjectDestinationCoordinatesX(
                    player.getPlayerProperties().getObjectCoordinates().x + diff
            );
        player.getPlayerProperties().setObjectMovementProgress(0f);
    }

    private boolean isThereCollision(boolean direction, boolean sign) {
        if (direction)
            if (sign)
                return !tree.getTreeObstacleCoordinates().equals(incrementedY(player.getPlayerProperties().getObjectCoordinates()));
            else
                return !tree.getTreeObstacleCoordinates().equals(decrementedY(player.getPlayerProperties().getObjectCoordinates()));
        else
            if (sign)
                return !tree.getTreeObstacleCoordinates().equals(incrementedX(player.getPlayerProperties().getObjectCoordinates()));
            else
                return !tree.getTreeObstacleCoordinates().equals(decrementedX(player.getPlayerProperties().getObjectCoordinates()));
    }

    private void calculatePlayerScreenCoordinates() {
        map.getTileMovement().moveRectangleBetweenTileCenters(
                player.getPlayerRectangle(),
                player.getPlayerProperties().getObjectCoordinates(),
                player.getPlayerProperties().getObjectDestinationCoordinates(),
                player.getPlayerProperties().getObjectMovementProgress());
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        tree.getGreenTreeTexture().dispose();
        player.getBlueTankTexture().dispose();
        map.getLevel().dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
