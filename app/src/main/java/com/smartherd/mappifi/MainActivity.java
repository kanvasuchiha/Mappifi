package com.smartherd.mappifi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smartherd.mappifi.Model.CountryDataSource;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //THE CONSTANTS SHOULD BE CAPITALISED.
    //Here we need this number to be unique.
    private static final int SPEAK_REQUEST=10;

    TextView txt_value;
    Button btn_voice_intent;

    public static CountryDataSource countryDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_value= (TextView) findViewById(R.id.txtValue);
        btn_voice_intent= (Button) findViewById(R.id.btnVoiceIntent);

        btn_voice_intent.setOnClickListener(MainActivity.this);



        Hashtable<String, String> countriesAndMessages = new Hashtable<>();
        countriesAndMessages.put("India", "Welcome to your Dreamland");
        countriesAndMessages.put("France", "Welcome to France");
        countriesAndMessages.put("Brazil", "Welcome to Brazil");
        countriesAndMessages.put("Canada", "Welcome to Canada");
        countriesAndMessages.put("England", "Welcome to England");

        countryDataSource=new CountryDataSource(countriesAndMessages);

        //done to make sure that the device has speech recognition.
        PackageManager packageManager= this.getPackageManager();
        List<ResolveInfo> listOfInformation=packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(listOfInformation.size()>0)
        {
            Toast.makeText(MainActivity.this, "Your device does support speech recognition!!", Toast.LENGTH_SHORT).show();
            listenToTheUsersVoice();
        }
        else {
            Toast.makeText(MainActivity.this, "Your device does NOTTT support speech recognition!!", Toast.LENGTH_SHORT).show();
        }




    }

    //method to actually use speech recognition and let user actually talk to his/her phone.
    private void listenToTheUsersVoice()
    {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to me");
        //the user can talk in any language.
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //MAXIMUM RESULTS WE WANT TO GET FROM USER'S SPEECH.
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        startActivityForResult(voiceIntent, SPEAK_REQUEST);
    }




    //to get data from the speech recognition part.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //THIS SPEAK_REQUEST CONFIRMS THAT THE INTENT WE HAVE IS THE SPEECH RECOGNITION ONE ONLY.
        if(requestCode==SPEAK_REQUEST && resultCode==RESULT_OK)
        {
            //SAVING THE WORDS THAT USER SPEAKS TO THE ARRAYLIST.
            ArrayList<String> voiceWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //CONFIDENCE LEVEL TELLS HOW ACCURATE OUR WORDS ARE.
            float[] confidLevels = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);


            //TO PRINT THE SAID WORDS ONSCREEN
            /*int index=0;
            for (String userWord : voiceWords){

                if(confidLevels!=null && index<confidLevels.length)
                {
                    txt_value.setText(userWord + "-"+confidLevels[index]);
                }
            }*/


            String countryMatchedWithUserWord= countryDataSource.matchWithMinimumConfidenceLevelOfUserWords(voiceWords, confidLevels);

            //This lets us transition from one activity to the other. Here, we are transitioning from mainactivity to the mapsactivity class.
            Intent myMapActivity= new Intent(MainActivity.this, MapsActivity.class);

            //TO SEND DATA TO THE ACTIVITY
            //countryMatchedWithUserWord IS THE WORD GOING TO BE ASSOCIATED WITH DEFAULT COUNTRY.
            myMapActivity.putExtra(CountryDataSource.COUNTREY_KEY, countryMatchedWithUserWord);
            startActivity(myMapActivity);



        }
    }

    //will execute every time button is clicked.
    @Override
    public void onClick(View view) {
        listenToTheUsersVoice();
    }
}
