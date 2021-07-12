package net.mostlyoriginal.zen.scene;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * @author Daan van Yperen
 */
public class DeathScene extends Scene {

    public DeathScene(String[] dialog) {
        super(dialog);
    }


    public void act( GameplayScreen screen, float delta )
    {
    }


    @Override
    public void sceneFinished(GameplayScreen screen) {
        screen.getSteve().setFrozen(false);

        // float steve to the heavens.
        screen.getSteve().addAction(Actions.moveTo(screen.getSteve().getX(), -150, 10, Interpolation.pow2));
        screen.getSteve().addAction(Actions.rotateBy(1200, 10,Interpolation.pow2));
        screen.getSteve().setYConstrained(false);
        screen.getSteve().setXConstrained(false);
        screen.getSteve().setAscending(true);
    }

    @Override
    public void updateProps(GameplayScreen screen, int index) {

        if ( index==0 )
        {
            screen.getSteve().setFrozen(true);
        }
    }
}
