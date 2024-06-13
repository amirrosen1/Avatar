package pepse.world.daynight;

import danogl.GameObject;
import java.awt.Color;
import danogl.gui.rendering.OvalRenderable;

/**
 * A class that creates a sun halo object that will be rendered around the sun
 */
public class SunHalo {

    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final String SUN_HALO_TAG = "sun Halo";
    private static final float SUN_HALO_SIZE_FACTOR = 1.5f;

    /**
     * Creates a sun halo object that will be rendered around the sun
     * @param sun the sun object
     * @return the sun halo object
     */
    public static GameObject create(GameObject sun){
        OvalRenderable haloRenderable = new OvalRenderable(HALO_COLOR);
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(),
                sun.getDimensions().mult(SUN_HALO_SIZE_FACTOR),
                haloRenderable);
        sunHalo.setTag(SUN_HALO_TAG);
        return sunHalo;
    }
}
