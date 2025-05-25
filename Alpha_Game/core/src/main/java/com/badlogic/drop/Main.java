package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Vector;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture backgroundForest2;
    Texture PlayerstandR;
    Texture PlayerstandL;
    Texture PlayerWalkR1;
    Texture PlayerWalkL1;
    Texture PlayerWalkR2;
    Texture PlayerWalkL2;
    Texture Enemy;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite PlayerSprite;
    Sprite EnemySprite;

    float animationTimer = 0f;
    float frameDuration = 0.53f;
    boolean moving = false;
    boolean side = false;

    Vector2 touchPos;

    @Override
    public void create() {
        backgroundForest2 = new Texture("backgroundForest2.png");
        PlayerstandR = new Texture("Player/standR.png");
        PlayerstandL = new Texture("Player/standL.png");
        PlayerWalkR1 = new Texture("Player/standWalkR1.png");
        PlayerWalkL1 = new Texture("Player/standWalkL1.png");
        PlayerWalkR2 = new Texture("Player/standWalkR2.png");
        PlayerWalkL2 = new Texture("Player/standWalkL2.png");
        Enemy = new Texture("Enemy/Enemy.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(9, 6);

        PlayerSprite = new Sprite(PlayerstandR);
        PlayerSprite.setSize(0.9f,1);
        PlayerSprite.setPosition(viewport.getWorldWidth() / 2f - PlayerSprite.getWidth() / 2f, 0.72f);

        EnemySprite = new Sprite(Enemy); // Inicializa o EnemySprite antes de usá-lo
        EnemySprite.setSize(0.9f, 1);
        EnemySprite.setPosition(1.5f, 0.72f); // Opcional: definir posição inicial

        touchPos = new Vector2();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input(){
        float speed = 2.3f;
        float delta = Gdx.graphics.getDeltaTime();
        moving = false;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            PlayerSprite.translateX(speed * delta);
            moving = true;
            side = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            PlayerSprite.translateX(-speed * delta);
            moving = true;
            side = true;
        }
    }

    private void logic(){
        float worldWidth = viewport.getWorldWidth();
        float PlayerWidth = PlayerSprite.getWidth();

        PlayerSprite.setX(MathUtils.clamp(PlayerSprite.getX(), 0, worldWidth - PlayerWidth));

        float delta = Gdx.graphics.getDeltaTime();

        if (moving) {
            animationTimer += delta;

            if (!side) { // Direita
                if (animationTimer < frameDuration) {
                    PlayerSprite.setTexture(PlayerWalkR1);
                } else if (animationTimer < frameDuration * 2) {
                    PlayerSprite.setTexture(PlayerWalkR2);
                } else {
                    animationTimer = 0f;
                }
            } else { // Esquerda
                if (animationTimer < frameDuration) {
                    PlayerSprite.setTexture(PlayerWalkL1);
                } else if (animationTimer < frameDuration * 2) {
                    PlayerSprite.setTexture(PlayerWalkL2);
                } else {
                    animationTimer = 0f;
                }
            }
        } else {
            // Parado
            if (side) {
                PlayerSprite.setTexture(PlayerstandL); // Esquerda
            } else {
                PlayerSprite.setTexture(PlayerstandR); // Direita
            }
            animationTimer = 0f;
        }
    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        spriteBatch.draw(backgroundForest2,0,0, worldWidth, worldHeight);

        PlayerSprite.draw(spriteBatch);
        EnemySprite.draw(spriteBatch);

        spriteBatch.end();
    }
    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        backgroundForest2.dispose();
        PlayerstandR.dispose();
        PlayerstandL.dispose();
        PlayerWalkR1.dispose();
        PlayerWalkL1.dispose();
        PlayerWalkR2.dispose();
        PlayerWalkL2.dispose();
        spriteBatch.dispose();
        Enemy.dispose();
    }
}
