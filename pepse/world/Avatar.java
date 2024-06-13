package pepse.world;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * A class that creates an avatar object that will be controlled by the user
 */
public class Avatar extends GameObject {
    private static final String JUMPED_TAG = "jumped";
    private static final float VELOCITY_X = 200;
    private static final float VELOCITY_Y = -200;
    private static final float GRAVITY = 150;
    private static final int MAX_ENERGY = 100;
    private static final int FRUIT_ADDITIONAL_ENERGY = 10;
    private static final String FRUIT_TAG = "fruit";
    private static final String[] IDLE_PATHS = {"assets/idle_0.png", "assets/idle_1.png",
            "assets/idle_2.png", "assets/idle_3" +
            ".png"};
    private static final String[] RUN_PATHS = {"assets/run_0.png", "assets/run_1.png",
            "assets/run_2.png", "assets/run_3" +
            ".png"};
    private static final String[] JUMP_PATHS = {"assets/jump_0.png", "assets/jump_1.png",
            "assets/jump_2.png", "assets/jump_3" +
            ".png"};
    private static final String ASSETS_IDLE_0_PNG = "assets/idle_0.png";
    private static final float TIME_BETWEEN_CLIPS = 0.1f;
    private static final int AVATAR_SIZE = 50;
    private double energy = 100;
    private  double JUMP_ENERGY = 10;
    private  double WALK_ENERGY = 0.5;
    private UserInputListener inputListener;
    private AnimationRenderable idleAnimation;
    private AnimationRenderable runAnimation;
    private AnimationRenderable jumpAnimation;
    private  ImageReader imageReader;
    private ArrayList<GameObject> observers = new ArrayList<>();


    /**
     * Creates an avatar object that will be controlled by the user
     * @param pos the position of the avatar
     * @param inputListener the input listener
     * @param imageReader the image reader
     */
    public Avatar(Vector2 pos,UserInputListener inputListener, ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(AVATAR_SIZE), imageReader.readImage(ASSETS_IDLE_0_PNG, true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        String[] idlePaths = IDLE_PATHS;
        String[] runPaths = RUN_PATHS;
        String[] jumpPaths = JUMP_PATHS;
        idleAnimation = new AnimationRenderable(idlePaths,
                imageReader,true, TIME_BETWEEN_CLIPS);
        runAnimation = new AnimationRenderable(runPaths,
                imageReader,true,TIME_BETWEEN_CLIPS);
        jumpAnimation = new AnimationRenderable(jumpPaths,
                imageReader,true,TIME_BETWEEN_CLIPS);
    }


    /**
     * Gets the energy of the avatar
     * @return the energy of the avatar
     */

    public double getEnergy(){
        return energy;
    }

    /**
     * Registers an observer to the avatar to be notified when the avatar jumps
     * @param observer the observer to be registered
     */
    public void registerToJumpUpdates(GameObject observer){
        observers.add(observer);
    }

    /**
     * Notifies the observers that the avatar has jumped
     */
    private void notifyObserversAboutJump(){
        for(GameObject observer: observers){
            observer.setTag(JUMPED_TAG);
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
        float xVel = 0;
        if(energy < MAX_ENERGY && transform().getVelocity().y() == 0
                && transform().getVelocity().x() == 0){
            energy = energy + 1 < MAX_ENERGY ? energy + 1 : MAX_ENERGY;
            renderer().setRenderable(idleAnimation);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            if(energy > WALK_ENERGY){
                xVel -= VELOCITY_X;
                energy = energy - WALK_ENERGY > 0 ? energy - WALK_ENERGY: 0;
                renderer().setRenderable(runAnimation);
                renderer().setIsFlippedHorizontally(true);
            }
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            if(energy > WALK_ENERGY){
                xVel += VELOCITY_X;
                energy = energy - WALK_ENERGY > 0 ? energy - WALK_ENERGY: 0;
                renderer().setRenderable(runAnimation);
                renderer().setIsFlippedHorizontally(false);
            }
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            if(energy > JUMP_ENERGY){
                transform().setVelocityY(VELOCITY_Y);
                energy = energy - JUMP_ENERGY > 0 ? energy - JUMP_ENERGY: 0;
                renderer().setRenderable(jumpAnimation);
                notifyObserversAboutJump();
            }
        }
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
        if(other.getTag().equals(FRUIT_TAG) && other.renderer().getOpaqueness() == 1){
            energy  = energy + FRUIT_ADDITIONAL_ENERGY < MAX_ENERGY ? energy +
                    FRUIT_ADDITIONAL_ENERGY : MAX_ENERGY;
        }
    }
}
