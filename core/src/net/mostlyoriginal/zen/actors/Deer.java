package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class Deer extends CombatActor {

    public static final int CELL_IDLE = 0;
    public static final int CELL_NIBBLE_1 = 1;
    public static final int CELL_NIBBLE_2 = 2;
    public static final int CELL_WALK_1 = 3;
    public static final int CELL_WALK_2 = 4;
    public static final int CELL_KICK_FORWARD = 5;
    public static final int CELL_KICK_BACKWARD = 6;

    private Animation<TextureRegion>[] walking;
    private Animation<TextureRegion>[] standing;
    private Animation<TextureRegion>[] kicking;
    private Animation<TextureRegion>[] punching;

    public Deer(TextureRegion[][] cells, Animation<TextureRegion>[] gibs, int x, int y) {
        super(cells, gibs, x, y);

        standing = createAnimation(1f, CELL_IDLE, CELL_NIBBLE_1, CELL_NIBBLE_2, CELL_NIBBLE_1, CELL_NIBBLE_2, CELL_IDLE, CELL_IDLE, CELL_IDLE, CELL_IDLE);
        walking = createAnimation(0.3f, CELL_WALK_1, CELL_IDLE, CELL_WALK_2, CELL_IDLE);
        kicking = createAnimation(0.2f, CELL_KICK_FORWARD);
        punching = createAnimation(0.2f, CELL_KICK_BACKWARD);

    }

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
        return MathUtils.random(0, 1) == 1 ? kicking : punching;
    }

    @Override
    public void act(float delta) {

        mx=my=0;
        basicAI(delta);

        super.act(delta);
    }


    @Override
    protected String getPacifistHitMessage() {
        return "\"I only hit mammals after they annoy me. More fun that way!\"";
    }

}
