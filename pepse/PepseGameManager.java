package pepse;

import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;

import java.util.List;

/**
 * The main class of the game. This class is responsible for initializing the game and
 * creating the game objects.
 */
public class PepseGameManager extends GameManager {
    private static final String AVATAR_TAG = "Avatar";
    private static final int MIN_X = 30;
    private static final Vector2 ENERGY_DISPLAY_LOC = new Vector2(0, 0);
    private static final Vector2 ENERGY_DISPLAY_SIZE = new Vector2(50, 50);
    private static final int SEED = 0;
    private static final int FRAME_RATE = 25;
    private float CYCLE_LENGTH = 30;
    private Avatar avatar;

    /**
     * The main method of the game. It creates a new instance of the game manager and runs it.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game. This method is called once at the beginning of the game.
     *
     * @param imageReader      Contains a single method: readImage, which reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which returns whether
     *                         a given key is currently pressed by the user or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(FRAME_RATE);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        createSky(windowDimensions);

        //create terrain
        Terrain terrain = createTerrain(windowDimensions);

        //Create night
        createNight(windowDimensions);

        //create sun
        GameObject sun = createSun(windowDimensions);

        //create sun halo
        createSunHalo(sun);

        //create avatar
        createAvatar(imageReader, inputListener, windowDimensions);

        //create energy display
        createEnergyDisplay();

        //create trees and leaves
        createTrees(windowDimensions, terrain);
    }

    /**
     * Creates the trees and leaves in the game
     *
     * @param windowDimensions the window dimensions
     * @param terrain          the terrain
     */
    private void createTrees(Vector2 windowDimensions, Terrain terrain) {
        Flora flora = new Flora(windowDimensions, terrain);
        List<Trunk> trunks = flora.createInRange(MIN_X, (int) windowDimensions.x() - MIN_X);
        for (Trunk trunk : trunks) {
            avatar.registerToJumpUpdates(trunk);
            gameObjects().addGameObject(trunk, Layer.STATIC_OBJECTS);
            List<Leaf> leaves = flora.createLeafInRange(trunk.getTopPositionX(),
                    trunk.getTopPositionY());
            for (Leaf leaf : leaves) {
                avatar.registerToJumpUpdates(leaf);
                gameObjects().addGameObject(leaf, Layer.STATIC_OBJECTS + 1);
            }
            List<Fruit> fruits = flora.createFruitsInRange(trunk.getTopPositionX(),
                    trunk.getTopPositionY());
            for (Fruit fruit : fruits) {
                avatar.registerToJumpUpdates(fruit);
                gameObjects().addGameObject(fruit);
            }
        }
    }

    /**
     * Creates the energy display in the game
     */
    private void createEnergyDisplay() {
        Vector2 energyDisplayLoc = ENERGY_DISPLAY_LOC;
        Vector2 energyDisplaySize = ENERGY_DISPLAY_SIZE;
        GameObject energyDisplay = new AvatarEnergyDisplay(avatar::getEnergy, energyDisplayLoc,
                energyDisplaySize);
        gameObjects().addGameObject(energyDisplay);
    }

    /**
     * Creates the avatar in the game
     *
     * @param imageReader      the image reader
     * @param inputListener    the input listener
     * @param windowDimensions the window dimensions
     */
    private void createAvatar(ImageReader imageReader, UserInputListener inputListener,
                              Vector2 windowDimensions) {
        Vector2 avatarPos = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        this.avatar = new Avatar(avatarPos, inputListener, imageReader);
        this.avatar.setTag(AVATAR_TAG);
        gameObjects().addGameObject(avatar);
    }

    /**
     * Creates the sun halo in the game
     *
     * @param sun the sun
     */
    private void createSunHalo(GameObject sun) {
        GameObject sunHalo = SunHalo.create(sun);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    /**
     * Creates the sun in the game
     *
     * @param windowDimensions the window dimensions
     * @return the sun
     */
    private GameObject createSun(Vector2 windowDimensions) {
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);
        return sun;
    }

    /**
     * Creates the night in the game
     *
     * @param windowDimensions the window dimensions
     */
    private void createNight(Vector2 windowDimensions) {
        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND);
    }

    /**
     * Creates the terrain in the game
     *
     * @param windowDimensions the window dimensions
     * @return the terrain
     */
    private Terrain createTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(windowDimensions, SEED);
        List<Block> blocks = terrain.createInRange(0, (int) windowDimensions.x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block);
        }
        return terrain;
    }

    /**
     * Creates the sky in the game
     *
     * @param windowDimensions the window dimensions
     */
    private void createSky(Vector2 windowDimensions) {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }
}
