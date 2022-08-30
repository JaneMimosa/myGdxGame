package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Item;
import com.mygdx.game.Weapon;

public class Hero extends GameCharacter {

    private String name;

    private int coins;
    private int level;
    private int exp;
    private int[] expTo = {0, 0, 100, 300, 600, 1000, 5000};

    public Hero(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.level = 1;
        this.name = "Jane";
        this.texture = new Texture("knight.png");
        this.textureHP = new Texture("bar.png");
        this.position = new Vector2(200, 200);
        while (!gameScreen.getMap().isCellPassable(position))
            this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        this.hpMax = 100.0f;
        this.direction = new Vector2(0, 0);
        this.hp = hpMax;
        this.speed = 100.0f;
        this.weapon = new Weapon("Sword", 50.0f, 0.5f, 5.0f);
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24){
        stringHelper.setLength(0);
        stringHelper.append("Knight: " ).append(name).append("\n")
                        .append("Level: ").append(level).append("\n")
                        .append("Exp: ").append(exp).append("/").append(expTo[level+1]).append("\n")
                        .append("Coins: ").append(coins);
        font24.draw(batch, stringHelper, 20 , 700);
    }
@Override
    public void update(float dt) {
        damageEffectTimer -= dt;

        Monster nearestMonster = null;
        float minDist = Float.MAX_VALUE;
    for (int i = 0; i < gameScreen.getAllMonsters().size(); i++) {
        float dst = gameScreen.getAllMonsters().get(i).getPosition().dst(this.position);
        if(dst  < minDist) {
            minDist = dst;
            nearestMonster = gameScreen.getAllMonsters().get(i);
        }
    }

        if (nearestMonster != null && minDist < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer > weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                nearestMonster.takeDamage(weapon.getDamage());
            }
        }


    direction.set(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction.x = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x = -1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.y = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = - 1.0f;
        }

        temp.set(position).mulAdd(direction, speed*dt);
        if(gameScreen.getMap().isCellPassable(temp)) {
            position.mulAdd(direction, speed*dt);

        }
        moveForward(dt);
        checkScreenBounds();
    }
 public void useItem(Item it) {
        switch (it.getType()) {
            case COINS:
                int amount = MathUtils.random(3, 5);
                coins+= amount;
                stringHelper.setLength(0);
                stringHelper.append("+").append(coins).append("coins");
                gameScreen.getTextEmitter().setup(it.getPosition().x, it.getPosition().y, stringHelper);
                break;
            case MEDKIT:
                hp += 10;
                if(hp >hpMax) {
                    hp = hpMax;
                }
                stringHelper.setLength(0);
                stringHelper.append("+").append("Medkit");
                gameScreen.getTextEmitter().setup(it.getPosition().x, it.getPosition().y, stringHelper);
                break;
        }
        it.deactivate();
 }
 public void killMonster(Monster monster) {
        exp+= monster.hpMax *5;
        if(exp> expTo[level+1]) {
            level++;
            hpMax += 10;
            hp=hpMax;
            exp -= expTo[level];
        }
 }

}
