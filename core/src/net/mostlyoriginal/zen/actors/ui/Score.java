package net.mostlyoriginal.zen.actors.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import net.mostlyoriginal.zen.Assets;
import net.mostlyoriginal.zen.actors.CollidableActor;

/**
 * @author Daan van Yperen
 */
public class Score extends CollidableActor {

    private final String text;
    private float age;
    private final BitmapFont font;

    private GlyphLayout glyphLayout = new GlyphLayout();;

    public Score(String text, int x, int y) {
        this.text = text;

        font = text.length() < 4 ? Assets.fontSmall : Assets.font;

        glyphLayout.setText(font,text);

        setWidth(64);
        setPosition(x - glyphLayout.width/2, y);
        setOrigin(getWidth() / 2, getHeight());
        addAction(Actions.moveBy(MathUtils.random(-40, 40),-50,1, Interpolation.swing));
        addAction(Actions.delay(1, Actions.removeActor()));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(1,1,1,1);
        font.draw(batch, text, getX(), getY());
        batch.setColor(0,0,0,1);
        font.draw(batch, text, getX(), getY());
    }

    @Override
    public void act(float delta) {

        super.act(delta);

        age += delta;
    }
}
