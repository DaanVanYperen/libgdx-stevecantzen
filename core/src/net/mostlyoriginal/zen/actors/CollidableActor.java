package net.mostlyoriginal.zen.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author Daan van Yperen
 */
public class CollidableActor extends Actor {
    public boolean overlaps (Actor other) {
   		return !(getX() > other.getX() + other.getWidth() || getX() + getWidth() < other.getX() || getY() > other.getY() + other.getHeight() || getY() + getHeight() < other.getY());
   	}

    // offset self box.
    public boolean overlaps(Actor other, float offsetX) {
        return !(getX()+offsetX > other.getX() + other.getWidth() || getX()+offsetX + getWidth() < other.getX() || getY() > other.getY() + other.getHeight() || getY() + getHeight() < other.getY());
    }
}
