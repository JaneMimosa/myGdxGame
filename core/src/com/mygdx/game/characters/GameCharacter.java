package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;

public abstract class GameCharacter {
    Texture texture;
    Texture textureHP;
    Vector2 position;
    float speed;
    float hp, hpMax;
    Vector2 direction;


    GameScreen gameScreen;

    float attackTimer;
    float damageEffectTimer;
    Weapon weapon;
    Vector2 temp;
    StringBuilder stringHelper;
    public boolean isAlive() {
        return hp>0;
    }

public GameCharacter () {
        temp = new Vector2(0,0);
        stringHelper = new StringBuilder();
}
    public Vector2 getPosition() {
        return position;
    }

    public abstract void update(float dt);

    public void render(SpriteBatch batch, BitmapFont font24) {
        if(damageEffectTimer > 0.0f) {
            batch.setColor(1, 1 - damageEffectTimer, 1 - damageEffectTimer, 1);
        }
        batch.draw(texture, position.x - 50, position.y - 50);
        batch.setColor(1, 1, 1, 1);
        batch.setColor(0, 0, 0, 1);
        batch.draw(textureHP, position.x - 52, position.y - 52, 104, 16);
        batch.setColor(1, 0, 0, 1);
        batch.draw(textureHP, position.x - 50, position.y - 52, 0, 0, hp / hpMax * 100, 10, 1, 1, 0, 0, 0, 80, 20, false, false);
        batch.setColor(1, 1, 1, 1);
        stringHelper.setLength(0);
        stringHelper.append((int) hp);
        font24.draw(batch, stringHelper, position.x -50, position.y - 35, 80, 1, false);
    }

    public void checkScreenBounds() {
        if (position.x > 1280.0f) {
            position.x = 1280.0f;
        }
        if (position.x < 0.0f) {
            position.x = 0.0f;
        }
        if (position.y > 720.0f) {
            position.y = 720.0f;
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
        }
    }
    public void takeDamage(float amount) {
        hp -= amount;
        damageEffectTimer += 0.5f;
        if (damageEffectTimer > 1.0f) {
            damageEffectTimer = 1.0f;
        }
    }
    public void moveForward(float dt) {
        if(gameScreen.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed *dt))) {
            position.set(temp);
        } else if(gameScreen.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed *dt).set(temp.x, position.y))) {
            position.set(temp);
        } else if(gameScreen.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed *dt).set(position.x, temp.y))) {
            position.set(temp);
        }
    }
}
