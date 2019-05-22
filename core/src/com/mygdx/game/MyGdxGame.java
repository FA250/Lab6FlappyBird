package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird, bird2;
	Texture[] pipesUp, pipesDown;
	int positionBirdX, positionBirdY, timeCountBirdWingsUpdate, timeCountBirdFallUpdate,timeCountUpdatePipes, updateRate, birdFallRate, currentTubeCount;
	boolean flagChangeImageBird=true, flagDrawPipes[];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		bird = new Texture("bird.png");
		bird2 = new Texture("bird2.png");
		pipesUp = new Texture[]{new Texture("bottomtube.png"), new Texture("bottomtube.png"), new Texture("bottomtube.png"), new Texture("bottomtube.png")};
		pipesDown = new Texture[]{new Texture("toptube.png"),new Texture("toptube.png"),new Texture("toptube.png"),new Texture("toptube.png")};
		positionBirdX=(Gdx.graphics.getWidth() / 2)-(bird.getWidth()/2);
		positionBirdY=(Gdx.graphics.getHeight() / 2)-(bird.getHeight()/2);
		timeCountBirdWingsUpdate=0;
		timeCountBirdFallUpdate=0;
		timeCountUpdatePipes=0;
		updateRate=50;
		birdFallRate=14;
		currentTubeCount=0;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (flagChangeImageBird) {
			batch.draw(bird2, positionBirdX, positionBirdY);
		} else {
			batch.draw(bird, positionBirdX, positionBirdY);
		}

		if (Gdx.input.justTouched()) {
			timeCountBirdWingsUpdate=0;
			timeCountBirdFallUpdate=0;
			birdFallRate=14;
			positionBirdY += 40;
			if (flagChangeImageBird)
				flagChangeImageBird=false;
			else
				flagChangeImageBird=true;
		}

		if(timeCountBirdWingsUpdate++==20) {
			flagChangeImageBird=false;
			timeCountBirdWingsUpdate = 0;
		}

		if(timeCountBirdFallUpdate++==birdFallRate) {
			flagChangeImageBird=false;
			if(birdFallRate!=0)
				birdFallRate -=2;
			timeCountBirdFallUpdate=0;
			positionBirdY -=10;
		}

		if(timeCountUpdatePipes++==updateRate) {

			if(currentTubeCount==3)
				currentTubeCount=0;
			else
				currentTubeCount++;

		}
		batch.end();
	}

	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
