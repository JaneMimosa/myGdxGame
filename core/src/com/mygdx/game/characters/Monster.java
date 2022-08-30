package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;

public class Monster extends GameCharacter {
    private float activityRadius;

    private float moveTimer;



    public Monster(GameScreen gameScreen) {
        this.texture = new Texture("monster.png");
        this.textureHP = new Texture("bar.png");
        this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        while (!gameScreen.getMap().isCellPassable(position))
            this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.direction = new Vector2(0, 0);

        this.speed = 40.0f;
        this.hpMax = 40.0f;
        this.hp = hpMax;
        this.activityRadius = 200.0f;
        this.weapon = new Weapon("Rusty Sword", 50.0f, 1.0f, 2.0f);
        this.gameScreen = gameScreen;

    }


    @Override
    public void update(float dt) {
        float dst = gameScreen.getHero().getPosition().dst(this.position);
        if (dst < activityRadius) {
            direction.set(gameScreen.getHero().getPosition()).sub(this.position).nor();

        } else {
            moveTimer -= dt;
            if (moveTimer < 0.0f) {
                moveTimer = MathUtils.random(1.0f, 4.0f);
                direction.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
                direction.nor();
            }
        }
        moveForward(dt);


        if (dst < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer >= weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                gameScreen.getHero().takeDamage(weapon.getDamage());
            }
        }
        checkScreenBounds();
    }
}
