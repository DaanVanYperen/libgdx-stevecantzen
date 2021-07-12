package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Daan van Yperen
 */
public class PreyBird extends CombatActor {

    public static final int CELL_PERCH = 2;
    public static final int CELL_FLY_1 = 0;
    public static final int CELL_FLY_2 = 1;
    public static final int CELL_PECK  = 3;

    private Animation<TextureRegion>[] walking;
    private Animation<TextureRegion>[] standing;
    private Animation<TextureRegion>[] peck;

    public PreyBird(TextureRegion[][] cells, Animation<TextureRegion>[] gibs, int x, int y) {
        super(cells, gibs, x, y);

        standing = createAnimation(1f, CELL_PERCH);
        walking = createAnimation(0.3f, CELL_FLY_1, CELL_FLY_2);
        peck    = createAnimation(0.5f, CELL_PECK);

        // flying.
        setZ(50);
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
        return peck;
    }

    @Override
    public void act(float delta) {

        // TODO proper AI!
        mx=my=0;

        if ( getZ() > 50 ) setZ(getZ() - delta*20);

        basicAI(delta);

        super.act(delta);
    }
}
