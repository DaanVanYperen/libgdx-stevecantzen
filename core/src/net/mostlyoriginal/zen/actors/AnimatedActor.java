package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.zen.Assets;

/**
 * @author Daan van Yperen
 */
public abstract class AnimatedActor extends CollidableActor {

    protected TextureRegion[][] cells;
    protected int facing = Assets.FACE_RIGHT;
    protected float z = 0;

    protected Vector3 velocity = new Vector3();


    // previous coordinates.
    protected float px, py;

    // delta
    protected float dx, dy;

    protected float age = 0;

    @Override
    public void act(float delta) {

        age += delta;

        px = getX();
        py = getY();

        super.act(delta);

        velocity.x = decreaseVelocity(velocity.x, delta * 400);
        velocity.y = decreaseVelocity(velocity.y, delta * 400);
        velocity.z = decreaseVelocity(velocity.z, delta * 400);

        updateDelta();
    }

    protected float decreaseVelocity(float current, float decrease)
    {
        if ( current > 0 ) { current -= decrease; return current > 0 ? current : 0; }
        if ( current < 0 ) { current += decrease; return current < 0 ? current : 0; }
        return current;
    }

    protected void updateDelta() {
        dx  = getX() - px;
        dy  = getY() - py;

        // derive facing on delta x.
        if ( dx < 0 ) facing = Assets.FACE_LEFT;
        if ( dx > 0 ) facing = Assets.FACE_RIGHT;
    }

    public boolean isMoving()
    {
        return (dx != 0) || (dy != 0);
    }

    public AnimatedActor(TextureRegion[][] cells) {
        this.cells = cells;
    }

    protected abstract TextureRegion getCell();

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final TextureRegion cell = getCell();
        if (cell != null) {
            batch.setColor(getColor());
            batch.draw(cell, getX(), getY() - z, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }

    /**
     * Build a left and right-facing animation out of cells.
     *
     * @param index one or more cells to combine.
     * @return left and right facing animation
     */
    protected Animation<TextureRegion>[] createAnimation(float frameDuration, int... index) {
        TextureRegion[][] regions = new TextureRegion[2][index.length];

        int c = 0;
        for (int i : index) {
            regions[Assets.FACE_LEFT][c] = cells[Assets.FACE_LEFT][i];
            regions[Assets.FACE_RIGHT][c] = cells[Assets.FACE_RIGHT][i];
            c++;
        }

        return new Animation[]{
                new Animation<TextureRegion>(frameDuration, regions[Assets.FACE_LEFT]),
                new Animation<TextureRegion>(frameDuration, regions[Assets.FACE_RIGHT])};
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void applyForce( float x, float y, float z )
    {
        velocity.x += x;
        velocity.y += y;
        velocity.z += z;
    }

}
