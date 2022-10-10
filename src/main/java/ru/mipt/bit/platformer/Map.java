package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class Map {

    public Map() {}

    public Map (String mapPath, Batch batch) {
        level = new TmxMapLoader().load(mapPath);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public void setGroundLayer(TiledMapTileLayer groundLayer) {
        this.groundLayer = groundLayer;
    }

    public void setLevel(TiledMap level) {
        this.level = level;
    }

    public void setLevelRenderer(MapRenderer levelRenderer) {
        this.levelRenderer = levelRenderer;
    }

    public void setTileMovement(TileMovement tileMovement) {
        this.tileMovement = tileMovement;
    }

    public TiledMap getLevel() {
        return level;
    }

    public MapRenderer getLevelRenderer() {
        return levelRenderer;
    }

    public TileMovement getTileMovement() {
        return tileMovement;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private TiledMapTileLayer groundLayer;
}
