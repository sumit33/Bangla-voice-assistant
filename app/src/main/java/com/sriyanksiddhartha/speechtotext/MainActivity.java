package com.sriyanksiddhartha.speechtotext;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import android.text.format.DateUtils;
import java.util.Date;
import android.net.Uri;


public class MainActivity extends AppCompatActivity {

	private TextView txvResult;
	private TextToSpeech tts;
	private Locale locale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txvResult = (TextView) findViewById(R.id.txvResult);

		initializeTextToSpeech();
	}

	public void getSpeechInput(View view) {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "bn");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn");

		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(intent, 10);
		} else {
			Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
		}
	}

	private void initializeTextToSpeech() {
	    locale=new Locale("bn","BD");
		tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (tts.getEngines().size() == 0 ){
					Toast.makeText(MainActivity.this, "Missing.........",Toast.LENGTH_LONG).show();
					finish();
				} else {
					tts.setLanguage(locale);
					speak("আপনাকে অভিনন্দন");
				}
			}
		});
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case 10:
				if (resultCode == RESULT_OK && data != null) {
					ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					txvResult.setText(result.get(0));

					processResult(result.get(0));

				}
				break;
		}
	}

	private void processResult(String result_message) {
		result_message = result_message.toLowerCase();

//        Handle at least four sample cases

//        First: What is your Name?
//        Second: What is the time?
//        Third: Is the earth flat or a sphere?
//        Fourth: Open a browser and open url
		if(result_message.indexOf("কি") != -1){
			if(result_message.indexOf("তোমার নাম") != -1){
				speak("আমার নাম সব্যসাচী ");
			}
//			if (result_message.indexOf("সময়") != -1){
//				String time_now = DateUtils.formatDateTime(this, new Date().getTime(),DateUtils.FORMAT_SHOW_TIME);
//				speak("The time is now: " + time_now);
//			}
		} else if (result_message.indexOf("সময়") != -1){
            String time_now = DateUtils.formatDateTime(this, new Date().getTime(),DateUtils.FORMAT_SHOW_TIME);
            speak("এখন সময় " + time_now);
        }
		else if (result_message.indexOf("earth") != -1){
			speak("Don't be silly, The earth is a sphere. As are all other planets and celestial bodies");
		} else if (result_message.indexOf("ব্রাউজার") != -1){
			speak("ওপেন করা হচ্ছে");
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/AnNJPf-4T70"));
			startActivity(intent);
		}
		else if (result_message.indexOf(" ") != -1){
			speak("সার্চ করা হচ্ছে");
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pipilika.com/search?q="+ result_message));
			startActivity(intent);
		}
	}

	private void speak(String message) {
		if(Build.VERSION.SDK_INT >= 21){

			tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
		} else {
			tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
		}
	}


}
