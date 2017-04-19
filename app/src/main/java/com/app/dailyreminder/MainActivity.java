package com.app.dailyreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private String STATE_DR;
    public static String TEXT_VALUE = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textViewDR);
        mButton = (Button) findViewById(R.id.mainButton);
        getSupportActionBar().hide();


        try {
            fetchQotD(mTextView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Calendar calendarDaily = Calendar.getInstance();
        calendarDaily.set(Calendar.HOUR_OF_DAY, 9);
        calendarDaily.set(Calendar.MINUTE, 0);
        calendarDaily.set(Calendar.SECOND, 0);

        Intent intentDaily = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntentDaily = PendingIntent.getBroadcast(MainActivity.this, 0, intentDaily, 0);
        AlarmManager daily = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        daily.setRepeating(AlarmManager.RTC_WAKEUP, calendarDaily.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentDaily);
    }

    //Code to save state on orientation change
    @Override
    public void onSaveInstanceState(Bundle outState) {
        mTextView = (TextView) findViewById(R.id.textViewDR);
        outState.putString(STATE_DR, mTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTextView = (TextView) findViewById(R.id.textViewDR);
        mTextView.setText(STATE_DR);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private AssetManager getApplicationAssets() {
        // open random quotes file
        AssetManager assetmanager = getAssets();
        return assetmanager;
    }

    // Get the path for the random quote file
    private InputStreamReader getQuoteReader() throws IOException {
        // open random quotes file
        AssetManager assets = getApplicationAssets();
        String path = null;
        InputStream inputStream = null;

        try {
            inputStream = assets.open("ReminderOfTheDay.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader textReader = new InputStreamReader(inputStream);
        return textReader;
    }

    // Get the total number of lines in the file
    private int getFileLineCount(InputStreamReader textReader) {
        BufferedReader br = new BufferedReader(textReader);
        int lineCount = 0;
        try {
            while ((br.readLine()) != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineCount; // total number of lines in the text file
    }

    // Return a random line number from where to get the
    // corresponding quote string
    private int getRandomLineNumber(int totalLines) {
        Random rand = new Random();
        return rand.nextInt(totalLines);
    }

    private String getRandomQuote(int lineToFetch)
            throws IOException {
        //1. get path
        AssetManager assets = getApplicationAssets();
        String path = null;

        //2. open assets
        InputStream stream = assets.open("ReminderOfTheDay.txt");
        InputStreamReader randomQuote = new InputStreamReader(stream);

        //3. Get BufferedReader object
        BufferedReader buf = new BufferedReader(randomQuote);

        String quote = null;
        String line = null;
        int currLine = 0;

        //4. Loop through using the new InputStreamReader until a match is found
        while ((line = buf.readLine()) != null && currLine < lineToFetch) {
            currLine++;
        }

        //Got the quote
        quote = line;

        //Clean up
        randomQuote.close();
        buf.close();

        return quote;
    }

    // Set the EditText widget to display the new random quote
    private void displayQuote(String quote) {
        TextView quoteDisplay = (TextView) findViewById(R.id.textViewDR);
        TEXT_VALUE = quote;
        quoteDisplay.setText(TEXT_VALUE);
    }

    // onClick handler for the button click
    public void fetchQotD(View view) throws IOException {
        // open random quotes file
        InputStreamReader textReader = getQuoteReader();

        final int totalLines = getFileLineCount(textReader);
        int lineToFetch = 0;
        String quote = null;

        // We want to get the quote at the following line number
        lineToFetch = getRandomLineNumber(totalLines);

        quote = getRandomQuote(lineToFetch);

        displayQuote(quote);
    }
}
