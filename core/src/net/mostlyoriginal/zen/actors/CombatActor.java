package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * @author Daan van Yperen
 */
public abstract class CombatActor extends AnimatedActor {

    protected Animation<TextureRegion>[] forcedAnimation;
    protected float forcedAnimationAge;

    protected float mx = 0;
    protected float my = 0;

    protected int damage = 0;
    protected int hitpoints = 3;

    protected final Animation<TextureRegion>[] gibs;
    private boolean XConstrained = true;
    private boolean pacifist;
    private boolean YConstrained = true;
    private float attackCooldownDelta = 0;


    public CombatActor(TextureRegion[][] cells, Animation<TextureRegion>[] gibs, int x, int y) {
        super(cells);

        this.gibs = gibs;

        setSize(
                cells[0][0].getRegionWidth() * Assets.PIXEL_SCALE,
                cells[0][0].getRegionHeight() * Assets.PIXEL_SCALE);
        setPosition(x, y);
        setOrigin(getWidth() / 2, getHeight());
    }

    protected void startForcedAnimation(final Animation<TextureRegion>[] animation) {
        forcedAnimationAge = 0;
        forcedAnimation = animation;
    }

    @Override
    protected TextureRegion getCell() {

        // are we running any forced animation like kicking?
        if (forcedAnimation != null && !forcedAnimation[facing].isAnimationFinished(forcedAnimationAge)) {
            return forcedAnimation[facing].getKeyFrame(forcedAnimationAge, false);
        } else forcedAnimation = null;


        // Default animations.

        Animation<TextureRegion>[] active = getStandingAnimation();

        if (isMoving()) {
            active = getWalkingAnimation();
        }

        return active != null ? active[facing].getKeyFrame(age, true) : null;
    }

    protected abstract Animation<TextureRegion>[] getWalkingAnimation();

    protected abstract Animation<TextureRegion>[] getStandingAnimation();

    protected abstract Animation<TextureRegion>[] getCombatAnimation();

    protected Animation<TextureRegion>[] getGibs() {
        return gibs;
    }

    ;

    @Override
    public void act(float delta) {
        super.act(delta);

        // applies momentum.
        setPositionClamped(getX() + mx * delta * 100f + velocity.x * delta, getY() + my * delta * 50f + velocity.y * delta);

        z = MathUtils.clamp(0f, z + velocity.z * delta, 100f);

        // recalc movement delta so it includes player controls.
        updateDelta();

        forcedAnimationAge += delta;
    }

    float cooldownDelta;

    public void attack() {
        if (!isBusy() && isVisible()) {
            if ( damageNearbyEnemies(1) )
            {
                startForcedAnimation(getCombatAnimation());
                Assets.steveKicks.play();
            }
        }
    }

    /**
     * @param damage
     * @return <code>true</code> if combat animation should be shown.
     */
    private boolean damageNearbyEnemies(int damage) {

        boolean damagedEnemies = false;

            int count = 0;
            for (Actor actor : getParent().getChildren()) {

                // can't hit invisible actors.
                if (actor == this || !actor.isVisible())
                    continue;

                final CollidableActor victim = (CollidableActor) actor;

                if (this.overlaps(victim, facing == 0 ? -getReach() : getReach()) && victim instanceof CombatActor) {
                    count++;
                    if ( ((CombatActor) victim).isEnemyOf(this) )
                    {
                        if ( ((CombatActor) victim).causeDamage(this, damage) )
                        {
                            damagedEnemies=true;
                            // we only hit one at a time, sorry!
                            break;
                        }
                    }
                }
            }
        return damagedEnemies || count==0;
    }

    private int getReach() {
        return 50;
    }

    protected boolean isEnemyOf(CombatActor actor) {
        // steve, enemy of ALL.
        return actor instanceof Steve;
    }

    /**
     * Apply damage caused by attacker, provided entity not in pacifist mode.
     *
     * @param attacker
     * @param damage
     * @return <code>true</code> if damage was caused, <code>false</code> if hitting pacifists. dang pacifists!
     */
    private boolean causeDamage(CombatActor attacker, int damage) {
        if (!this.isPacifist()) {

            applyForce(facing == Assets.FACE_LEFT ? 400 : -400, MathUtils.random(-10f, 10f), 0f);
            this.damage += damage;
            damaged();
            Assets.damageDealt.play();

            if ( !(this instanceof Steve)  )
            {
                GameplayScreen.getInstance().score( 25,(int)getX(), (int)getY());
            }

            // we kill all BUT STEVE. we love steve.
            if (this.damage >= this.hitpoints && !(this instanceof Steve) ) {
                GameplayScreen.getInstance().score( 2500,(int)getX(), (int)getY());
                setVisible(false);
                addAction(Actions.removeActor());
                spawnGibs();
            }

            return true;
        } else {
            GameplayScreen.getInstance().displayMessage( this.getPacifistHitMessage());
        }
        return false;
    }

    protected void spawnGibs() {
        Animation<TextureRegion>[] gibs = getGibs();
        if (gibs != null) {
            for (int i = 0; i < MathUtils.random(4,10); i++) {
                getStage().addActor(new Gib(gibs[MathUtils.random(0, gibs.length - 1)], getX(), getY(), velocity));
            }
        }

    }

    protected boolean isBusy() {
        return forcedAnimation != null && !isPushed();
    }

    private boolean isPushed() {
        return velocity.x != 0 || velocity.y != 0 || velocity.z != 0;
    }

    /**
     * Clamp actor to combat area.
     */
    protected void setPositionClamped(float x, float y) {
        // entities that are not XConstrained have some room outside the screen to be in. that way we can animate them into the scene.
        setPosition(
                MathUtils.clamp(x, 0f - (XConstrained ? 0 : 200), Gdx.graphics.getWidth() - getWidth() + (XConstrained ? 0 : 200)),
                MathUtils.clamp(y, Gdx.graphics.getHeight() - getHeight() - 24f * Assets.PIXEL_SCALE - (YConstrained ? 0 : 500), Gdx.graphics.getHeight() - getHeight() - (4f * Assets.PIXEL_SCALE) + getZ() + (YConstrained ? 0 : 500)));
    }

    public void setXConstrained(boolean XConstrained) {
        this.XConstrained = XConstrained;
    }

    public boolean isXConstrained() {
        return XConstrained;
    }

    public void setPacifist(boolean pacifist) {
        this.pacifist = pacifist;
    }

    public boolean isPacifist() {
        return pacifist;
    }

    public void setYConstrained(boolean YConstrained) {
        this.YConstrained = YConstrained;
    }

    public boolean isYConstrained() {
        return YConstrained;
    }

    protected void basicAI( float delta ) {

        // pacifists chase steve. TO THE DEATH.
        if (!isPacifist() && isVisible()) {

            final Steve steve = GameplayScreen.getInstance().getSteve();

            if (Math.abs(steve.getX() - getX()) >= 30)
                mx = MathUtils.clamp(steve.getX() - getX(), -1, 1);
            else if (Math.abs(steve.getY() - getY()) >= 10)
                my = MathUtils.clamp(steve.getY() - getY(), -1, 1);
            else
            {
                attackCooldownDelta -= delta;
                if ( attackCooldownDelta <= 0 )
                {
                    attackCooldownDelta = attackSpeed();
                    attack();
                }
            }
        }
    }

    private float attackSpeed() {
        return MathUtils.random(0.5f, 1.5f);
    }

    protected String getPacifistHitMessage() {
        return null;
    }

    public void damaged()
    {
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }
}
