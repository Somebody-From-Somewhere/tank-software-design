package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;

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
    ArrayList<Bot> bots = new ArrayList<>();

    private void treeGenerator(int treeQuantity) {
        int random_x, random_y;
        for(int i = 0; i < treeQuantity; i++) {
            random_x = random(9);
            random_y = random(7);
            trees.add(new Tree(random_x, random_y));
            moveRectangleAtTileCenter(map.getGroundLayer(), trees.get(i).getTreeObstacleRectangle(), trees.get(i).getTreeObstacleCoordinates());
        }
    }

    private void drawTreesTextureRegionUnscaled() {
        for(Tree tree : trees) {
            drawTextureRegionUnscaled(batch, tree.getTreeObstacleGraphics(), tree.getTreeObstacleRectangle(), 0f);
        }
    }

    private void treesDispose() {
        for(Tree tree : trees) {
            tree.getGreenTreeTexture().dispose();
        }
    }

    private void botGenerator(int bot_quantity) {
        for(int i = 0; i < bot_quantity; i++) {
            bots.add(new Bot());
            moveRectangleAtTileCenter(
                    map.getGroundLayer(),
                    bots.get(i).getBaseObjectRectangle(),
                    bots.get(i).getBotProperties().getObjectCoordinates());
        }
    }

    private void drawBotTextureRegionUnscaled() {
        for(Bot bot : bots) {
            drawTextureRegionUnscaled(
                    batch,
                    bot.getBaseObjectGraphics(),
                    bot.getBaseObjectRectangle(),
                    bot.getBotProperties().getObjectRotation().getFloatRotation()
            );
        }
    }

    private void botsDispose() {
        for(Bot bot : bots) {
            bot.getBaseObjectTexture().dispose();
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles

        map = new Map("level.tmx", batch);
        if (mapPathForParser == null) {
            player = new Player();
            treeGenerator(random(10) + 1);
        } else {
            try {
                parser(mapPathForParser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        botGenerator(random(15) + 1);

    }

    public void clean() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render() {
        clean();

        moveBot();
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
                player.getPlayerProperties().getObjectRotation().getFloatRotation()
        );

        // render bots obstacles
        drawBotTextureRegionUnscaled();

        // render tree obstacle
        drawTreesTextureRegionUnscaled();


        // submit all drawing requests
        batch.end();
    }

    private void moveBot() {
        for (Bot bot : bots) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            int i = random(4);
            MovementDirection direction = MovementDirection.NONE;
            Rotation rotation = bot.getBotProperties().getObjectRotation();
            if(i == 1) {
                direction = MovementDirection.RIGHT;
                rotation = Rotation.RIGHT;
            }
            if(i == 2) {
                direction = MovementDirection.LEFT;
                rotation = Rotation.LEFT;
            }
            if(i == 3) {
                direction = MovementDirection.UP;
                rotation = Rotation.UP;
            }
            if(i == 4) {
                direction = MovementDirection.DOWN;
                rotation = Rotation.DOWN;
            }
            if (isEqual(bot.getBotProperties().getObjectMovementProgress(), 1f)) {
                if (isThereBotCollision(bot, direction)) {
                    updateBotCoordinate(bot, direction);
                }
                bot.getBotProperties().setObjectRotation(rotation);
            }
            calculateBotScreenCoordinates(bot);
            bot.getBotProperties().setObjectMovementProgress(continueProgress(
                    bot.getBotProperties().getObjectMovementProgress(), deltaTime, MOVEMENT_SPEED));
            if (isEqual(bot.getBotProperties().getObjectMovementProgress(), 1f)) {
                // record that the bot has reached his/her destination
                bot.getBotProperties().getObjectCoordinates().set(
                        bot.getBotProperties().getObjectDestinationCoordinates()
                );
            }
        }


    }

    private void movePlayer() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();
        MovementDirection direction = MovementDirection.NONE;
        Rotation rotation = player.getPlayerProperties().getObjectRotation();

        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            direction = MovementDirection.UP;
            rotation = Rotation.UP;
        }
        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            direction = MovementDirection.LEFT;
            rotation = Rotation.LEFT;
        }
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            direction = MovementDirection.DOWN;
            rotation = Rotation.DOWN;
        }
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            direction = MovementDirection.RIGHT;
            rotation = Rotation.RIGHT;
        }

        if (isEqual(player.getPlayerProperties().getObjectMovementProgress(), 1f)) {
            if (isThereCollision(direction)) {
                updatePlayerCoordinate(direction);
            }
            player.getPlayerProperties().setObjectRotation(rotation);
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

    private void updatePlayerCoordinate(MovementDirection movementDirection) {
        player.getPlayerProperties().setObjectDestinationCoordinatesY(
                player.getPlayerProperties().getObjectCoordinates().y + movementDirection.getY());
        player.getPlayerProperties().setObjectDestinationCoordinatesX(
                player.getPlayerProperties().getObjectCoordinates().x + movementDirection.getX());
        player.getPlayerProperties().setObjectMovementProgress(0f);
    }

    private void updateBotCoordinate(Bot bot, MovementDirection movementDirection) {
        bot.getBotProperties().setObjectDestinationCoordinatesY(
                bot.getBotProperties().getObjectCoordinates().y + movementDirection.getY());
        bot.getBotProperties().setObjectDestinationCoordinatesX(
                bot.getBotProperties().getObjectCoordinates().x + movementDirection.getX());
        bot.getBotProperties().setObjectMovementProgress(0f);
    }

    private boolean isOnScreen(GridPoint2 newCoordinates) {
        return newCoordinates.x >= 0 && newCoordinates.x <= 9 &&
                newCoordinates.y >= 0 && newCoordinates.y <= 7;
    }

    private boolean isThereCollision(MovementDirection direction) {
        boolean isThereCollision = true;
        GridPoint2 newPlayerCoordinates = player.getPlayerProperties().getObjectCoordinates();
        if (direction == MovementDirection.UP)
            newPlayerCoordinates = incrementedY(player.getPlayerProperties().getObjectCoordinates());
        if (direction == MovementDirection.DOWN)
            newPlayerCoordinates = decrementedY(player.getPlayerProperties().getObjectCoordinates());
        if (direction == MovementDirection.RIGHT)
            newPlayerCoordinates = incrementedX(player.getPlayerProperties().getObjectCoordinates());
        if (direction == MovementDirection.LEFT)
            newPlayerCoordinates = decrementedX(player.getPlayerProperties().getObjectCoordinates());
        for (int i = 0; i < trees.size() && isThereCollision; i++) {
            isThereCollision = !trees.get(i).getTreeObstacleCoordinates().equals(newPlayerCoordinates);
        }
        for(int i = 0; i < bots.size() && isThereCollision; i++) {
            isThereCollision = !bots.get(i).getBotProperties().getObjectCoordinates().equals(newPlayerCoordinates) &&
                    !bots.get(i).getBotProperties().getObjectDestinationCoordinates().equals(newPlayerCoordinates);
        }

        return isThereCollision && isOnScreen(newPlayerCoordinates);
    }

    private boolean isThereBotCollision(Bot bot, MovementDirection direction) {
        boolean isThereBotCollision;
        GridPoint2 newBotCoordinates = bot.getBotProperties().getObjectCoordinates();
        if (direction == MovementDirection.UP)
            newBotCoordinates = incrementedY(bot.getBotProperties().getObjectCoordinates());
        if (direction == MovementDirection.DOWN)
            newBotCoordinates = decrementedY(bot.getBotProperties().getObjectCoordinates());
        if (direction == MovementDirection.RIGHT)
            newBotCoordinates = incrementedX(bot.getBotProperties().getObjectCoordinates());
        if (direction == MovementDirection.LEFT)
            newBotCoordinates = decrementedX(bot.getBotProperties().getObjectCoordinates());

        isThereBotCollision = !player.getPlayerProperties().getObjectCoordinates().equals(newBotCoordinates) &&
                !player.getPlayerProperties().getObjectDestinationCoordinates().equals(newBotCoordinates);
        for (int i = 0; i < trees.size() && isThereBotCollision; i++) {
            isThereBotCollision = !trees.get(i).getTreeObstacleCoordinates().equals(newBotCoordinates);
        }
        for(int i = 0; i < bots.size() && isThereBotCollision; i++) {
            isThereBotCollision = !bots.get(i).getBotProperties().getObjectCoordinates().equals(newBotCoordinates)&&
                    !bots.get(i).getBotProperties().getObjectDestinationCoordinates().equals(newBotCoordinates);
        }

        return isThereBotCollision && isOnScreen(newBotCoordinates);
    }

    private void calculatePlayerScreenCoordinates() {
        map.getTileMovement().moveRectangleBetweenTileCenters(
                player.getPlayerRectangle(),
                player.getPlayerProperties().getObjectCoordinates(),
                player.getPlayerProperties().getObjectDestinationCoordinates(),
                player.getPlayerProperties().getObjectMovementProgress());
    }

    private void calculateBotScreenCoordinates(Bot bot) {
        map.getTileMovement().moveRectangleBetweenTileCenters(
                bot.getBaseObjectRectangle(),
                bot.getBotProperties().getObjectCoordinates(),
                bot.getBotProperties().getObjectDestinationCoordinates(),
                bot.getBotProperties().getObjectMovementProgress());
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
        treesDispose();
        botsDispose();
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
