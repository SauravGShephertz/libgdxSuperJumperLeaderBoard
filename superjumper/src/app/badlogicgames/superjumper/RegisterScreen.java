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

import java.util.Random;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class RegisterScreen implements Screen {
	
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	
	Rectangle enterNameBound;
	Rectangle enterEmailBound;
	Rectangle enterPassBound;
	Rectangle submitBound;
	
	
	Vector3 touchPoint;
	
	
	

	private String  notification = "";
	
		
	public RegisterScreen (Game game) {
		this.game = game;

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		batcher = new SpriteBatch();
		enterNameBound = new Rectangle(10, 400 , 128, 32);
		enterEmailBound = new Rectangle(10, 350, 128, 32);
		enterPassBound = new Rectangle(10, 300, 128, 32);
		submitBound = new Rectangle(160-64, 100, 128, 32);
		
		touchPoint = new Vector3();
		readPref();
		
	}
	
	public static boolean isUserRegistered(){
		readPref();
		if(Util.NAME!=null){
			return true;
		}else{
			return false;
		}
	}
	
	private static void readPref(){
		Preferences prefs = Gdx.app.getPreferences("my-preferences");
		Util.NAME = prefs.getString("name");
		Util.EMAIL = prefs.getString("email");
		Util.PASSWORD = prefs.getString("password");
	}
	
	private void savePref(){
		Preferences prefs = Gdx.app.getPreferences("my-preferences");
		if(Util.NAME.length()>0 && Util.EMAIL.length()>0 && Util.PASSWORD.length()>0){
			prefs.putString("name", Util.NAME);
			prefs.putString("email", Util.EMAIL);
			prefs.putString("password", Util.PASSWORD);
			prefs.flush();
			notification = "Saved Successfully";
			game.setScreen(new MainMenuScreen(game));
		}else{
			notification = "All Fields are mandatory";
		}
	}
	
	public void update () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (enterNameBound.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Gdx.input.getTextInput(new NameInputListener(), "Enter Name*", Util.NAME);
				return;
			}
			if (enterEmailBound.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Gdx.input.getTextInput(new EmailInputListener(), "Enter Email*", Util.EMAIL);
				return;
			}
			if (enterPassBound.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Gdx.input.getTextInput(new PasswordInputListener(), "Enter Password*", Util.PASSWORD);
				return;
			}
			if (submitBound.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				savePref();
				return;
			}
		}
	}

	long last = System.nanoTime();
	public void draw () {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);

		batcher.disableBlending();
		batcher.begin();
		batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();
		
		batcher.draw(Assets.enterName, 10, 400 , 128, 32);
		Assets.font.draw(batcher, ": "+Util.NAME, 140, 400+Assets.font.getLineHeight());
		batcher.draw(Assets.enterEmail, 10, 350, 128, 32);
		Assets.font.draw(batcher, ": "+Util.EMAIL, 140, 350+Assets.font.getLineHeight());
		batcher.draw(Assets.enterPass, 10, 300, 128, 32);
		Assets.font.draw(batcher, ": "+Util.PASSWORD, 140, 300+Assets.font.getLineHeight());
		batcher.draw(Assets.submit, 160-64, 100, 128, 32);
		Assets.font.draw(batcher, notification, 10, 50);
		batcher.end();
		
		if(System.nanoTime() - last > 2000000000) {
			Gdx.app.log("SuperJumper", "version: " + Gdx.app.getVersion() + 
												", memory: " + Gdx.app.getJavaHeap() + ", " + Gdx.app.getNativeHeap() + 
												", native orientation:" + Gdx.input.getNativeOrientation() + 
												", orientation: " + Gdx.input.getRotation() + 
												", accel: " + (int)Gdx.input.getAccelerometerX() + ", " + (int)Gdx.input.getAccelerometerY() + ", " + (int)Gdx.input.getAccelerometerZ() +
												", apr: " + (int)Gdx.input.getAzimuth() + ", " + (int)Gdx.input.getPitch() + ", " + (int)Gdx.input.getRoll());
			last = System.nanoTime();
		}
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
		Settings.save();
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
	
	private String getRandomHexString(int numchars){
      Random r = new Random();
      StringBuffer sb = new StringBuffer();
      while(sb.length() < numchars){
          sb.append(Integer.toHexString(r.nextInt()));
      }
      return sb.toString().substring(0, numchars);
  }
	public class NameInputListener implements TextInputListener {
	   @Override
	   public void input (String text) {
	   	Util.NAME = text;
	   }

	   @Override
	   public void canceled () {
	   	
	   }
	}
	public class EmailInputListener implements TextInputListener {
	   @Override
	   public void input (String text) {
	   	if(text.indexOf('.')==-1 || text.indexOf('@')==-1){
	   		notification = "enter valid email id";
	   	}
	   	Util.EMAIL = text;
	   }
	   @Override
	   public void canceled () {
	   	
	   }
	}
	public class PasswordInputListener implements TextInputListener {
	   @Override
	   public void input (String text) {
	   	Util.PASSWORD = text;
	   }
	   @Override
	   public void canceled () {
	   	
	   }
	}
}
