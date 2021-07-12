package net.mostlyoriginal.zen.actors.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.Inputs;
import net.mostlyoriginal.zen.actors.CollidableActor;
import net.mostlyoriginal.zen.screen.GameplayScreen;

/**
 * @author Daan van Yperen
 */
public class ZenProgress extends CollidableActor {

    private final TextureRegion partialBar;
    private float age;

    private float shake;

    private float progress;

    public ZenProgress(int x, int y) {

        progress = 0.25f;

        partialBar = new TextureRegion(Assets.uiProgress[0][1]);

        setSize(
                Assets.uiProgress[0][0].getRegionWidth() * Assets.PIXEL_SCALE * 0.5f,
                Assets.uiProgress[0][0].getRegionHeight() * Assets.PIXEL_SCALE * 0.5f);
        setPosition(x, y);
        setOrigin(getWidth() / 2, getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(getColor());

        batch.draw(getCell(),
                getX() + (MathUtils.random(-shake, shake)),
                getY() + (MathUtils.random(-shake, shake)), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    private TextureRegion getCell() {
        return Assets.uiProgress[0][(int) MathUtils.clamp((int) (progress * 6), 0, 5)];
    }

    @Override
    public void act(float delta) {

        if ( !isVisible()) return;

        shakeLogic(delta);

        super.act(delta);

        age += delta;
    }

    private void shakeLogic(float delta) {
        /*
        final boolean inDeathScene = GameplayScreen.getInstance().inDeathScene();
        if ( inDeathScene )
        {
            // give the players a little boost in the death scene.
            if ( shake == 0 ) shake = 1;
              progress += delta;
        } */

        if ( Inputs.attackOnce && shake <= 0) {
            shake = 5;

            GameplayScreen.getInstance().scoreZen(25);

            // testing.
            progress += delta * fillSpeed();
            if (progress > 2) progress = 0;
        } else

        // slowly decrease.
        if ( shake == 0 && progress > delta * 0.5f )
            progress -= delta * 0.5f;

        shake -= delta * 20;
        if (shake < 0) shake = 0;
    }

    private int fillSpeed() {

        switch (GameplayScreen.getInstance().getElevation() )
        {
            case 1: return 10;
            case 2: return 6;
            default: return 4;
        }
    }


    public void reset() {
        shake=0;
        progress=0;
    }

    public float getProgress() {
        return progress;
    }
}
