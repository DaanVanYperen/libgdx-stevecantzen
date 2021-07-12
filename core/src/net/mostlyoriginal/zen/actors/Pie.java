package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Daan van Yperen
 */
public class Pie extends CombatActor {

    public static final int CELL_STEAM_0 = 0;
    public static final int CELL_STEAM_1 = 1;
    public static final int CELL_STEAM_2 = 2;
    public static final int CELL_STEAM_3 = 3;

    private Animation<TextureRegion>[] walking;
    private Animation<TextureRegion>[] standing;

    public Pie(TextureRegion[][] cells, int x, int y) {
        super(cells, null, x, y);
        walking = standing = createAnimation(0.2f, CELL_STEAM_0, CELL_STEAM_0, CELL_STEAM_0, CELL_STEAM_0, CELL_STEAM_1, CELL_STEAM_2, CELL_STEAM_3 );
    }

    @Override
    protected String getPacifistHitMessage() {
        return "\"That potato pie looks delicious but it isn't mine.\"";
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

        // TODO proper PIE-AI!
        super.act(delta);
    }


}
