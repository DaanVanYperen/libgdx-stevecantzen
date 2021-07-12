package net.mostlyoriginal.zen.scene;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.screen.GameplayScreen;
import net.mostlyoriginal.zen.actors.Deer;
import net.mostlyoriginal.zen.actors.StaticScenery;

/**
 * @author Daan van Yperen
 */
public class DeerScene extends Scene {

    public static final float SHAKE = 5f;
    public static final int DEER_WALKIN_DELAY = 6;
    public static final int DEER_MEETUP_DELAY = 2;
    private Deer[] deer = new Deer[2];

    private StaticScenery censored;
    private StaticScenery heart;

    private float shake = SHAKE;

    public DeerScene(String[] dialog) {
        super(dialog);
    }


    @Override
    public void act(GameplayScreen screen, float delta) {
        if (censored != null) {
            shake -= delta * 10f;
            if (shake < 0) shake = SHAKE;

            censored.setPosition(82 * Assets.PIXEL_SCALE + MathUtils.random(-shake, shake), 83 * Assets.PIXEL_SCALE + MathUtils.random(-shake, shake));
        }

    }


    @Override
    public void sceneFinished(GameplayScreen screen) {

        screen.getSteve().setFrozen(false);

        // spread the deer so they are a tad more interesting to kill.
        deer[0].addAction(Actions.moveTo(110 * Assets.PIXEL_SCALE, 86 * Assets.PIXEL_SCALE, 6));
        deer[1].addAction(Actions.moveTo(94 * Assets.PIXEL_SCALE, 72 * Assets.PIXEL_SCALE, 6));

        for (Deer d : deer) {
            d.setPacifist(false);
        }

        spawnDeerWave(screen);

        cleanup();
    }

    private void spawnDeerWave(GameplayScreen screen) {
        // deer wave system. It's awesome!
        for (int i = 0; i < 40; i++) {
            final boolean side = MathUtils.randomBoolean();
            Deer waveDeer = new Deer(Assets.deer, Assets.deerGibs,
                    side ? -50 * Assets.PIXEL_SCALE : 150 * Assets.PIXEL_SCALE, 86 * Assets.PIXEL_SCALE);
            waveDeer.setXConstrained(false);
            waveDeer.setPacifist(false);
            waveDeer.setVisible(false);
            waveDeer.addAction(Actions.delay(i * 1.5f + MathUtils.random(0f, 3f), Actions.show()));
            //waveDeer.addAction(Actions.delay(i * 3, Actions.moveTo(side ? 40 * Assets.PIXEL_SCALE : 110 * Assets.PIXEL_SCALE, 86 * Assets.PIXEL_SCALE, DEER_WALKIN_DELAY)));

            screen.getStage().addActor(waveDeer);
        }
    }

    private void cleanup() {
        // oh gosh mixing state with static stuff. oops!
        if (censored != null) censored.remove();
        if (heart != null) heart.remove();
        censored = heart = null;
    }

    @Override
    public void updateProps(GameplayScreen screen, int index) {

        // prep the deer.
        if (index == 0) {
            cleanup();

            // spawn outside screen.
            deer[0] = new Deer(Assets.deer, Assets.deerGibs, 50 * Assets.PIXEL_SCALE + 110 * Assets.PIXEL_SCALE, 86 * Assets.PIXEL_SCALE);
            deer[1] = new Deer(Assets.deer, Assets.deerGibs, 50 * Assets.PIXEL_SCALE + 94 * Assets.PIXEL_SCALE, 72 * Assets.PIXEL_SCALE);

            deer[0].setXConstrained(false);
            deer[1].setXConstrained(false);

            deer[0].setPacifist(true);
            deer[1].setPacifist(true);

            // deer walk in.
            deer[0].addAction(Actions.moveTo(110 * Assets.PIXEL_SCALE, 86 * Assets.PIXEL_SCALE, DEER_WALKIN_DELAY));
            deer[1].addAction(Actions.moveTo(93 * Assets.PIXEL_SCALE, 72 * Assets.PIXEL_SCALE, DEER_WALKIN_DELAY));
            // extra move will flip it.
            deer[1].addAction(Actions.delay(DEER_WALKIN_DELAY, Actions.moveTo(94 * Assets.PIXEL_SCALE, 72 * Assets.PIXEL_SCALE, 0.1f)));

            screen.getStage().addActor(deer[0]);
            screen.getStage().addActor(deer[1]);
        }

        // sweet deer love.
        if (index == 2) {
            screen.getSteve().setFrozen(true);
            deer[0].addAction(Actions.moveTo(98 * Assets.PIXEL_SCALE, 80 * Assets.PIXEL_SCALE, DEER_MEETUP_DELAY));
            deer[1].addAction(Actions.moveTo(98 * Assets.PIXEL_SCALE, 80 * Assets.PIXEL_SCALE, DEER_MEETUP_DELAY));

            censored = new StaticScenery(Assets.censored, 85 * Assets.PIXEL_SCALE, 81 * Assets.PIXEL_SCALE);
            censored.setVisible(false);
            censored.addAction(Actions.delay(DEER_MEETUP_DELAY, Actions.show()));

            heart = new StaticScenery(Assets.heart, 90 * Assets.PIXEL_SCALE, 65 * Assets.PIXEL_SCALE);

            screen.getStage().addActor(censored);
            screen.getStage().addActor(heart);
        }

        if (index == 3) {
            screen.getSteve().peekAtSurroundings();
            Assets.deerOhLaLa.play();
        }

        if (index == 5) {
            screen.getSteve().stopMeditating();
            screen.getSteve().rage();
        }
    }
}
