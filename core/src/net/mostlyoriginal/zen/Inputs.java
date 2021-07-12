package net.mostlyoriginal.zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Anti-pattern but pragmatic.
 *
 * @author Daan van Yperen
 */
public class Inputs {

    public static boolean left;
    public static boolean right;
    public static boolean up;
    public static boolean down;
    public static boolean attack;
    public static boolean attackOnce;
    public static boolean attackSecondary;
    public static boolean attackSecondaryOnce;
    public static boolean escape;
    public static boolean escapeOnce;

    public static void hookup()
    {
        Gdx.input.setInputProcessor(new MyListener());
    }

    public static void reset()
    {
        attackOnce = false;
        attackSecondaryOnce = false;
        escapeOnce = false;
    }


    private static class MyListener implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {

            switch ( keycode )
            {
                case Input.Keys.LEFT:
                case Input.Keys.A: left = true; break;
                case Input.Keys.RIGHT:
                case Input.Keys.D: right = true; break;
                case Input.Keys.UP:
                case Input.Keys.W: up = true; break;
                case Input.Keys.DOWN:
                case Input.Keys.S: down = true; break;
                case Input.Keys.SPACE:
                case Input.Keys.E:
                {
                    attack = true;
                    attackOnce = true;
                    break;
                }
                case Input.Keys.Z:
                case Input.Keys.Q:
                {
                    attackSecondary = true;
                    attackSecondaryOnce = true;
                    break;
                }
                case Input.Keys.ESCAPE:
                {
                    escape = true;
                    escapeOnce = true;
                    break;
                }
                default: return false;
            }

            return true;
        }

        @Override
        public boolean keyUp(int keycode) {

            switch ( keycode )
            {
                case Input.Keys.LEFT:
                case Input.Keys.A: left = false; break;
                case Input.Keys.RIGHT:
                case Input.Keys.D: right = false; break;
                case Input.Keys.UP:
                case Input.Keys.W: up = false; break;
                case Input.Keys.DOWN:
                case Input.Keys.S: down = false; break;
                case Input.Keys.SPACE:
                case Input.Keys.E: attack = false; attackOnce=false; break;
                case Input.Keys.Z:
                case Input.Keys.Q:
                    attackSecondary = false;
                    attackSecondaryOnce=false; break;
                case Input.Keys.ESCAPE: escape = false; escapeOnce=false; break;
                default: return false;
            }

            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
