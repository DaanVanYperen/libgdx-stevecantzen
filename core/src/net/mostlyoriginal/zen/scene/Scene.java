package net.mostlyoriginal.zen.scene;

import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * @author Daan van Yperen
 */
public class Scene {

    public final String[] dialog;

    public Scene(String[] dialog) {
        this.dialog = dialog;
    }

    public void updateProps( GameplayScreen screen, int index )
    {

    }

    public void sceneFinished( GameplayScreen screen ) {}

    public void act(GameplayScreen screen, float delta) {
    }
}
