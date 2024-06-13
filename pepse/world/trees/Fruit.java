package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that creates a fruit object that will be rendered on the tree
 */
public class Fruit extends GameObject {
    private String FRUIT_TAG = "fruit";
    private static final int FRUIT_SIZE = 10;
    private static final String AVATAR_TAG = "Avatar";
    private static final String JUMPED_TAG = "jumped";
    private static final float FRUIT_APPEARANCE_TIME = 30;
    private Color fruitColor = Color.RED;

    /**
     * Creates a fruit object that will be rendered on the tree
     * @param renderable the renderable object
     * @param positionX the x position of the fruit
     * @param positionY the y position of the fruit
     */
    public Fruit(OvalRenderable renderable, int positionX, int positionY){
        super(new Vector2(positionX, positionY), new Vector2(FRUIT_SIZE,FRUIT_SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * This method is called when a collision occurs with this GameObject.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals(AVATAR_TAG)){
           renderer().setOpaqueness(0);
            ScheduledTask task = new ScheduledTask(this,
                    FRUIT_APPEARANCE_TIME, false,
                    () -> {renderer().setOpaqueness(1);});
        }
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
        if (this.getTag().equals(JUMPED_TAG)){
            fruitColor = fruitColor.equals(Color.GREEN) ? Color.RED : Color.GREEN;
            OvalRenderable renderable = new OvalRenderable(fruitColor);
            renderer().setRenderable(renderable);
            this.setTag(FRUIT_TAG);
        }
    }
}
