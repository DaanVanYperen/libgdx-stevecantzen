package net.mostlyoriginal.zen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.Inputs;
import net.mostlyoriginal.zen.ZenGame;

/**
 * @author Daan van Yperen
 */
public abstract class ZenScreen implements Screen {

    protected ZenGame game;

    protected Stage stage;
    protected SpriteBatch batch;

    private ZenScreen() { }
    public ZenScreen(ZenGame game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Inputs.reset();
    }

    /**
     * Utility method to switch to different screen, disposing the active one.
     *
     * @param screen
     */
    public void setScreen( ZenScreen screen )
    {
        final Screen oldScreen = game.getScreen();
        if (oldScreen != null)
        {
            oldScreen.dispose();
            game.setScreen(null);
        }

        if ( screen != null ) screen.game=game;
        game.setScreen(screen);
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        updateCamera();

        Assets.loadResources();

        Inputs.hookup();
    }

    private void updateCamera() {

        // flip from Y-up to Y-down, prefer it that way.
        OrthographicCamera camera = (OrthographicCamera)stage.getCamera();
        camera.setToOrtho(true);
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        updateCamera();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        Assets.dispose();
        if ( batch != null ) batch.dispose(); batch = null;
    }
}
