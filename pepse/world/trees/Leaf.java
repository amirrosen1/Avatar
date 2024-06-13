package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;



import java.util.Random;

/**
 * A class that creates a leaf object

 */
public class Leaf extends GameObject {
    private static final int LEAF_SIZE = 15;
    private static final float MAX_DELAY = 0.8f;
    private static final float MIN_DELAY = 0.1f;
    private static final float INITIAl_ANGLE = 0;
    private static final float FINAL_ANGLE = 90;
    private static final float TRANSITION_TIME = 1;
    private static final float LEAF_INITIAL_WIDTH = -1;
    private static final float LEAF_FINAL_WIDTH = 1;
    private static final String LEAF_TAG = "leaf";
    private static final String JUMPED_TAG = "jumped";
    private Random random = new Random();

    /**
     * Constructor for the Leaf class
     * @param renderable the renderable object
     * @param positionX the x position of the leaf
     * @param positionY the y position of the leaf
     * @param size the size of the leaf
     */
    public Leaf(RectangleRenderable renderable, int positionX, int positionY, int size) {
        super(new Vector2(positionX, positionY), new Vector2(size, size), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * This method is called when a collision occurs with this GameObject.
     * @param leaf The GameObject with which a collision occurred.
     */
    private static void changeLeafWidth(Leaf leaf) {
        Transition<Float> widthLeafTtansition = new Transition<>(leaf,
                width -> leaf.setDimensions(new Vector2(LEAF_SIZE, LEAF_SIZE)),
                LEAF_INITIAL_WIDTH, LEAF_FINAL_WIDTH,
                Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE, null);
    }

    /**
     * Changes the angle of the leaf
     * @param leaf the leaf to change
     */
    private static void changeLeafAngle(Leaf leaf) {
        Transition<Float> angleLeafTtansition = new Transition<>(leaf,
                angle -> leaf.renderer().setRenderableAngle(angle), INITIAl_ANGLE, FINAL_ANGLE,
                Transition.CUBIC_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE, null);
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
            float delay = MIN_DELAY + random.nextFloat() * (MAX_DELAY - MIN_DELAY);
            new ScheduledTask(this, delay, false,
                    () -> {changeLeafAngle(this);
                changeLeafWidth(this);});
            this.setTag(LEAF_TAG);
        }
    }
}
