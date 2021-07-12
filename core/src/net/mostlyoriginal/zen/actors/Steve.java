package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.zen.Inputs;
import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * Player avatar.
 *
 * @author Daan van Yperen
 */
public class Steve extends CombatActor {

    // sprite index.
    public static final int CELL_STAND_OPEN_MOUTH = 0;
    public static final int CELL_STAND = 1;
    public static final int CELL_MEDIDATE_LOOK_UP = 2;
    public static final int CELL_MEDIDATE = 3;
    public static final int CELL_MEDIDATE_CLOSED_EYES = 4;
    public static final int CELL_MEDIDATE_FACE_FORWARD = 5;
    public static final int CELL_MEDIDATE_PEEKING = 6;
    public static final int CELL_COMBAT_STANCE = 7;
    public static final int CELL_COMBAT_STEP = 8;
    public static final int CELL_COMBAT_STEP_2 = 9;
    public static final int CELL_COMBAT_PUNCH = 10;
    public static final int CELL_COMBAT_KICK = 11;
    public static final int CELL_CUTLERY = 12;
    public static final int CELL_EAT = 13;
    public static final int MEDITATION_COOLDOWN = 4;

    private boolean frozen = false;

    private Animation<TextureRegion>[] standing;
    private Animation<TextureRegion>[] walking;
    private Animation<TextureRegion>[] kicking;
    private Animation<TextureRegion>[] punching;
    private Animation<TextureRegion>[] meditatingRestless;
    private Animation<TextureRegion>[] meditatingCalm;
    private Animation<TextureRegion>[] meditatingPeeking;
    private Animation<TextureRegion>[] meditatingRage;
    private Animation<TextureRegion>[] meditatingCutlery;
    private final GameplayScreen gameplayScreen;
    private final TextureRegion[][] cellsWounded;
    private final TextureRegion[][] cellsDeadlyWounded;
    private final Animation<TextureRegion>[] gibs;
    private Animation<TextureRegion>[] eating;
    private final int x;
    private final int y;

    private boolean meditating = false;
    private float meditationFacingCooldown = 1;
    private float meditationCooldown = MEDITATION_COOLDOWN;

    private float meditatingSeconds = 0;
    private boolean ascending;

    public Steve(GameplayScreen gameplayScreen, TextureRegion[][] cells, TextureRegion[][] cellsWounded, TextureRegion[][] cellsDeadlyWounded, Animation<TextureRegion>[] gibs, int x, int y) {
        super(cells, gibs, x, y);

        this.gameplayScreen = gameplayScreen;

        this.cellsWounded = cellsWounded;
        this.cellsDeadlyWounded = cellsDeadlyWounded;

        this.gibs = gibs;
        this.x = x;
        this.y = y;

        this.hitpoints = 10;

        rebuildAnimations();
    }

    @Override
    public void damaged() {
        if (damage > hitpoints / 2) {
            if (cells != cellsWounded && cells != cellsDeadlyWounded) {
                cells = cellsWounded;
                rebuildAnimations();
            } else if (cells == cellsWounded) {
                // upscale to second damage type.
                if (damage > hitpoints) {
                    cells = cellsDeadlyWounded;
                    rebuildAnimations();
                }
            }
        }
    }

    private void rebuildAnimations() {
        standing = createAnimation(1, CELL_STAND);
        walking = createAnimation(0.3f, CELL_COMBAT_STANCE, CELL_COMBAT_STEP, CELL_COMBAT_STEP_2, CELL_COMBAT_STEP);
        kicking = createAnimation(0.2f, CELL_COMBAT_KICK, CELL_COMBAT_STEP);
        punching = createAnimation(0.2f, CELL_COMBAT_PUNCH, CELL_COMBAT_STEP);
        eating = createAnimation(0.2f, CELL_EAT, CELL_COMBAT_STEP, CELL_EAT, CELL_COMBAT_STEP);

        meditatingRestless = createAnimation(0.5f, CELL_MEDIDATE, CELL_MEDIDATE_FACE_FORWARD, CELL_MEDIDATE_LOOK_UP, CELL_MEDIDATE, CELL_MEDIDATE_LOOK_UP, CELL_MEDIDATE_FACE_FORWARD);
        meditatingCalm = createAnimation(0.5f, CELL_MEDIDATE_CLOSED_EYES);
        meditatingPeeking = createAnimation(6f, CELL_MEDIDATE_PEEKING);
        meditatingRage = createAnimation(0.2f, CELL_COMBAT_PUNCH, CELL_COMBAT_STEP, CELL_COMBAT_KICK, CELL_COMBAT_PUNCH, CELL_COMBAT_PUNCH, CELL_COMBAT_PUNCH, CELL_COMBAT_STEP, CELL_COMBAT_KICK, CELL_COMBAT_PUNCH, CELL_COMBAT_PUNCH);
        meditatingCutlery = createAnimation(2.5f, CELL_CUTLERY);
    }

    private float testDelta = 1;

    @Override
    protected Animation<TextureRegion>[] getWalkingAnimation() {
        return walking;
    }

    @Override
    protected Animation<TextureRegion>[] getStandingAnimation() {
        return standing;
    }

    @Override
    protected Animation<TextureRegion>[] getCombatAnimation() {

        if (gameplayScreen.getPie() != null && gameplayScreen.getPie().isVisible()) {
            if (gameplayScreen.getPie().overlaps(this)) return eating;
        }

        return !isMeditating() ? (MathUtils.random(0, 1) == 1 ? kicking : punching) : null;
    }

    @Override
    protected TextureRegion getCell() {

        // are we running any forced animation like kicking?
        if (forcedAnimation != null && !forcedAnimation[facing].isAnimationFinished(forcedAnimationAge)) {
            return forcedAnimation[facing].getKeyFrame(forcedAnimationAge, false);
        } else forcedAnimation = null;

        if (meditating) {
            Animation<TextureRegion>[] active;
            switch (gameplayScreen.getElevation()) {
                case 1:
                    active = meditatingRestless;
                    break;
                case 2:
                default:
                    active = meditatingCalm;
                    break;
            }
            return active[facing].getKeyFrame(age, true);
        }

        return super.getCell();
    }


    @Override
    public void act(float delta) {

        mx = my = 0;
        if (Inputs.attackOnce) {
            facing = 1;
            attack();
        } else if (Inputs.attackSecondaryOnce) {
            facing = 0;
            attack();
        }

        // control steve, unless he's frozen.
        if (!isFrozen()) {
            if (Inputs.left) mx = -1;
            else if (Inputs.right) mx = 1;
            if (Inputs.up) my = -1;
            else if (Inputs.down) my = 1;
        }

        if (mx == 0 && my == 0) {

            // work towards meditation when motionless and not under threat (be it pie or beast). and the tutorial has been shown.
            meditationCooldown -= delta;
            if (meditationCooldown <= 0 && !gameplayScreen.threatsAlive() && gameplayScreen.shownMeditationHelp) {
                meditating = true;
            }

            if (meditating) {
                meditatingSeconds += delta;
                // randomly flip direction to extend range of cells.
                meditationFacingCooldown -= delta;
                if (meditationFacingCooldown <= 0) {
                    meditationFacingCooldown = 1;
                    facing = MathUtils.random(0, 1);
                }
            }
        } else {
            meditatingSeconds = 0;
            meditating = false;
            meditationFacingCooldown = 1;
            meditationCooldown = MEDITATION_COOLDOWN;
        }

        super.act(delta);
    }

    public boolean isMeditating() {
        return meditating && (forcedAnimation == null);
    }


    public void stopMeditating() {
        meditationCooldown = MEDITATION_COOLDOWN * 4;
        meditating = false;
    }

    public void peekAtSurroundings() {
        startForcedAnimation(meditatingPeeking);
    }

    public void rage() {
        startForcedAnimation(meditatingRage);
    }

    public void displayCutlery() {
        startForcedAnimation(meditatingCutlery);
    }

    @Override
    protected boolean isBusy() {
        return super.isBusy() || isMeditating();
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean inForcedAnimation() {
        return forcedAnimation != null;
    }

    @Override
    protected boolean isEnemyOf(CombatActor actor) {
        // steve loves himself.
        return !(actor instanceof Steve);
    }

    public float isMeditatingSeconds() {
        return meditatingSeconds;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
