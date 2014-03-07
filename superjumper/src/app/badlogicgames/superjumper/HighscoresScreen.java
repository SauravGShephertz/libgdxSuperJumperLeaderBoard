/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package app.badlogicgames.superjumper;

import shephertz.App42Handler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.shephertz.app42.paas.sdk.java.App42CallBack;
import com.shephertz.app42.paas.sdk.java.game.Game.Score;





public class HighscoresScreen implements Screen, App42CallBack{
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle backBounds;
	Vector3 touchPoint;
	float xOffset = 0;
	int score;
	String notification;
	java.util.ArrayList<Score> scoreList;

	public HighscoresScreen (Game game, int score) {
		this.game = game;
		this.score = score;
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		backBounds = new Rectangle(0, 0, 64, 64);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		
		App42Handler.initialize();
		
		if(score>0){
			System.out.println("Util.NAME: "+Util.NAME);
			App42Handler.instance().submitScore(Util.NAME, score, this);
		}else{
			App42Handler.instance().getLeaderBoard(this);
		}
		notification = "Please Wait...";
	}

	public void update () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (backBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}

	public void draw () {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();

		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();
		batcher.draw(Assets.highScoresRegion, 10, 450 - 16, 300, 33);
		Assets.font.draw(batcher, notification, 50, 200);

		float y = 400;
		if(scoreList!=null && scoreList.size()>0){
			for (int i = 0; i < scoreList.size(); i++) {
				Score s = scoreList.get(i);
				if(s.getUserName().length()>15){
					Assets.font.draw(batcher, (i+1)+"  "+s.getUserName().substring(0, 14)+".", 10, y);
				}else{
					Assets.font.draw(batcher, (i+1)+"  "+s.getUserName(), 10, y);
				}
				int score= s.getValue().intValue();
				Assets.font.draw(batcher, score+"", 250, y);
				y -= Assets.font.getLineHeight();
			}
		}
		batcher.draw(Assets.arrow, 0, 0, 64, 64);
		batcher.end();
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}

	@Override
	public void onException (Exception e) {
		System.out.println(">>>>>>>>>>>>>>>"+e.getMessage());
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess (Object object) {
		com.shephertz.app42.paas.sdk.java.game.Game game = (com.shephertz.app42.paas.sdk.java.game.Game)object;
		if(score>0){
			App42Handler.instance().getLeaderBoard(this);
			score=0;
			return;
		}
		scoreList = game.getScoreList();
		if(scoreList.size()>0){
			notification = "Success";
		}else{
			notification = "List Empty";
		}
		notification = "";
	}
	
}
