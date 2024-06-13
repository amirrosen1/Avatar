package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

/**
 * A class that creates a sun object that will transition from one side of the screen to the other
 */
public class Sun {
    private static final Vector2 SUN_DIMENSIONS = new Vector2(100, 100);
    private static final String SUN_TAG = "sun";
    private static final float INITIAL_ROTATION_ANGLE = 0f;
    private static final float FINAL_ROTATION_ANGLE = 360f;

    /**
     * Creates a sun object that will transition from one side of the screen to the other
     * @param windowDimensions the dimensions of the window
     * @param cycleLength the time it takes for the sun to go from one side of the screen to
     *                    the other
     * @return the sun object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        Color sunColor = Color.YELLOW;
        OvalRenderable sunRenderable = new OvalRenderable(sunColor);

        Vector2 initialSunCenter = new Vector2(windowDimensions.x()/2, windowDimensions.y()/2);
        Vector2 sunDimensions = SUN_DIMENSIONS;
        GameObject sun = new GameObject(initialSunCenter, sunDimensions , sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        Vector2 cycleCenter = new Vector2(windowDimensions.x()/2, windowDimensions.y()*(2/3f));
        Transition<Float> sunTransition = new Transition<>(
                sun,
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter).
                        rotated(angle).add(cycleCenter)),
                INITIAL_ROTATION_ANGLE,
                FINAL_ROTATION_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }
}
