package br.com.intelligence;

import generic.ScreenResolution;
import br.com.intelligence.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class Sobre extends Activity implements ScreenResolution {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pegaScreenResolution();
		setContentView(R.layout.activity_sobre);
	}

	@Override
	public void pegaScreenResolution() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		Log.i("Script", " Tela do dispositivo - " + height + "x" + width);
		// pegar - largura por altura
		if (width <= 780 && height <= 1080) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}

	// metodo sair...
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
