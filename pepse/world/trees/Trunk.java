package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that creates a trunk object that will be rendered as a rectangle
 */
public class Trunk extends GameObject {

    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final int BASE_RED = 100;
    private static final int BASE_GREEN = 50;
    private static final int BASE_BLUE = 20;
    private static final int RANGE = 30;
    private static final int MAX_COLOR = 255;
    private static final int MIN_COLOR = 0;
    private static final String JUMPED_TAG = "jumped";
    private static final String TRUNK_TAG = "trunk";
    private int topPositionX;
    private int topPositionY;
    private Color trunkColor = new Color(100, 50, 20);


    /**
     * Creates a trunk object that will be rendered as a rectangle
     * @param position the position of the trunk
     * @param dimension the dimension of the trunk
     * @param renderable the renderable object
     */
    public Trunk(Vector2 position, Vector2 dimension,RectangleRenderable renderable) {
        super(position, dimension, renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        topPositionX = (int)position.x();
        topPositionY = (int)position.y();
    }

    /**
     * Gets the x position of the top of the trunk
     * @return the x position of the top of the trunk
     */
    public int getTopPositionX() {
        return topPositionX;
    }

    /**
     * Gets the y position of the top of the trunk
     * @return the y position of the top of the trunk
     */
    public int getTopPositionY() {
        return topPositionY;
    }

    /**
     * Generates a random brown color
     * @return a random brown color
     */
    private Color randomBrownColor() {
        int baseRed = BASE_RED, baseGreen = BASE_GREEN, baseBlue = BASE_BLUE;
        int range = RANGE;
        int red = Math.min(MAX_COLOR, baseRed + (int) (Math.random() * range));
        int green = Math.min(MAX_COLOR, baseGreen + (int) (Math.random() * range) - 15);
        int blue = Math.max(MIN_COLOR, baseBlue + (int) (Math.random() * range) - 30);
        return new Color(red, green, blue);
    }


    /**
     * This method is called every frame to update the GameObject's state.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getTag().equals(JUMPED_TAG)) {
            trunkColor = trunkColor.equals(TRUNK_COLOR) ? randomBrownColor() : TRUNK_COLOR;
            RectangleRenderable renderable = new RectangleRenderable(trunkColor);
            renderer().setRenderable(renderable);
            this.setTag(TRUNK_TAG);
        }
    }
}
