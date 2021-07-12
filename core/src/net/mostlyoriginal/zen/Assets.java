package net.mostlyoriginal.zen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Antipattern but pragmatic given time.
 */
public class Assets {

    public static final int PIXEL_SCALE = 4;

    public static final int GRID_SCALE = 10;
    public static final int FACE_RIGHT = 1;
    public static final int FACE_LEFT = 0;


    private static Texture[] bgTexture = new Texture[4];
    private static Texture logoTexture;
    private static Texture spriteTexture;
    public static BitmapFont font;
    public static BitmapFont fontSmall;
    public static BitmapFont fontHuge;

    // background
    public static TextureRegion[] background = new TextureRegion[4];

    // scenery
    public static TextureRegion bonsai[] = new TextureRegion[4];
    public static TextureRegion statue[] = new TextureRegion[4];

    public static TextureRegion[][] steve;
    public static TextureRegion[][] deer;
    public static TextureRegion[][] songbird;
    public static TextureRegion[][] preybird;
    public static TextureRegion[][] pie;

    public static Animation<TextureRegion>[] deerGibs;
    public static Animation<TextureRegion>[] preybirdGibs;

    // ui
    public static TextureRegion[][] uiBlankButton;
    public static TextureRegion[][] uiProgress;
    public static TextureRegion censored;
    public static TextureRegion heart;
    public static TextureRegion logo;
    public static TextureRegion textBackground;
    public static TextureRegion[][] steveWounded;
    public static TextureRegion[][] steveDeadlyWounded;

    public static Sound damageDealt;
    public static Sound birdTweet;
    public static Sound steveKicks;
    public static Sound steveZenPowerup;
    public static Sound deerOhLaLa;

    private Assets() {
    }

    public static void loadResources() {
        bgTexture[0] = new Texture(Gdx.files.internal("img/background.png"));
        bgTexture[1] = new Texture(Gdx.files.internal("img/background-stage2.png"));
        bgTexture[2] = new Texture(Gdx.files.internal("img/background-stage3.png"));
        bgTexture[3] = new Texture(Gdx.files.internal("img/background-stage4.png"));
        logoTexture = new Texture(Gdx.files.internal("img/logo.png"));
        spriteTexture = new Texture(Gdx.files.internal("img/sprites.png"));

        font = new BitmapFont(Gdx.files.internal("font/tahoma-10.fnt"), true);
        font.setColor(0, 0, 0, 0.9f);
        font.getData().setScale(2f);

        fontHuge = new BitmapFont(Gdx.files.internal("font/tahoma-10.fnt"), true);
        fontHuge.setColor(0, 0, 0, 0.9f);
        fontHuge.getData().setScale(2f);

        fontSmall = new BitmapFont(Gdx.files.internal("font/tahoma-10.fnt"), true);
        fontSmall.setColor(0, 0, 0, 0.9f);
        font.getData().setScale(2f);

        buildRegions();
    }

    private static void buildRegions() {

        for ( int i=0; i<4;i++)
        {
            background[i] = new TextureRegion(bgTexture[i]);
            background[i].flip(false, true);
        }

        logo = new TextureRegion(logoTexture);
        logo.flip(false, true);

        textBackground = createRegionFromGrid(9, 7, 1, 1);

        // meditative state 1 (waking) to 4 (victory)
        statue[0] = createRegionFromGrid(0, 8, 2, 3);
        statue[1] = createRegionFromGrid(12, 8, 2, 3);
        statue[2] = createRegionFromGrid(17, 8, 2, 3);
        statue[3] = createRegionFromGrid(22, 8, 2, 3);

        // meditative state 1 (waking) to 4 (victory)
        bonsai[0] = createRegionFromGrid(2, 8, 3, 3);
        bonsai[1] = createRegionFromGrid(14, 8, 3, 3);
        bonsai[2] = createRegionFromGrid(19, 8, 3, 3);
        bonsai[3] = createRegionFromGrid(24, 8, 3, 3);

        censored = createRegionFromGrid(9, 13, 4, 2);
        heart = createRegionFromGrid(9, 15, 2, 2);

        steve = createRegionsFromGrid(0, 0, 2, 2, 14);
        steveWounded = createRegionsFromGrid(0, 27, 2, 2, 14);
        steveDeadlyWounded = createRegionsFromGrid(0, 29, 2, 2, 14);

        deer = createRegionsFromGrid(0, 2, 2, 2, 7);
        songbird = createRegionsFromGrid(0, 4, 2, 2, 4);
        preybird = createRegionsFromGrid(6, 4, 2, 2, 4);
        pie = createRegionsFromGrid(0, 6, 2, 2, 4);

        uiBlankButton = createRegionsFromGrid(12, 15, 2, 3, 5);
        uiProgress = createRegionsFromGrid(0, 18, 2, 7, 6);

        deerGibs = regionToSingleFrameAnimation(createRegionsFromGrid(14, 2, 2, 2, 6));
        preybirdGibs = regionToSingleFrameAnimation(createRegionsFromGrid(14, 4, 2, 2, 3));

        // brain slowly slipping.
        damageDealt = Gdx.audio.newSound(Gdx.files.internal("sound/hit-punch.wav"));
        birdTweet = Gdx.audio.newSound(Gdx.files.internal("sound/bird-tweet.wav"));
        steveKicks = Gdx.audio.newSound(Gdx.files.internal("sound/steve-kicks.wav"));
        steveZenPowerup = Gdx.audio.newSound(Gdx.files.internal("sound/steve-zen-powerup.wav"));
        deerOhLaLa = Gdx.audio.newSound(Gdx.files.internal("sound/deer-oh-la-la.wav"));
    }

    private static Animation<TextureRegion>[] regionToSingleFrameAnimation(TextureRegion[][] regions) {

        final Animation<TextureRegion>[] cells = new Animation[regions[0].length * 2];

        int count = 0;
        for (int i = 0; i < regions[0].length; i++) {
            cells[count++] = new Animation(1000, regions[0][i]);
            cells[count++] = new Animation(1000, regions[1][i]);
        }
        return cells;
    }

    private static TextureRegion[][] createRegionsFromGrid(int gx, int gy, int gw, int gh, int count) {

        final TextureRegion[][] cells = new TextureRegion[2][count];

        for (int i = 0; i < count; i++) {
            cells[FACE_RIGHT][i] = createRegionFromGrid(gx + i * gw, gy, gw, gh);
            cells[FACE_LEFT][i] = createRegionFromGrid(gx + i * gw, gy, gw, gh);
            cells[FACE_LEFT][i].flip(true, false);
        }

        return cells;
    }

    private static TextureRegion createRegionFromGrid(int gx, int gy, int gw, int gh) {
        TextureRegion textureRegion = new TextureRegion(spriteTexture, gx * GRID_SCALE, gy * GRID_SCALE, gw * GRID_SCALE, gh * GRID_SCALE);
        textureRegion.flip(false, true);
        return textureRegion;
    }

    public static void dispose() {
        for (int i=0;i<4;i++)
        if (bgTexture[i] != null) {
            bgTexture[i].dispose();
            bgTexture[i] = null;
        }
        if (logoTexture != null) {
            logoTexture.dispose();
            logoTexture = null;
        }
        if (spriteTexture != null) {
            spriteTexture.dispose();
            spriteTexture = null;
        }
        if (font != null) {
            font.dispose();
            font = null;
        }
        if (fontSmall != null) {
            fontSmall.dispose();
            fontSmall = null;
        }
        if (fontHuge != null) {
            fontHuge.dispose();
            fontHuge = null;
        }

    }
}