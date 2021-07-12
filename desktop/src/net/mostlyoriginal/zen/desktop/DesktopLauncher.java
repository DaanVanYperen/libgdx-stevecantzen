package net.mostlyoriginal.zen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.ZenGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Steve Can't Zen";
        config.width = 160 * Assets.PIXEL_SCALE;
        config.height = 120 * Assets.PIXEL_SCALE;
        config.fullscreen = false;
        new LwjglApplication(new ZenGame(), config);
    }
}
