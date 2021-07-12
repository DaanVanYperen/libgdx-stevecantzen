package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.zen.Assets;

/**
 * @author Daan van Yperen
 */
public class FloatingSteve extends AnimatedActor {

    private final Animation<TextureRegion>[] agasp;
    private float facingDelta = 1;

    public FloatingSteve(TextureRegion[][] cells, int x, int y) {
        super(cells);
        setSize(cells[0][0].getRegionWidth() * Assets.PIXEL_SCALE, cells[0][0].getRegionHeight() * Assets.PIXEL_SCALE);

        agasp = createAnimation(0.8f, Steve.CELL_COMBAT_KICK, Steve.CELL_STAND, Steve.CELL_STAND_OPEN_MOUTH, Steve.CELL_STAND, Steve.CELL_STAND_OPEN_MOUTH, Steve.CELL_COMBAT_PUNCH);

        setOrigin(getWidth()/2,getHeight()/2);
        setPosition(x,y);
    }

    @Override
    protected TextureRegion getCell() {
        return agasp[facing].getKeyFrame(age, true);
    }

    @Override
    public void act(float delta) {

        facingDelta -= delta;
        if (facingDelta < 0)
        {
            facingDelta = 1;
            facing = MathUtils.random(0,1);
        }

        super.act(delta);
    }
}
