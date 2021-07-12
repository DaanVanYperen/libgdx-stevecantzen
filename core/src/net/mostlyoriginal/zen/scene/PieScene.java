package net.mostlyoriginal.zen.scene;

import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * @author Daan van Yperen
 */
public class PieScene extends Scene {

    public PieScene(String[] dialog) {
        super(dialog);
    }


    public void act(GameplayScreen screen, float delta) {
    }


    @Override
    public void sceneFinished(GameplayScreen screen) {
        screen.getSteve().setFrozen(false);
    }

    @Override
    public void updateProps(GameplayScreen screen, int index) {

        if (index == 3) {
            screen.getSteve().peekAtSurroundings();
        }
        if (index == 4)
        {
            screen.getSteve().setFrozen(true);
        }
        if (index == 5) {
            screen.getSteve().stopMeditating();
            screen.getSteve().displayCutlery();
            screen.getSteve().setFrozen(false);

            // make the pie a threat. DEATH PIE!
            screen.getPie().setPacifist(false);

            screen.addPieHint();
        }
    }
}
