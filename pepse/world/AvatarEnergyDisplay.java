package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

/**
 * A class that creates an energy display object that will be rendered as a text

 */
public class AvatarEnergyDisplay extends GameObject {
    private Supplier<Double> energySupplier;
    private  double presentedEnergy;

    /**
     * Creates an energy display object that will be rendered as a text
     * @param energySupplier the supplier of the energy
     * @param topLeftCorner the top left corner of the energy display
     * @param dimensions the dimensions of the energy display
     */
    public AvatarEnergyDisplay(Supplier<Double> energySupplier, Vector2 topLeftCorner,
                               Vector2 dimensions){
        super(topLeftCorner,dimensions,new TextRenderable(String.format("%.0f%s",
                energySupplier.get(),"%")));
        this.energySupplier = energySupplier;
        this.presentedEnergy = energySupplier.get();
    }

    /**
     * Updates the energy display
     * @param deltaTime the time since the last update
     */
    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        double curEnergy = energySupplier.get();
        if(presentedEnergy != curEnergy){
            renderer().setRenderable( new TextRenderable(String.format("%.0f%s", curEnergy,"%")));
            presentedEnergy = curEnergy;
        }
    }
}
