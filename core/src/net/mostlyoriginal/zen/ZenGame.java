package net.mostlyoriginal.zen;

import com.badlogic.gdx.Game;
import net.mostlyoriginal.zen.screen.SplashScreen;

/**
 * @author Daan van Yperen
 */
public class ZenGame extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }
}
