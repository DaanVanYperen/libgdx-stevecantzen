package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mostlyoriginal.zen.Assets;

/**
 * Non-moving scenery.
 *
 * @author Daan van Yperen
 */
public class StaticScenery extends CollidableActor {

    private TextureRegion[] regions;
    private TextureRegion region;

    public StaticScenery(TextureRegion region, int x, int y) {
        this.region = region;
        setSize(region.getRegionWidth() * Assets.PIXEL_SCALE, region.getRegionHeight() * Assets.PIXEL_SCALE);
        setPosition(x,y);
    }

    public StaticScenery(TextureRegion[] regions, int x, int y) {
        this.region = regions[0];
        this.regions = regions;
        setSize(region.getRegionWidth() * Assets.PIXEL_SCALE, region.getRegionHeight() * Assets.PIXEL_SCALE);
        setPosition(x,y);
    }

    public void setActiveCell( int index )
    {
        this.region = regions[index];
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(getColor());
        batch.draw(region, getX(), getY(),getOriginX(), getOriginY(), getWidth(), getHeight(),getScaleX(),getScaleY(),getRotation() );
    }

}
