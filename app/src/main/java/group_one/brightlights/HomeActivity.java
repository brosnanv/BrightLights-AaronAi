package group_one.brightlights;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    TextView t;
    MediaPlayer background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        background = MediaPlayer.create(this, R.raw.background);
        background.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         //   t=(TextView) findViewById(R.id.my_name);
         //   Typeface myCustomFont= Typeface.createFromAsset(getAssets(),"fonts/ARCADE_R.TTF");
         // t.setTypeface(myCustomFont);
        background.stop();

    }

}
