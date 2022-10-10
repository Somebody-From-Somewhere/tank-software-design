package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static com.badlogic.gdx.math.MathUtils.random;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Batch batch;

    String mapPathForParser /*= "/home/dmitrii_penkin/Documents/Java/Software design/tank-software-design/src/main/resources/Map.txt"*/;
    Map map;
    Player player;
    ArrayList<Tree> trees = new ArrayList<>();

    public void TreeGenerator(int quantity) {
        int random_x, random_y;
        for(int i = 0; i < quantity; i++) {
            random_x = random(5);
            random_y = random(5);
            trees.add(new Tree(random_x, random_y));
            moveRectangleAtTileCenter(map.getGroundLayer(), trees.get(i).getTreeObstacleRectangle(), trees.get(i).getTreeObstacleCoordinates());
        }
    }

    public void drawTreesTextureRegionUnscaled(Batch batch) {
        for (Tree tree : trees) {
            drawTextureRegionUnscaled(batch, tree.getTreeObstacleGraphics(), tree.getTreeObstacleRectangle(), 0f);
        }
    }

    public void TreesDispose(ArrayList<Tree> trees) {
        for(Tree tree : trees) {
            tree.getGreenTreeTexture().dispose();
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles

        map = new Map("level.tmx", batch);
        if (mapPathForParser == null) {
            player = new Player();
            TreeGenerator(5);
        } else {
            try {
                parser(mapPathForParser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        drawTreesTextureRegionUnscaled(batch);


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
        boolean isThereCollision = true;
        for (int i = 0; i < trees.size() && isThereCollision; i++) {
            if (direction)
                if (sign)
                    isThereCollision = !trees.get(i).getTreeObstacleCoordinates().equals(incrementedY(player.getPlayerProperties().getObjectCoordinates()));
                else
                    isThereCollision = !trees.get(i).getTreeObstacleCoordinates().equals(decrementedY(player.getPlayerProperties().getObjectCoordinates()));
            else if (sign)
                isThereCollision = !trees.get(i).getTreeObstacleCoordinates().equals(incrementedX(player.getPlayerProperties().getObjectCoordinates()));
            else
                isThereCollision = !trees.get(i).getTreeObstacleCoordinates().equals(decrementedX(player.getPlayerProperties().getObjectCoordinates()));
        }
        return isThereCollision;
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
        TreesDispose(trees);
        player.getBlueTankTexture().dispose();
        map.getLevel().dispose();
        batch.dispose();
    }

    public void parser(String filePath) throws IOException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        ArrayList<String> charMap= new ArrayList<>();
        int treeCounter = 0;
        while (scanner.hasNext()) {
            charMap.add(scanner.nextLine());
        }
        for(int i = charMap.size() - 1; i >= 0; i--) {
            for (int j = 0; j < charMap.get(i).length(); j++) {
                if (charMap.get(i).charAt(j) == 'X') {
                    player = new Player(j, charMap.size() - i - 1);
                }
                if (charMap.get(i).charAt(j) == 'T') {
                    trees.add(new Tree(j, charMap.size() - i - 1));
                    moveRectangleAtTileCenter(map.getGroundLayer(), trees.get(treeCounter).getTreeObstacleRectangle(), trees.get(treeCounter).getTreeObstacleCoordinates());
                    treeCounter++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }

}
