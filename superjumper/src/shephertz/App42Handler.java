package shephertz;

import java.math.BigDecimal;

import com.shephertz.app42.paas.sdk.java.App42API;
import com.shephertz.app42.paas.sdk.java.App42CallBack;
import com.shephertz.app42.paas.sdk.java.App42Exception;
import com.shephertz.app42.paas.sdk.java.App42Log;
import com.shephertz.app42.paas.sdk.java.game.ScoreBoardService;
import com.shephertz.app42.paas.sdk.java.user.User;
import com.shephertz.app42.paas.sdk.java.user.UserService;


public class App42Handler {
	
	private static App42Handler app42Handler;
	
	private String API_KEY = "42c04922c093f02822d5a87f628bdf5cad546a295e62ab05c35ee038a2e2f7c4";
	private String SECRET_KEY = "aa7ab9b14cea8c7a2b6af683a07fb25df1505e4a763878057f5dba92b0ddedaa";
	private ScoreBoardService scoreBoardService;
	private final String GAME_NAME = "SuperJumper";
	
	private App42Handler(){
		App42API.initialize(API_KEY, SECRET_KEY);
		scoreBoardService = App42API.buildScoreBoardService();
	}
	public static void initialize(){
		app42Handler = new App42Handler();
		App42Log.setDebug(true);
	}
	
	public static App42Handler instance(){
		return app42Handler;
	}
	
	public void submitScore(String uName, int score, App42CallBack callBack){
		scoreBoardService.saveUserScore(GAME_NAME, uName, new BigDecimal(score), callBack);
	}
	
	public void getLeaderBoard(App42CallBack callBack){
		try {
			scoreBoardService.getTopNRankers(GAME_NAME, 10, callBack);
		} catch (App42Exception e) {
			e.printStackTrace();
		}catch (Exception e) {
			System.out.println("Exception"+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
