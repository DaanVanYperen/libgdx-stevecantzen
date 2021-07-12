package net.mostlyoriginal.zen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.Inputs;
import net.mostlyoriginal.zen.Settings;
import net.mostlyoriginal.zen.ZenGame;
import net.mostlyoriginal.zen.actors.FloatingSteve;
import net.mostlyoriginal.zen.actors.StaticScenery;
import net.mostlyoriginal.zen.actors.ui.KeyHint;

/**
 * @author Daan van Yperen
 */
public class VictoryScreen extends ZenScreen {

    public static final int TEXT_X_OFFSET = 40;
    private float ignoreKeySeconds = 1.5f;
    private FloatingSteve floatingSteve;
    private StaticScenery scenery;
    private float blockSpawnDelay = 1;
    private Group blockGroup;
    private Integer score;
    private float age;

    private float nextDialogCooldown = 24;
    private float dialogCooldown = 0;
    private String activeBanter;
    private int banterIndex = 0;

    private String banter[] = new String[]{
            "\"I'm feeling sick! *urgl*\"",
            "\"Ok, very funny. GET ME OUT OF HERE!\"",
            "\"Helloooooooooooooo?\"",
            "\"Anyone?\"",
            "\"Oh well. Good riddance.\"",
            "\"I can learn to love cubes.\"",
            "\"That potato pie was tasty though.\"",
            "\"I wonder why it wasn't cherry.\"",
            "\"There was a perfectly fine cherry tree right there.\"",
            "\"I bet these cubes are really soft.\"",
            "\"If they weren't ETHEREAL. #$&!$&*$#\"",
            "\"Did you see those deer explode?\"",
            "\"I wonder if they ate something explosive.\"",
            "\"I can kick, but explosions are beyond my feet.\"",
            "\"That was some high detail blood and guts!\"",
            "\"Glad you're hanging out with me.\"",
            "\"Would be kind of lonely if you leave.\"",
            "\"Just drag me on your second monitor!\"",
            "\"If you have one.\"",
            "\"I can't tell. stuck in box-land.\"",
            "\"Guess that makes me the box-king.\"",
            "\"BOW FOR THE BOX-KING!\"",
            "\"I DEMAND YOU STACK INTO A CASTLE.\"",
            "\"AND NAKED WOMEN.\"",
            "\"AND BEER.\"",
            "\"AND A COOKIE.\"",
            "\"AND A TOILET.\"",
            "\"Wait, do I stell need a toilet?\"",
            "\"I'm not a cube. So my poop probably isn't.\"",
            "\"Am I going to float around with my poop?\"",
            "\"Until the end of days?\"",
            "\"...\"",
            "\"HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP.\"",
            "\"HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP.\"",
            "\"God?\"",
            "\"Are you out there?\"",
            "\"I'm very sorry for hitting those deer.\"",
            "\"The bird had it coming though.\"",
            "\"His bird-friends too!\"",
            "\"Sneaking up to me like that.\"",
            "\"Anyway, can you please swoop me out of here?\"",
            "\"..\"",
            "\"Oh, you're still here?\"",
            "\"You got me here, why don't you get me out!\"",
            "\"Close the game.\"",
            "\"Just do it.\"",
            "\"I'm sure my banter is going to end soon.\"",
            "\"Only 48 hours available to make me.\"",
            "\"At least 16 hours spent sleeping.\"",
            "\"So he can't have much time left.\"",
            "\"Just close the game.\"",
            "\"go on.\"",
            "\"It's fine.\"",
            "\"Thanks for playing!\"",
    };

    public VictoryScreen(ZenGame game, int score) {
        super(game);
        this.score = score;
    }

    @Override
    public void render(float delta) {

        if (Settings.DEBUG)
            delta *= Settings.DEBUG_MULTIPLIER;

        ignoreKeySeconds -= delta;
        age += delta;

        randomlySpawnBlocks(delta);

        final GL20 gl = Gdx.gl20;
        if (gl != null) {
            gl.glClearColor(1, 1, 1, 1);
            gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            stage.act(delta);
            stage.draw();

            batch.setProjectionMatrix(stage.getCamera().combined);
            batch.begin();
            //batch.draw(Assets.background[GameplayScreen.MAX_ELEVATION], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (ignoreKeySeconds <= 0)
                Assets.font.draw(batch, "Press       to continue.", 64 * Assets.PIXEL_SCALE, 95 * Assets.PIXEL_SCALE);

            Assets.fontHuge.draw(batch, "Good Job!", TEXT_X_OFFSET * Assets.PIXEL_SCALE, 16 * Assets.PIXEL_SCALE);
            Assets.font.draw(batch, "What do you know. Steve CAN Zen!", TEXT_X_OFFSET * Assets.PIXEL_SCALE, 33 * Assets.PIXEL_SCALE);

            if (age > 10) {
                Assets.font.draw(batch, "Steve loves his new minimalistic home!", TEXT_X_OFFSET * Assets.PIXEL_SCALE, 40 * Assets.PIXEL_SCALE);
            }
            if (age > 12 && age < 16) {
                Assets.font.draw(batch, "\"Cubes Suck!\"", 20 * Assets.PIXEL_SCALE, floatingSteve.getY());
            }

            if (age > 14 && age < 20) {
                Assets.font.draw(batch, "Don't listen to him.", TEXT_X_OFFSET * Assets.PIXEL_SCALE, 47 * Assets.PIXEL_SCALE);
            }
            Assets.fontHuge.draw(batch, "Final Score: " + score + " points!", TEXT_X_OFFSET * Assets.PIXEL_SCALE, 80 * Assets.PIXEL_SCALE);

            handleBanter(delta);

            batch.end();

        }

        if (ignoreKeySeconds <= 0 && (Inputs.attackOnce || Inputs.escapeOnce)) {
            Inputs.attackOnce = false;
            setScreen(new SplashScreen(game));
        }

        super.render(delta);
    }

    private void handleBanter(float delta) {
        nextDialogCooldown -= delta;
        if (nextDialogCooldown <= 0 && banterIndex < banter.length-1) {
            activeBanter = banter[banterIndex++];
            dialogCooldown = 3;
            nextDialogCooldown = 5;
        }

        dialogCooldown -= delta;
        if  (dialogCooldown >= 0 )
        {
            Assets.font.draw(batch, activeBanter, 20 * Assets.PIXEL_SCALE, floatingSteve.getY());
        }
    }

    private void randomlySpawnBlocks(float delta) {
        blockSpawnDelay -= delta;
        if (blockSpawnDelay <= 0) {
            blockSpawnDelay = MathUtils.random(0.5f, 3f);

            for (int i = 0; i < MathUtils.random(2, 4); i++) {
                spawnFloatingBlock();
            }

        }
    }

    @Override
    public void show() {
        super.show();

        final Group keystructions = new Group();

        keystructions.addActor(new KeyHint("e", -34 * Assets.PIXEL_SCALE, 20 * Assets.PIXEL_SCALE));

        keystructions.setPosition(110 * Assets.PIXEL_SCALE, 70 * Assets.PIXEL_SCALE);
        keystructions.setVisible(false);
        keystructions.addAction(Actions.delay(ignoreKeySeconds, Actions.show()));

        floatingSteve = new FloatingSteve(Assets.steve, 10 * Assets.PIXEL_SCALE, 120 * Assets.PIXEL_SCALE);
        floatingSteve.addAction(Actions.moveTo(10 * Assets.PIXEL_SCALE, 20, 30));
        floatingSteve.addAction(Actions.rotateBy(36000, 300));

        blockGroup = new Group();

        spawnFloatingBlock();
        spawnFloatingBlock();

        stage.addActor(blockGroup);
        stage.addActor(floatingSteve);
        stage.addActor(keystructions);

    }

    private void spawnFloatingBlock() {
        scenery = new StaticScenery(randomAsset(), MathUtils.random(Gdx.graphics.getWidth()), -150);
        scenery.setColor(1, 1, 1, 0.5f);
        scenery.addAction(Actions.moveTo(scenery.getX(), 120 * Assets.PIXEL_SCALE, 30));
        scenery.addAction(Actions.delay(30, Actions.removeActor()));
        blockGroup.addActor(scenery);
    }

    private TextureRegion randomAsset() {

        switch (MathUtils.random(0, 1)) {
            case 1:
                return Assets.bonsai[3];
            default:
                return Assets.statue[3];
        }

    }

}
