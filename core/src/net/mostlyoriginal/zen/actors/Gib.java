package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;

/**
 * @author Daan van Yperen
 */
public class Gib extends AnimatedActor {

    private final Animation<TextureRegion> region;

    public Gib(Animation<TextureRegion> region, float x, float y, Vector3 velocity) {
        // hack hack but we're doing it for speeeeeeeed.
        super(null);

        setSize(
               region.getKeyFrame(0).getRegionWidth() * Assets.PIXEL_SCALE,
               region.getKeyFrame(0).getRegionHeight() * Assets.PIXEL_SCALE);
        setPosition(x, y);
        setOrigin(getWidth() / 2, getHeight()/2);

        this.velocity.set(velocity);
        this.region = region;

        // give it a bit of a random force.
        applyForce(
                MathUtils.random(-50f, 50f) * 5,
                MathUtils.random(-10f, 10f) * 1,
                MathUtils.random(-5f, 5f) * 1
                );

        addAction(Actions.delay(1.5f,Actions.fadeOut(1)));
        addAction(Actions.delay(1.5f,Actions.removeActor()));
    }

    @Override
    protected TextureRegion getCell() {
        return region.getKeyFrame(age,true);
    }


    @Override
    public void act(float delta) {

        setRotation(getRotation() + delta * 150f);
        setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
        z = MathUtils.clamp(0f, z + velocity.z * delta, 150f);

        super.act(delta);
    }
}
