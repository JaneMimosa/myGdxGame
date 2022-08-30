package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.characters.GameCharacter;
import com.mygdx.game.characters.Hero;
import com.mygdx.game.characters.Monster;

import java.util.*;

public class GameScreen {

    private SpriteBatch batch;
    private BitmapFont font24;

    private Map map;

    private Hero hero;
    private ItemsEmitter itemsEmitter;
    private TextEmitter textEmitter;
    private List<GameCharacter> allCharacters;
    private List<Monster> allMonsters;

    private Comparator<GameCharacter> drawOrderComparator;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public Hero getHero() {
        return hero;
    }


    public List<Monster> getAllMonsters() {
        return allMonsters;
    }

    public TextEmitter getTextEmitter() {
        return textEmitter;
    }

    public void create() {
        map = new Map();
        allCharacters = new ArrayList<>();
        allMonsters = new ArrayList<>();
        itemsEmitter = new ItemsEmitter();
        textEmitter = new TextEmitter();
        hero = new Hero(this);
        allCharacters.addAll(Arrays.asList(hero, new Monster(this), new Monster(this)));
        for (int i = 0; i < allCharacters.size(); i++) {
            if(allCharacters.get(i) instanceof Monster) {
                allMonsters.add((Monster) allCharacters.get(i));
            }
        }
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));

        drawOrderComparator = new Comparator<GameCharacter>() {
            @Override
            public int compare(GameCharacter o1, GameCharacter o2) {
                return (int) (o2.getPosition().y - o1.getPosition().y);
            }
        };
    }

    public Map getMap() {
        return map;
    }

    public void render () {
            float dt = Gdx.graphics.getDeltaTime();
            update(dt);
            ScreenUtils.clear(0, 0, 0, 1);
            batch.begin();
            map.render(batch);
            Collections.sort(allCharacters, drawOrderComparator);
            for (int i = 0; i < allCharacters.size(); i++) {
                allCharacters.get(i).render(batch, font24);
            }
            itemsEmitter.render(batch);
            hero.renderHUD(batch, font24);
        textEmitter.render(batch, font24);
            batch.end();
        }
        public void update ( float dt){
            for (int i = 0; i < allCharacters.size(); i++) {
                allCharacters.get(i).update(dt);
            }
            for (int i = 0; i < allMonsters.size(); i++) {
                Monster currentMonster = allMonsters.get(i);
                if(!currentMonster.isAlive()) {
                    allMonsters.remove(currentMonster);
                    allCharacters.remove(currentMonster);
                    itemsEmitter.generateRandomItem(currentMonster.getPosition().x, currentMonster.getPosition().y, 5, 0.6f);
                    hero.killMonster(currentMonster);
                }
            }
            for (int i = 0; i < itemsEmitter.getItems().length; i++) {
                Item it = itemsEmitter.getItems()[i];
                if(it.isActive()) {
                    float dst = hero.getPosition().dst(it.getPosition());
                    if(dst < 24.0f) {
                        hero.useItem(it);
                    }
                }
            }
            itemsEmitter.update(dt);
        }
    }
