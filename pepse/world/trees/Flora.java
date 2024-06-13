package pepse.world.trees;

import pepse.world.Terrain;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class that creates a flora object
 */
public class Flora {
    private static final String TAG_TRUNK = "trunk";
    private static final String TAG_LEAF = "leaf";
    private static final String TAG_FRUIT = "fruit";
    private static final int MIN_HEIGHT = 100;
    private static final int MAX_HEIGHT = 150;
    private static final int LEAF_FRUIT_AREA_SIZE = 60;
    private static final int LEAF_SIZE = 15;
    private static final int LEAF_DENSITY = 12;
    private static final int FRUIT_DENSITY = 20;
    private static final int BLOCK_SIZE = 30;
    private static final int SPACE_BETWEEN_TREES = 150;
    private static final int WIDTH_OF_TRUNK = 15;
    private static final float PROBABILITY_OF_TREE = 0.6f;
    private static final float MAX_DELAY = 0.8f;
    private static final float MIN_DELAY = 0.1f;
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final float INITIAL_LEAF_WIDTH = -1f;
    private static final float FINAL_LEAF_WIDTH = 1f;
    private static final float INITIAL_LEAF_ANGLE = 0f;
    private static final float FINIAL_LEAF_ANGLE = 10f;
    private static final float TRANSITION_TIME_WIDTH = 2.5f;
    private static final float TRANSITION_TIME_ANGLE = 2;
    private final Terrain terrain;
    private final Random random = new Random();

    /**
     * Constructor for the Flora class
     * @param windowDimension the window dimension
     * @param terrain the terrain object, used to determine the height of the trees
     */
    public Flora(Vector2 windowDimension, Terrain terrain) {
        this.terrain = terrain;
    }


    /**
     * Creates a list of trees within a given range
     * @param minX the minimum x value
     * @param maxX the maximum x value
     * @return a list of trunks
     */
    public ArrayList<Trunk> createInRange(int minX, int maxX) {
        ArrayList<Trunk> trees = new ArrayList<>();
        for (int x = minX; x < maxX; x += SPACE_BETWEEN_TREES) {
            if (random.nextFloat() < PROBABILITY_OF_TREE) {
                float  topY =
                        Math.floorDiv((int)terrain.groundHeightAt(x), BLOCK_SIZE) * BLOCK_SIZE;
                int height = random.nextInt(MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT;
                float groundHeight = topY - height;
                Vector2 position = new Vector2(x, groundHeight);
                Vector2 dimensions = new Vector2(WIDTH_OF_TRUNK,height);
                RectangleRenderable renderable = new RectangleRenderable(TRUNK_COLOR);
                Trunk tree = new Trunk(position, dimensions, renderable);
                tree.setTag(TAG_TRUNK);
                trees.add(tree);
            }
        }
        return trees;
    }


    /**
     * Creates a list of leaves within a given range
     * @param atTopPositionX the x position of the top of the tree
     * @param atTopPositionY the y position of the top of the tree
     * @return a list of leaves
     */
    public ArrayList<Leaf> createLeafInRange(int atTopPositionX, int atTopPositionY) {
        ArrayList<Leaf> leaves = new ArrayList<>();
        for (int x = atTopPositionX - LEAF_FRUIT_AREA_SIZE / 2; x < atTopPositionX
                + LEAF_FRUIT_AREA_SIZE / 2; x += LEAF_DENSITY) {
            for (int y = atTopPositionY - LEAF_FRUIT_AREA_SIZE / 2; y < atTopPositionY
                    + LEAF_FRUIT_AREA_SIZE / 2; y += LEAF_DENSITY) {
                if (random.nextBoolean()) {
                    RectangleRenderable renderable = new RectangleRenderable(LEAF_COLOR);
                    Leaf leaf = new Leaf(renderable, x, y, LEAF_SIZE);
                    float delay = MIN_DELAY + random.nextFloat() * (MAX_DELAY - MIN_DELAY);
                    new ScheduledTask(leaf, delay, false, () -> {changeLeafAngle(leaf);
                    changeLeafWidth(leaf);});
                    leaf.setTag(TAG_LEAF);
                    leaves.add(leaf);
            }
        }
    }
        return leaves;
    }

    /**
     * Creates a list of fruits within a given range
     * @param atTopPositionX the x position of the top of the tree
     * @param atTopPositionY the y position of the top of the tree
     * @return a list of fruits
     */
    public ArrayList<Fruit> createFruitsInRange(int atTopPositionX, int atTopPositionY) {
        ArrayList<Fruit> fruits = new ArrayList<>();
        for (int x = atTopPositionX - LEAF_FRUIT_AREA_SIZE / 2; x < atTopPositionX
                + LEAF_FRUIT_AREA_SIZE / 2; x += FRUIT_DENSITY) {
            for (int y = atTopPositionY - LEAF_FRUIT_AREA_SIZE / 2; y < atTopPositionY
                    + LEAF_FRUIT_AREA_SIZE / 2; y += FRUIT_DENSITY) {
                if (random.nextBoolean()) {
                    OvalRenderable renderable = new OvalRenderable(Color.red);
                    Fruit newFruit = new Fruit(renderable,x,y);
                    newFruit.setTag(TAG_FRUIT);
                    fruits.add(newFruit);
                }
            }
        }
        return fruits;
    }

    /**
     * Changes the width of the leaf
     * @param leaf the leaf to change
     */
    private static void changeLeafWidth(Leaf leaf) {
        Transition<Float> widthLeafTtansition = new Transition<>(leaf,
                width -> leaf.setDimensions(new Vector2(LEAF_SIZE, LEAF_SIZE)),
                INITIAL_LEAF_WIDTH, FINAL_LEAF_WIDTH,
                Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME_WIDTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }


    /**
     * Changes the angle of the leaf
     * @param leaf the leaf to change
     */
    private static void changeLeafAngle(Leaf leaf) {
        Transition<Float> angleLeafTtansition = new Transition<>(leaf,
                angle -> leaf.renderer().setRenderableAngle(angle),
                INITIAL_LEAF_ANGLE, FINIAL_LEAF_ANGLE,
                Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME_ANGLE,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }
}
