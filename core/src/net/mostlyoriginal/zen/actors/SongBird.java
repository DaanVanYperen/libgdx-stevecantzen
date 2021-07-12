package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.zen.NotImplementedException;

/**
 * @author Daan van Yperen
 */
public class SongBird extends CombatActor {

    public static final int CELL_IDLE = 0;
    public static final int CELL_WALK_1 = 1;
    public static final int CELL_WALK_2 = 2;

    private Animation<TextureRegion>[] walking;
    private Animation<TextureRegion>[] standing;

    public SongBird(TextureRegion[][] cells, int x, int y) {
        super(cells, null, x, y);

        standing = createAnimation(1f, CELL_IDLE );
        walking = createAnimation(0.3f, CELL_WALK_1, CELL_WALK_2);

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
        throw new NotImplementedException();
    }

    @Override
    public void act(float delta) {

        // TODO proper AI!
        mx=my=0;

        super.act(delta);
    }
}
