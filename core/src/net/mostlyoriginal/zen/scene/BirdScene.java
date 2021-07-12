package net.mostlyoriginal.zen.scene;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.actors.PreyBird;
import net.mostlyoriginal.zen.actors.SongBird;
import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * @author Daan van Yperen
 */
public class BirdScene extends Scene {

    private SongBird songBird;
    private PreyBird preyBird;
    private boolean songBirdGlued;

    public BirdScene(String[] dialog) {
        super(dialog);
    }

    @Override
    public void act(GameplayScreen screen, float delta) {

        moveSongbirdToPlayer(screen);
    }

    @Override
    public void sceneFinished(GameplayScreen screen) {
        screen.getSteve().setFrozen(false);
        screen.getSteve().rage();
        cleanup();


    }

    private void cleanup() {
        // oh gosh mixing state with static stuff. oops!
        if (songBird != null) songBird.remove();
        songBird = null;
        songBirdGlued = false;
    }

    @Override
    public void updateProps(GameplayScreen screen, int index) {

        final Stage stage = screen.getStage();

        if (index == 0) cleanup();

        if (index == 1) {
            songBird = new SongBird(Assets.songbird, 160 * Assets.PIXEL_SCALE, 60 * Assets.PIXEL_SCALE);
            songBird.setXConstrained(false);
            songBird.setYConstrained(false);
            songBird.setPacifist(true);

            songBird.addAction(Actions.moveTo(-60 * Assets.PIXEL_SCALE, 60 * Assets.PIXEL_SCALE, 4, Interpolation.pow2));

            stage.addActor(songBird);
            Assets.birdTweet.play();
        }

        // prep the songbird.
        if (index == 2) {
            songBird.addAction(Actions.moveTo(screen.getSteve().getX() - 10, screen.getSteve().getY() + 13, 2, Interpolation.pow2Out));
            songBirdGlued = true;
            Assets.birdTweet.play();
        }

        if (index == 3) {
            screen.getSteve().setFrozen(true);
            screen.getSteve().peekAtSurroundings();
        }
        if (index == 5) {
            replaceSongbirdWithPreybird(screen);

        // prey bird wave system. It's awesome! and possibly copied from the deer one, teehee!
        for (int i = 0; i < 30; i++) {
            final boolean side = MathUtils.randomBoolean();
            PreyBird waveBird = new PreyBird(Assets.preybird, Assets.preybirdGibs,
                    side ? -50 * Assets.PIXEL_SCALE : 150 * Assets.PIXEL_SCALE, 80 * Assets.PIXEL_SCALE);

            // goal is to swoop down. not a lot of swooping going on yet though.
            waveBird.setY(200);
            waveBird.setXConstrained(false);
            waveBird.setPacifist(false);
            waveBird.setVisible(false);
            waveBird.addAction(Actions.delay(i * 1.5f + MathUtils.random(0f, 3f), Actions.show()));

            screen.getStage().addActor(waveBird);
        }
        }
    }

    private void replaceSongbirdWithPreybird(GameplayScreen screen) {

        preyBird = new PreyBird(Assets.preybird, Assets.preybirdGibs, (int) songBird.getX(), (int) songBird.getY());
        screen.getStage().addActor(preyBird);
        preyBird.attack();

        screen.getSteve().rage();

        if (songBird != null) songBird.remove();
        songBird = null;
    }

    private void moveSongbirdToPlayer(GameplayScreen screen) {
        if (songBird != null && songBird.getActions().size == 0 && songBirdGlued) {
            songBird.setPosition(screen.getSteve().getX() - 10, screen.getSteve().getY() + 13 - (screen.getSteve().isMeditating() ? 0 : 4));
        }
    }
}
