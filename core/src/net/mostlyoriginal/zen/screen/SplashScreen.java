package net.mostlyoriginal.zen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.Inputs;
import net.mostlyoriginal.zen.ZenGame;
import net.mostlyoriginal.zen.actors.FloatingSteve;
import net.mostlyoriginal.zen.actors.StaticScenery;
import net.mostlyoriginal.zen.actors.ui.KeyHint;

/**
 * @author Daan van Yperen
 */
public class SplashScreen extends ZenScreen {

    private float logoDelta;
    private float ignoreKeySeconds = 1.5f;
    private StaticScenery statue;
    private StaticScenery bonsai;
    private FloatingSteve floatingSteve;

    public SplashScreen(ZenGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {

        ignoreKeySeconds -= delta;

        logoDelta += delta;

        final GL20 gl = Gdx.gl20;
        if (gl != null) {
            gl.glClearColor(1, 1, 1, 1);
            gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(stage.getCamera().combined);
            batch.begin();

            batch.draw(Assets.background[0], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(Assets.logo, 0, (float)Math.sin(logoDelta * 2) * 5, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            Assets.fontSmall.setColor(1,1,1,1);
            Assets.fontSmall.draw(batch, "Ludum dare 26 - Daan van Yperen", 0, Gdx.graphics.getHeight() - 20);
            Assets.fontSmall.draw(batch, "v1.0.0.0", Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 20);

            if ( ignoreKeySeconds <= 0 )
            Assets.font.draw(batch, "Press       to start.", 64 * Assets.PIXEL_SCALE, 95 * Assets.PIXEL_SCALE );

            batch.end();

            stage.act(delta);
            stage.draw();
        }

        if (Inputs.escapeOnce&& (ignoreKeySeconds <= 0 ) )
        {
            Gdx.app.exit();
            return;
        }

        if (Inputs.attackOnce && (ignoreKeySeconds <= 0 ))
        {
            Inputs.attackOnce=false;
            setScreen(new GameplayScreen(game));
        }

        super.render(delta);
    }

    @Override
    public void show() {
        super.show();

        final Group keystructions = new Group();

        /*
        keystructions.addActor(new KeyHint("a", 10 * Assets.PIXEL_SCALE, 20* Assets.PIXEL_SCALE));
        keystructions.addActor(new KeyHint("d", 30 * Assets.PIXEL_SCALE, 20* Assets.PIXEL_SCALE));
        keystructions.addActor(new KeyHint("w", 20* Assets.PIXEL_SCALE, 10* Assets.PIXEL_SCALE));
        keystructions.addActor(new KeyHint("s", 20* Assets.PIXEL_SCALE, 30* Assets.PIXEL_SCALE)); */

        keystructions.addActor(new KeyHint("e", -34 * Assets.PIXEL_SCALE, 20 * Assets.PIXEL_SCALE));

        keystructions.setPosition( 110 * Assets.PIXEL_SCALE, 70 * Assets.PIXEL_SCALE);
        keystructions.setVisible(false);
        keystructions.addAction(Actions.delay(ignoreKeySeconds, Actions.show()));

        stage.addActor(keystructions);


        floatingSteve = new FloatingSteve(Assets.steve, 74 * Assets.PIXEL_SCALE, 82 * Assets.PIXEL_SCALE);
        stage.addActor(floatingSteve);
        floatingSteve.setZIndex(0);


        // statue
        statue = new StaticScenery(Assets.statue, 37 * Assets.PIXEL_SCALE, 80 * Assets.PIXEL_SCALE);
        stage.addActor(statue);

        // bonsai
        bonsai = new StaticScenery(Assets.bonsai, 124 * Assets.PIXEL_SCALE, 82 * Assets.PIXEL_SCALE);
        stage.addActor(bonsai);


    }
}
