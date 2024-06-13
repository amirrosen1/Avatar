package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that creates a night object that will transition from transparent to opaque and back
 */
public class Night {

    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final String NIGHT_TAG = "night";
    private static final float INITIAL_OPAQUENESS = 0f;

    /**
     * Creates a night object that will transition from transparent to opaque and back
     * @param windowDimensions the dimensions of the window
     * @param cycleLength the time it takes for the night to go from transparent to opaque and back
     * @return the night object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        RectangleRenderable nightRenderable =
                new RectangleRenderable(pepse.util.ColorSupplier.approximateColor(Color.BLACK));
        GameObject night = new GameObject(Vector2.LEFT, windowDimensions, nightRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        Transition<Float> nightTransition =
        new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                INITIAL_OPAQUENESS,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength/2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        return night;
    }
}
