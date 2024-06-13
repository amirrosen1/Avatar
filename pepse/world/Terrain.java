package pepse.world;

import danogl.util.Vector2;
import pepse.util.NoiseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import danogl.gui.rendering.RectangleRenderable;
import pepse.util.ColorSupplier;

/**
 * A class that creates a terrain object that will be rendered as a series of blocks
 */
public class Terrain {
    private static final String GROUND_TAG = "ground";
    private float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;
    private static final Color BASE_GROUND_COLOR  = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final int TERRAIN_NOISE_FACTOR = Block.SIZE * 7;
    private final int BLOCK_SIZE = Block.SIZE;


    /**
     * Creates a terrain object that will be rendered as a series of blocks
     * @param windowDimensions the dimensions of the window
     * @param seed the seed for the noise generator
     */
    public Terrain(Vector2 windowDimensions, int seed){
        groundHeightAtX0 = windowDimensions.y() * ((float) 2 /3);
        noiseGenerator = new NoiseGenerator(seed,(int)groundHeightAtX0);

    }

    /**
     * Returns the height of the terrain at a given x coordinate
     * @param x the x coordinate
     * @return the height of the terrain at the given x coordinate
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, TERRAIN_NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }


    /**
     * Creates a list of blocks within a given range
     * @param minX the minimum x value
     * @param maxX the maximum x value
     * @return a list of blocks
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();

        // Adjust minX and maxX to ensure coverage and align with BLOCK_SIZE
        int startX = Math.floorDiv(minX, BLOCK_SIZE) * BLOCK_SIZE;
        if (startX > minX) {
            startX -= BLOCK_SIZE;
        }
        int endX = Math.floorDiv(maxX, BLOCK_SIZE) * BLOCK_SIZE;
        if (endX < maxX) {
            endX += BLOCK_SIZE;
        }

        // Generate blocks column by column
        for (int x = startX; x <= endX; x += BLOCK_SIZE) {
            int topY = Math.floorDiv((int)groundHeightAt(x), BLOCK_SIZE) * BLOCK_SIZE;
            for (int y = topY; y < topY + TERRAIN_DEPTH * BLOCK_SIZE; y += BLOCK_SIZE) {
                Vector2 position = new Vector2(x, y);
                RectangleRenderable renderable  =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(position,renderable);
                block.setTag(GROUND_TAG);
                blocks.add(block);
            }
        }
        return blocks;
    }
}
