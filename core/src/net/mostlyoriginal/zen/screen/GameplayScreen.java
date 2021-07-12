package net.mostlyoriginal.zen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.Inputs;
import net.mostlyoriginal.zen.Settings;
import net.mostlyoriginal.zen.ZenGame;
import net.mostlyoriginal.zen.actors.*;
import net.mostlyoriginal.zen.actors.ui.KeyHint;
import net.mostlyoriginal.zen.actors.ui.Score;
import net.mostlyoriginal.zen.actors.ui.ZenProgress;
import net.mostlyoriginal.zen.scene.Scene;
import net.mostlyoriginal.zen.scene.SceneHandler;

import java.util.Comparator;

/**
 * @author Daan van Yperen
 */
public class GameplayScreen extends ZenScreen {

    public static final int MIN_ELEVATION = 1; // waking state.
    public static final int MAX_ELEVATION = 3;

    public static final Scene STARTING_SCENE = SceneHandler.SCENE_1_PIE;

    private int elevation = MIN_ELEVATION;

    private Comparator<Actor> depthComparator = new Comparator<Actor>() {

        @Override
        public int compare(Actor o1, Actor o2) {

            final float y1 = o1.getY() + o1.getHeight();
            final float y2 = o2.getY() + o2.getHeight();

            if (y1 < y2) return -1;
            if (y1 > y2) return 1;

            return 0;
        }
    };
    private KeyHint hint;
    private ZenProgress progress;
    private Steve steve;
    private SceneHandler sceneHandler;
    private Pie pie;
    private StaticScenery statue;
    private StaticScenery bonsai;

    private static GameplayScreen instance;
    private int totalScore = 0;
    private KeyHint hintW;
    private KeyHint hintS;
    private KeyHint hintA;
    private KeyHint hintD;
    private KeyHint hintE;
    private KeyHint hintQ;

    public boolean shownMeditationHelp = false;
    private KeyHint pieHint;

    private boolean defcon = false;

    public GameplayScreen(ZenGame game) {
        super(game);
        this.instance = this;
    }

    public static GameplayScreen getInstance() {
        return instance;
    }


    /**
     * Are there any threats to the monk alive?
     *
     * @return <code>true</code> if at least one threat lives.
     */
    public boolean threatsAlive() {
        for (Actor actor : stage.getActors()) {
            if (actor instanceof CombatActor && actor != steve) {
                // invisible actors are still a threat! they'll surface sooner or later.
                if (!((CombatActor) actor).isPacifist()) { defcon=true; return true; }
            }
        }

        if ( defcon )
        {
            defcon =false;
            displayMessage("Pfew. All calm again.");
        }

        fadeoutPieHint();

        return false;
    }

    private void fadeoutPieHint() {
        if ( pieHint != null )
        {
            fadeoutHintButton(pieHint);
            pieHint=null;
        }
    }


    @Override
    public void render(float delta) {

        if (Settings.DEBUG)
            delta *= Settings.DEBUG_MULTIPLIER;

        if (batch == null || stage == null || stage.getCamera() == null) return;

        if (Inputs.escapeOnce) {
            setScreen(new SplashScreen(game));
            return;
        }

        // teehee, player won by ascending to heaven.
        if (getSteve().getY() <= -100) {
            playerWon();
            return;
        }

        stage.act(delta);
        if (sceneHandler != null && sceneHandler.act(delta)) nextScene();

        zenLogic(delta);
        hintLogic(delta);

        if (statue != null)
            statue.setActiveCell(getRenderedElevation());
        if (bonsai != null)
            bonsai.setActiveCell(getRenderedElevation());

        if (batch == null || stage == null || stage.getCamera() == null) return;

        final GL20 gl = Gdx.gl20;
        if (gl != null) {
            gl.glClearColor(1, 1, 1, 1);
            gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(stage.getCamera().combined);
            batch.begin();
            batch.draw(Assets.background[getRenderedElevation()], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();

            stage.getRoot().getChildren().sort(depthComparator);

            stage.draw();

            if (sceneHandler != null) {
                batch.begin();
                sceneHandler.draw(batch);
                batch.end();
            }
        }

        super.render(delta);

    }

    private void hintLogic(float delta) {

        final float x = steve.getX() + 20;
        final float y = steve.getY();

        final float offset = 9 * Assets.PIXEL_SCALE;

        if (hintW.isVisible()) hintW.setPosition(x, y - offset);
        if (hintS.isVisible()) hintS.setPosition(x, y + offset);
        if (hintA.isVisible()) hintA.setPosition(x - offset, y);
        if (hintD.isVisible()) hintD.setPosition(x + offset, y);
        if (hintE.isVisible()) hintE.setPosition(x + offset, y - offset);
        if (hintQ.isVisible()) hintQ.setPosition(x - offset, y - offset);

        if (Inputs.left) {
            fadeoutHintButton(hintA);
        }
        if (Inputs.right) {
            fadeoutHintButton(hintD);
        }
        if (Inputs.up) {
            fadeoutHintButton(hintW);
        }
        if (Inputs.down) {
            fadeoutHintButton(hintS);
        }

        if (Inputs.attackOnce) {
            fadeoutHintButton(hintE);
        }

        if (Inputs.attackSecondaryOnce) {
            fadeoutHintButton(hintQ);
        }

        if ( !pie.isVisible() )
        {
            fadeoutPieHint();
        }

        if ( pieHint != null )
        {
            pieHint.setPosition(pie.getX() + 20, pie.getY() - 20);
        }

        if ( !shownMeditationHelp )
        {
            if ( !hintA.isVisible() && !hintD.isVisible() && !hintE.isVisible() && !hintS.isVisible() && !hintW.isVisible() && !hintQ.isVisible() )
            {
                shownMeditationHelp=true;
                sceneHandler.displayMessage("Don't move, eventually steve will meditate!");
            }
        }
    }

    private void fadeoutHintButton(KeyHint key) {
        if (key.isVisible() && key.getActions().size==0) {
            key.addAction(Actions.fadeOut(1.5f));
            key.addAction(Actions.delay(1.5f, Actions.hide()));
            key.addAction(Actions.delay(1.6f, Actions.removeActor()));
        }
    }

    private int getRenderedElevation() {
        if (steve.isAscending()) return 3;
        if (inDeathScene()) return elevation - 1;
        return 0;
    }

    public boolean inDeathScene() {
        return sceneHandler != null && sceneHandler.getScene() == SceneHandler.SCENE_4_DEATH;
    }

    private void nextScene() {
        Scene nextScene = null;

        if (sceneHandler == null) nextScene = STARTING_SCENE;
        else if (sceneHandler.getScene() == SceneHandler.SCENE_3_DEER) nextScene = SceneHandler.SCENE_4_DEATH;
        else if (sceneHandler.getScene() == SceneHandler.SCENE_2_SONGBIRD_OF_DEATH)
            nextScene = SceneHandler.SCENE_3_DEER;
        else if (sceneHandler.getScene() == SceneHandler.SCENE_1_PIE)
            nextScene = SceneHandler.SCENE_2_SONGBIRD_OF_DEATH;

        sceneHandler = nextScene != null ? new SceneHandler(this, nextScene) : null;
    }

    /**
     * Steve won! well. in a way. ok he died, shut up.
     */
    private void playerWon() {
        setScreen(new VictoryScreen(game, totalScore));
    }

    private void zenLogic(float delta) {


        // don't carry over progress from previous use. (ignore if stuck in a forced
        // animation, which might be part of meditation).
        if (!progress.isVisible() && steve.isMeditating() && !steve.inForcedAnimation()) {
            progress.reset();
        }

        // we only see the stress-bar when meditating.
        hint.setVisible(steve.isMeditating());
        progress.setVisible(steve.isMeditating());

        // reset to default when we lose meditation.
        if (!steve.isMeditating() && !steve.inForcedAnimation()) {
            elevation = MIN_ELEVATION;
        }

        // we've made enough progress. DEFCON 5, ESCALATE!
        if (progress.getProgress() > 1.1f) {
            scoreZen(1000);
            Assets.steveZenPowerup.play(0.25f);
            if (elevation < maxElevation()) {
                elevation++;
            }
            progress.reset();
        }
    }

    public void scoreZen(int points) {
        score(points, 127 * Assets.PIXEL_SCALE, 40 * Assets.PIXEL_SCALE);
    }

    public void score(int score, int x, int y) {
        totalScore += score;
        stage.addActor(new Score("+" + Integer.toString(score), x, y));
    }

    private int maxElevation() {
        // during the death scene we reach maximum meditation.
        return inDeathScene() ? MAX_ELEVATION + 1 : MAX_ELEVATION;
    }

    @Override
    public void show() {
        super.show();

        setupStage();
    }

    private void setupStage() {

        // statue
        statue = new StaticScenery(Assets.statue, 37 * Assets.PIXEL_SCALE, 80 * Assets.PIXEL_SCALE);
        stage.addActor(statue);

        // bonsai
        bonsai = new StaticScenery(Assets.bonsai, 124 * Assets.PIXEL_SCALE, 82 * Assets.PIXEL_SCALE);
        stage.addActor(bonsai);

        steve = new Steve(this, Assets.steve, Assets.steveWounded, Assets.steveDeadlyWounded, null, 74 * Assets.PIXEL_SCALE, 82 * Assets.PIXEL_SCALE);
        stage.addActor(steve);

        pie = new Pie(Assets.pie, 2 * Assets.PIXEL_SCALE, 70 * Assets.PIXEL_SCALE);
        pie.setPacifist(true);
        stage.addActor(pie);

        hint = new KeyHint("e", 125 * Assets.PIXEL_SCALE, 70 * Assets.PIXEL_SCALE);
        progress = new ZenProgress(125 * Assets.PIXEL_SCALE, 40 * Assets.PIXEL_SCALE);

        hint.setVisible(false);
        progress.setVisible(false);

        stage.addActor(hint);
        stage.addActor(progress);

        hintW = new KeyHint("w", 50, 40, false);
        hintS = new KeyHint("s", 50, 60, false);
        hintA = new KeyHint("a", 40, 50, false);
        hintD = new KeyHint("d", 60, 50, false);
        hintE = new KeyHint("e", 60, 50, false);
        hintQ = new KeyHint("q", 60, 50, false);

        stage.addActor(hintW);
        stage.addActor(hintS);
        stage.addActor(hintA);
        stage.addActor(hintD);
        stage.addActor(hintE);
        stage.addActor(hintQ);

        if (Settings.DEBUG ) {
            final Pie pie = new Pie(Assets.pie, 40 * Assets.PIXEL_SCALE, 90 * Assets.PIXEL_SCALE);
            pie.setPacifist(false);
            pie.setHitpoints(1000);
            stage.addActor(pie);
            final Pie pie2 = new Pie(Assets.pie, 80 * Assets.PIXEL_SCALE, 90 * Assets.PIXEL_SCALE);
            pie2.setPacifist(false);
            pie2.setHitpoints(1000);
            stage.addActor(pie2);
        }

        if (Settings.DEBUG && Settings.DEBUG_TEST_MONSTERS) {
            for (int i = 0; i < 2; i++) {
                final Deer testDeer = new Deer(Assets.deer, Assets.deerGibs, 20 * Assets.PIXEL_SCALE * i, 86 * Assets.PIXEL_SCALE);
                testDeer.setPacifist(false);
                stage.addActor(testDeer);

                final PreyBird testPreyBird = new PreyBird(Assets.preybird, Assets.preybirdGibs, 20 * Assets.PIXEL_SCALE * i, 90 * Assets.PIXEL_SCALE);
                testPreyBird.setPacifist(false);
                stage.addActor(testPreyBird);
            }
        }

        nextScene();

        sceneHandler.displayMessage("\"I think i'll try to meditate here!\"");
    }

    public int getElevation() {
        return elevation;
    }

    public Steve getSteve() {
        return steve;
    }

    public Stage getStage() {
        return stage;
    }

    public Pie getPie() {
        return pie;
    }

    public void displayMessage(String message) {
        if ( sceneHandler != null )
        sceneHandler.displayMessage(message);
    }

    public void addPieHint() {
        pieHint = new KeyHint("e", 2 * Assets.PIXEL_SCALE, 70 * Assets.PIXEL_SCALE, false);
        stage.addActor(pieHint);
    }
}
