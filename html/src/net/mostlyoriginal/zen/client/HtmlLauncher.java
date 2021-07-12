package net.mostlyoriginal.zen.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.ZenGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(160 * Assets.PIXEL_SCALE, 120 * Assets.PIXEL_SCALE);
                return config;
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new ZenGame();
        }
}