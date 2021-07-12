package net.mostlyoriginal.zen.actors.ui;

import com.badlogic.gdx.graphics.g2d.*;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.actors.CollidableActor;

/**
 * @author Daan van Yperen
 */
public class KeyHint extends CollidableActor {

    private final String text;
    private final Animation<TextureRegion> animation;
    private float age;
    private boolean animated = false;

    public KeyHint(String text, int x, int y) {
        this(text, x, y, true);

    }

    public KeyHint(String text, int x, int y, boolean animated) {
        this.text = text;
        this.animated = animated;

        // animated or static button.
        animation = animated ? new Animation<>(0.1f,
                Assets.uiBlankButton[0][0],
                Assets.uiBlankButton[0][1],
                Assets.uiBlankButton[0][2],
                Assets.uiBlankButton[0][3]) : new Animation<>(1, Assets.uiBlankButton[0][4]);

        setSize(
                Assets.uiBlankButton[0][0].getRegionWidth() * Assets.PIXEL_SCALE * 0.5f,
                Assets.uiBlankButton[0][0].getRegionHeight() * Assets.PIXEL_SCALE * 0.5f);
        setPosition(x, y);
        setOrigin(getWidth() / 2, getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(getColor());
        batch.draw(animation.getKeyFrame(age, true), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        Assets.font.setColor(0,0,0,getColor().a);
        Assets.font.draw(batch, text, getX() + 16, getY() + 16 + (animated && (animation.getKeyFrameIndex(age) % 2 == 0) ? 3 : 0));
    }

    @Override
    public void act(float delta) {

        super.act(delta);

        age += delta;
    }
}
