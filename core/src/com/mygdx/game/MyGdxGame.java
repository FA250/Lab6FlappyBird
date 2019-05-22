package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Type;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,bird, bird2, gameOver, btnEasy,btnNormal,btnHard;
	Texture[] pipesUp, pipesDown;
	int [] positionPipeX, positionBottomPipeY, positionTopPipeY;
	int positionBirdX, positionBirdY, timeCountBirdWingsUpdate, timeCountBirdFallUpdate,timeCountUpdatePipes, pipesUpdateRate, birdFallRate, currentTubeCount, distanceBetweenPipes,gapBetweenTopBottomPipe;
	int heightScreen,widthScreen, pipeDownHeight, pipeDownWidth,pipeUpHeight,pipeUpWidth, birdRadius;
	boolean flagChangeImageBird=true, flagDrawPipes[], firstTime, hasentLose, playGame;
	Circle collisionBird;
	Rectangle[] collisionPipeUp, collisionPipeDown;


	@Override
	public void create () {
		heightScreen=Gdx.graphics.getHeight();
		widthScreen=Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		bird = new Texture("bird.png");
		bird2 = new Texture("bird2.png");
		pipesUp = new Texture[]{new Texture("bottomtube.png"), new Texture("bottomtube.png"), new Texture("bottomtube.png"), new Texture("bottomtube.png")};
		pipesDown = new Texture[]{new Texture("toptube.png"),new Texture("toptube.png"),new Texture("toptube.png"),new Texture("toptube.png")};
		gameOver= new Texture("game_over.png");
		btnEasy=new Texture("btneasy.png");
        btnNormal=new Texture("btnnormal.png");
        btnHard=new Texture("btnhard.png");

		pipeDownHeight=pipesDown[0].getHeight();
		pipeDownWidth=pipesUp[0].getWidth();
		pipeUpHeight=pipesUp[0].getHeight();
		pipeUpWidth=pipesDown[0].getWidth();
		birdRadius=bird.getWidth()/2-2;

		playGame=false;
		hasentLose=true;
		//resetGame();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0, widthScreen, heightScreen);

		if(hasentLose && playGame) {
			if (flagChangeImageBird) {
				batch.draw(bird2, positionBirdX, positionBirdY);
			} else {
				batch.draw(bird, positionBirdX, positionBirdY);
			}


			batch.draw(pipesDown[0], positionPipeX[0], positionTopPipeY[0]);
			batch.draw(pipesDown[1], positionPipeX[1], positionTopPipeY[1]);
			batch.draw(pipesDown[2], positionPipeX[2], positionTopPipeY[2]);
			batch.draw(pipesDown[3], positionPipeX[3], positionTopPipeY[3]);

			batch.draw(pipesUp[0], positionPipeX[0], positionBottomPipeY[0]);
			batch.draw(pipesUp[1], positionPipeX[1], positionBottomPipeY[1]);
			batch.draw(pipesUp[2], positionPipeX[2], positionBottomPipeY[2]);
			batch.draw(pipesUp[3], positionPipeX[3], positionBottomPipeY[3]);

			collisionBird.x=positionBirdX+birdRadius; collisionBird.y = positionBirdY+birdRadius;


			for(int i=0; i<collisionPipeUp.length;i++){
			    collisionPipeDown[i].x=positionPipeX[i];
                collisionPipeDown[i].y=positionTopPipeY[i];
                collisionPipeUp[i].x=positionPipeX[i];
                collisionPipeUp[i].y=positionBottomPipeY[i];

				if(Intersector.overlaps(collisionBird,collisionPipeDown[i])||Intersector.overlaps(collisionBird,collisionPipeUp[i]))
					hasentLose=false;
			}

			if(positionBirdY>heightScreen-bird.getHeight()||positionBirdY<0)
				hasentLose=false;

			if (Gdx.input.justTouched()) {
				timeCountBirdWingsUpdate = 0;
				timeCountBirdFallUpdate = 0;
				birdFallRate = 30;
				positionBirdY += 65;
				if (flagChangeImageBird)
					flagChangeImageBird = false;
				else
					flagChangeImageBird = true;
			}

			if (timeCountBirdWingsUpdate++ == 20) {
				flagChangeImageBird = false;
				timeCountBirdWingsUpdate = 0;
			}

			if (birdFallRate != 0)
				birdFallRate -= 0.1;

			if (timeCountBirdFallUpdate++ >= birdFallRate) {
				flagChangeImageBird = false;
				timeCountBirdFallUpdate = 0;
				positionBirdY -= 10;
			}

			if (timeCountUpdatePipes++ == pipesUpdateRate) {

				if (currentTubeCount == 3) {
					if ((positionPipeX[currentTubeCount] - positionPipeX[currentTubeCount - 1]) >= distanceBetweenPipes && !flagDrawPipes[currentTubeCount]) {
						flagDrawPipes[currentTubeCount] = true;
						currentTubeCount = 0;
					}
				} else {
					if (currentTubeCount == 0) {
						if ((positionPipeX[currentTubeCount] - positionPipeX[3]) >= distanceBetweenPipes && !flagDrawPipes[currentTubeCount] || firstTime) {
							firstTime = false;
							flagDrawPipes[currentTubeCount] = true;
							currentTubeCount++;
						}
					} else if (positionPipeX[currentTubeCount] - (positionPipeX[currentTubeCount - 1]) >= distanceBetweenPipes && !flagDrawPipes[currentTubeCount]) {
						flagDrawPipes[currentTubeCount] = true;
						currentTubeCount++;
					}
				}

				for (int i = 0; i < positionPipeX.length; i++) {
					if (flagDrawPipes[i]) {
						if (positionPipeX[i] < -pipesUp[i].getWidth()) {
							flagDrawPipes[i] = false;
							resetPipe();
						} else
							positionPipeX[i] -= 12;
					}
				}

				timeCountUpdatePipes = 0;
			}
		}else if(!playGame){
			batch.draw(btnEasy, widthScreen/2-btnEasy.getWidth()/2, heightScreen/2-btnEasy.getHeight()/2+200);
			batch.draw(btnNormal, widthScreen/2-btnNormal.getWidth()/2, heightScreen/2-btnNormal.getHeight()/2);
			batch.draw(btnHard, widthScreen/2-btnHard.getWidth()/2, heightScreen/2-btnHard.getHeight()/2-200);

			if(Gdx.input.isTouched())
			{
				Vector3 touch=new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
				Rectangle buttonEasy=new Rectangle(widthScreen/2-btnEasy.getWidth()/2,heightScreen/2-btnEasy.getHeight()/2-200,btnEasy.getWidth(),btnEasy.getHeight());
                Rectangle buttonNormal=new Rectangle(widthScreen/2-btnEasy.getWidth()/2,heightScreen/2-btnEasy.getHeight()/2,btnEasy.getWidth(),btnEasy.getHeight());
                Rectangle buttonHard=new Rectangle(widthScreen/2-btnEasy.getWidth()/2,heightScreen/2-btnEasy.getHeight()/2+200,btnEasy.getWidth(),btnEasy.getHeight());
				
				if(buttonEasy.contains(touch.x,touch.y))
					resetGame(5,700,500);
				else if(buttonNormal.contains(touch.x,touch.y))
                    resetGame(2,600,450);
				else if(buttonHard.contains(touch.x,touch.y))
                    resetGame(1,500,400);
			}
		}
		else{
			batch.draw(gameOver,(widthScreen / 2)-(gameOver.getWidth()/2),(heightScreen / 2)-(gameOver.getHeight()/2));

            if (Gdx.input.justTouched())
                playGame=false;
		}
		batch.end();
	}

	private void resetPipe(){
		positionPipeX[currentTubeCount] = widthScreen;
		int random = new Random().nextInt(((heightScreen-(heightScreen/4))-(heightScreen/2)+1)) + (heightScreen/2);
		positionBottomPipeY[currentTubeCount] = random-gapBetweenTopBottomPipe-pipeUpHeight;
		positionTopPipeY[currentTubeCount] = random;
	}

	private void resetGame(int pPipesUpdateRate,int pDistanceBetweenPipes, int pGapBetweenTopBottomPipe){
		positionBirdX=(widthScreen / 2)-(bird.getWidth()/2);
		positionBirdY=(heightScreen / 2)-(bird.getHeight()/2);
		positionPipeX = new int[]{ widthScreen, widthScreen, widthScreen, widthScreen};
		positionBottomPipeY = new int[]{ 10, 10, 10, 10};
		positionTopPipeY = new int[]{ 10, 10, 10, 10};

		collisionBird = new Circle(positionBirdX+birdRadius, positionBirdY+birdRadius, birdRadius);
		collisionPipeDown= new Rectangle[]{new Rectangle(positionPipeX[0],positionTopPipeY[0], pipeDownWidth, pipeDownHeight),new Rectangle(positionPipeX[1],positionTopPipeY[1], pipeDownWidth, pipeDownHeight),new Rectangle(positionPipeX[3],positionTopPipeY[2], pipeDownWidth, pipeDownHeight),new Rectangle(positionPipeX[3],positionTopPipeY[3], pipeDownWidth, pipeDownHeight)};
		collisionPipeUp=new Rectangle[]{new Rectangle(positionPipeX[0], positionBottomPipeY[0],pipeUpWidth,pipeUpHeight),new Rectangle(positionPipeX[1], positionBottomPipeY[1],pipeUpWidth,pipeUpHeight),new Rectangle(positionPipeX[2], positionBottomPipeY[2],pipeUpWidth,pipeUpHeight),new Rectangle(positionPipeX[3], positionBottomPipeY[3],pipeUpWidth,pipeUpHeight)};

		timeCountBirdWingsUpdate=0;
		timeCountBirdFallUpdate=0;
		timeCountUpdatePipes=0;
		pipesUpdateRate=pPipesUpdateRate;
		distanceBetweenPipes=pDistanceBetweenPipes;
		gapBetweenTopBottomPipe=pGapBetweenTopBottomPipe;
		birdFallRate=30;

		for (int i=0;i<positionPipeX.length;i++) {
			currentTubeCount=i;
			resetPipe();
		}
		currentTubeCount=0;

		flagDrawPipes=new boolean[]{false,false,false,false};
		firstTime=true;
		hasentLose=true;
		playGame=true;
	}

	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
