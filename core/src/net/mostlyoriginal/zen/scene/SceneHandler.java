package net.mostlyoriginal.zen.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * @author Daan van Yperen
 */
public class SceneHandler {

    public static final int DIALOG_DISPLAY_DURATION = 4;
    private final GameplayScreen screen;
    private final Scene scene;
    private int dialogIndex = 0;
    private float displayCooldown = 0;
    private float nextDelay = 1.5f;
    private boolean initialized = false;
    private float manualMessageCooldown;
    private String manualMessage;

    public SceneHandler(GameplayScreen screen, Scene scene) {
        this.screen = screen;
        this.scene = scene;
        this.initialized=false;

        displayCooldown = DIALOG_DISPLAY_DURATION;
    }

    /**
     * @param delta
     * @return <code>true</code> if completed.
     */
    public boolean act(float delta) {

        if (displayCooldown > 0) {
            displayCooldown -= delta;
        }

        if ( manualMessageCooldown > 0 )
        {
            manualMessageCooldown -= delta;
        }

        if (scene != null) {

            if ( !initialized )
            {
                if ( !screen.threatsAlive() && screen.getSteve().isMeditating() && screen.getSteve().isMeditatingSeconds() > 3 )
                {
                    initialized = true;
                    // setup initial props.
                    scene.updateProps(screen, 0);
                }
            } else {
                scene.act(screen, delta);
            }
        }


        if (screen.getSteve().isMeditating() && screen.getSteve().isMeditatingSeconds() > 3) {
            if (displayCooldown <= 0) {
                if (progressDialog(delta)) return true;
            }
            // move things forward as the player meditates.
            // 1 message (stage 1)
            // 2 messages (stage 2)
            // whatever messages remain, then drop out of meditate and prepare the next stage.
        }
        return false;
    }

    private boolean progressDialog(float delta) {
        nextDelay -= delta;
        if (nextDelay < 0) {
            if (hasDialogRemaining()) {
                nextDelay = 1;
                attemptNextDialog();
            } else {
                // stop meditating! end scene.
                nextDelay = 1;
                scene.sceneFinished(screen);
                screen.getSteve().stopMeditating();
                return true;
            }
        }
        return false;
    }

    /**
     * Attempt to go for next dialog, but only if the player is whacking keys like a maniac. ;)
     */
    private void attemptNextDialog() {
        if (dialogIndex < maxDialogLevelAllowed()) {
            dialogIndex++;

            // update props to match dialog.
            scene.updateProps(screen, dialogIndex);

            displayCooldown = DIALOG_DISPLAY_DURATION;
        } else {
            // we repeat the line (but a bit less frequently) for if players missed it.
            if (MathUtils.random(0, 3) == 3) displayCooldown = DIALOG_DISPLAY_DURATION;
        }
    }

    /**
     * @return max dialog level given the current meditative elevation.
     */
    private int maxDialogLevelAllowed() {
        int maxDialog = 0;
        switch (screen.getElevation()) {
            case 2:
                maxDialog = 2;
                break;
            case 3:
            case 4:
                maxDialog = 99;
                break;
        }
        return maxDialog;
    }

    private boolean hasDialogRemaining() {
        return dialogIndex < scene.dialog.length - 1;
    }

    private GlyphLayout glyphLayout = new GlyphLayout();;

    public void draw(SpriteBatch batch) {

        float y = GameplayScreen.getInstance().getSteve().getY() - 46f;

        if (displayCooldown > 0 ) {

            glyphLayout.setText(Assets.font, scene.dialog[dialogIndex], Color.WHITE,  Gdx.graphics.getWidth() - 8, Align.left,true);
            float x = MathUtils.clamp(GameplayScreen.getInstance().getSteve().getX() - glyphLayout.width/2, 4, Gdx.graphics.getWidth() - glyphLayout.width - 4 );

            batch.setColor(1,1,1,1);
            batch.draw(Assets.textBackground, x-2,y+4, 4+glyphLayout.width,4+glyphLayout.height);
            Assets.font.setColor(1f,1f,1f,1f);
            Assets.font.draw(batch, scene.dialog[dialogIndex], x + 1, y + 1, Gdx.graphics.getWidth() - 8,Align.left,true);
            //Assets.font.setColor(0f, 0f, 0f, 1f);
            //Assets.font.drawWrapped(batch, scene.dialog[dialogIndex], x, y, Gdx.graphics.getWidth() - 8);
        } else {
            if (manualMessageCooldown > 0 ) {

                glyphLayout.setText(Assets.font, manualMessage, Color.WHITE,  Gdx.graphics.getWidth() - 8,Align.left,true);
                float x = MathUtils.clamp(GameplayScreen.getInstance().getSteve().getX() - glyphLayout.width/2, 4, Gdx.graphics.getWidth() - glyphLayout.width - 4 );

                batch.setColor(1,1,1,1);
                batch.draw(Assets.textBackground, x-2,y+4, 4+glyphLayout.width,4+glyphLayout.height);
                Assets.font.setColor(1f,1f,1f,1f);
                Assets.font.draw(batch, manualMessage, x+1, y+1, Gdx.graphics.getWidth() - 8, Align.left, true);
                //Assets.font.setColor(0f, 0f, 0f, 1f);
               // Assets.font.drawWrapped(batch, manualMessage, x, y, Gdx.graphics.getWidth() - 8);
            }
        }
    }

    // PIE EVENT.

    public static final Scene SCENE_1_PIE = new PieScene(new String[]
            {
                    //        "*steve sits down*",

                    "\"Now, clear your head Steve...\"",

                    //"* level 1 zen achieved *

                    "\"I am but a leaf on the wind, watch how i...\"",

                    "*stomach grumbles*",

                    //"* level 2 zen achieved *

                    "\"Stop it stomach, Steve has no use for pie!\"",

                    "... ",

                    "\"delicious, delicious PIEEEEEEEEEE\""

                    // *COMBAT COMMENCES. Score based on how quickly the pie is dispatched*
                    // *bonus: blood smurched pie covered steve*

            });

    public static final Scene SCENE_2_SONGBIRD_OF_DEATH = new BirdScene(new String[]
            {
                    "\"No more distractions!\"",

//*level 1 zen, graphics become simpler*

                    "\"A thought is but a whisper of a restless mind.\"",

//*bird of prey lands on head, in this level of zen it looks like a songbird*

                    "\"The void is all around me. I am nothing.\"",

//*level 2 zen*

                    "\"Oh! What a lovely songbird..... focus Steve, focus!\"",


                    "\"The void is all around m..\"",

//*squawk*

                    "\"WAIT, THAT'S NOT A SONGBIRD!\"",

//*bird of prey assaults monk*

//*COMBAT COMMENCES. Score based on how quickly the bird is dispatched*

//*bonus: let monk rip things from environment to fight with*

//*bonus: Steve wheezes until he calms down again*

//*bonus: blood smurched steve*
            });

    public static final Scene SCENE_3_DEER = new DeerScene(new String[]
            {
//*put some deer in the first scene to tie things together*

//*steve sits down*

                    "\"*Wheeze* I just *wheeze* rest *pant* breath\"",

//*Space to ignore wounds and attain enlightenment*

//* level 1 zen achieved *

                    "\"Calmth... rest... the silence... so glorious\"",

//*deer move closer together*

                    "\"I can feel it. Nothing. the void. perfect.. .silence\"",

//* level 2 zen achieved *

//*Bonus: Floating!*

                    "", //*pause to accommodate deer start doing the deed*

                    "\"mrrwaaaaaHHHHHHHHHHHHHHHHHHHHH\""

//*COMBAT COMMENCES. Score based on how quickly the deer are dispatched*

//*bonus: extra blood smurched steve*

            });


    public static final Scene SCENE_4_DEATH = new DeathScene(new String[]
            {
                    //*steve sits down, mortally wounded*

                    "\"*Gargl* Steve's not *swallow* giving up, universe! *cough up blood*\"",

                    //* level 1 zen achieved *

                    "\"*Hnggg* Vast emptiness\"",

                    "\"I see it n.. now! *gargl*\"",

                    //* level 2 zen achieved *

                    "\"We are all but droplets in a sea of noth...\"",

                    //* level 3 zen achieved: it is now a typical minimalistic cube game. *

                    "\"Uh..\"",

                    "\"What the hell is with this minimalistic sh*t!?\"",

                    //* steve dies*

                    // "You have achieved Enlightenment.

                    //Upticking final Score + Rating

            }
    );

    public Scene getScene() {
        return scene;
    }

    /**
     * Hacked in for quick message display. Low priority messages only please!
     *
     * @param message
     */
    public void displayMessage(String message) {
        manualMessageCooldown = 3;
        manualMessage = message;
    }
}
