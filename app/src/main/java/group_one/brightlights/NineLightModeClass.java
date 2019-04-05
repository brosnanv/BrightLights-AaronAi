package group_one.brightlights;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class NineLightModeClass extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private TextView textViewPlayer1;
    private int[][] color = new int[3][3];
    private int[][] clicked = new int[3][3];
    private String gameId ="k6986774";

    private SoundPool soundPool;
    private int lightSwitch ;

    private static final String TAG = "ColorCoding";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase.getInstance().getReference().child("games")
                .child(gameId)
                .setValue(null);

        FirebaseDatabase.getInstance().getReference().child("games")
                .child(gameId)
                .child("RESET")
                .setValue(System.currentTimeMillis());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.nine_light_mode);
        textViewPlayer1 = findViewById(R.id.text1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                setarray(i, j);
                setboardcolor(i, j);
            }
        }
        setlevel();
        Button reset;
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        Button ss;
        ss = (Button) findViewById(R.id.ss);
        ss.setOnClickListener(this);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                clicked[i][j] = 0;
            }
        }
        clicked[1][1] = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes= new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes)
                    .build();
        }else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        lightSwitch = soundPool.load(this,R.raw.light,1);


    }

    @Override
    public void onClick(View v) {

        playSound(v);
        //if (((Button) v).getText().toString().equals("x")){
        switch (v.getId()) {
            case R.id.button_00:
                swap(0, 0);
                break;
            case R.id.button_01:
                swap(0, 1);
                break;
            case R.id.button_02:
                swap(0, 2);
                break;
            case R.id.button_10:
                swap(1, 0);
                break;
            case R.id.button_12:
                swap(1, 2);
                break;
            case R.id.button_11:
                swap(1, 1);
                break;
            case R.id.button_20:
                swap(2, 0);
                break;
            case R.id.button_21:
                swap(2, 1);
                break;
            case R.id.button_22:
                swap(2, 2);
                break;
            case R.id.reset:
                resetarray();
            case R.id.ss:
                if(!checkforwin()) {
                    int move = do_move();

                    int x = move / 3;
                    int y = move - (x * 3);
                    swap(x, y);
                    break;
                }
        }

    }

    private void setboardcolor(int i, int j) {
        // Random rand = new Random();
        // color[i][j]= rand.nextInt(2) + 0;
        if (color[i][j] == 1) {
            buttons[i][j].setBackgroundColor(Color.parseColor("red"));
            buttons[i][j].setTextColor(Color.parseColor("red"));
            buttons[i][j].setText("x");
            FirebaseDatabase.getInstance().getReference().child("games")
                    .child(gameId)
                    .child(i + "_" + j)
                    .setValue("1");

        } else {
            buttons[i][j].setBackgroundColor(Color.parseColor("#a4c639"));
            buttons[i][j].setTextColor(Color.parseColor("#a4c639"));
            buttons[i][j].setText("0");
            FirebaseDatabase.getInstance().getReference().child("games")
                    .child(gameId)
                    .child(i + "_" + j)
                    .setValue("0");
        }

    }

    private void setarray(int i, int j) {
//        Random rand = new Random();
        //color[i][j]= rand.nextInt(2) + 0;
        color[i][j] = 1;
        setboardcolor(i, j);
    }

    private void resetarray() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                setarray(i, j);
                FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId)
                        .child(i + "_" + j)
                        .setValue("0");
            }
        }


        setlevel();
    }

    private void swap(int i, int j) {
        if (i - 1 >= 0) {
            if (color[i - 1][j] == 1) {
                color[i - 1][j] = 0;
            } else {
                color[i - 1][j] = 1;
            }
        }
        if (i + 1 <= 2) {
            if (color[i + 1][j] == 1) {
                color[i + 1][j] = 0;
            } else {
                color[i + 1][j] = 1;
            }
        }
        if (j - 1 >= 0) {
            if (color[i][j - 1] == 1) {
                color[i][j - 1] = 0;
            } else {
                color[i][j - 1] = 1;
            }
        }
        if (j + 1 <= 2) {
            if (color[i][j + 1] == 1) {
                color[i][j + 1] = 0;
            } else {
                color[i][j + 1] = 1;
            }
        }
        if (color[i][j] == 1) {
            color[i][j] = 0;
        } else {
            color[i][j] = 1;
        }

        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {

                setboardcolor(k, l);
            }
        }

    }

    private static int Reduction_steps[][]= {
            {2,	1,	0}, //1
            {4,	1,	0}, //2
            {3,	2,	1}, //3
            {4,	2,	0}, //4
            {5,	2,	0}, //5
            {4,	3,	0}, //6
            {5,	3,	0}, //7
            {6,	3,	0}, //8
            {6,	4,	0}, //9
            {7,	4,	0}, //10
            {5,	8,	1}, //11
            {6,	7,	1}, //12
            {9,	6,	0}, //13
            {5,	9,	0}, //14
            {7,	9,	0}, //15
            {5,	8,	0}, //16
            {6,	8,	0}, //17
            {4,	7,	0}, //18
            {5,	7,	0}, //19
            {2,	6,	0}, //20
            {4,	6,	0}, //21
            {3,	5,	0}, //22
            {1,	4,	0}, //23
            {3,	4,	0}, //24
            {2,	3,	0}, //25
            {1,	2,	0}, //26
    };


    private int do_move(){

        int lights_up_on[] = new int[9];
        int lights_up_off[] = new int[9];
        int numb_move_lights_off = 0;
        int numb_move_lights_on = 0;
        int temp1,temp2;

        //Initializing the lights on and off arrays
        //So it will calculate shortest distance both to fully off and fully on lights
        for(int x=0;x<3;x++) {
            for(int y=0;y<3;y++) {

                lights_up_on[(x*3)+y] = color[x][y];

                if(color[x][y] == 0){
                    lights_up_off[(x*3)+y] = 1;
                }else{
                    lights_up_off[(x*3)+y] = 0;
                }
            }
        }

        for(int y = 0; y < 26 ; y++)
        {
            if(Reduction_steps[y][2] == 0)
            {
                //Lights on
                lights_up_on[(Reduction_steps[y][0] - 1)] = lights_up_on[(Reduction_steps[y][0] - 1)] - lights_up_on[(Reduction_steps[y][1] - 1)];
                if(lights_up_on[(Reduction_steps[y][0] - 1)] == -1) {
                    lights_up_on[(Reduction_steps[y][0] - 1)] = 1;
                }

                //Lights off
                lights_up_off[(Reduction_steps[y][0] - 1)] = lights_up_off[(Reduction_steps[y][0] - 1)] - lights_up_off[(Reduction_steps[y][1] - 1)];
                if(lights_up_off[(Reduction_steps[y][0] - 1)] == -1) {
                    lights_up_off[(Reduction_steps[y][0] - 1)] = 1;
                }

            }
            else
            {
                //Lights on
                temp1 = lights_up_on[(Reduction_steps[y][0] - 1)];
                lights_up_on[(Reduction_steps[y][0] - 1)] = lights_up_on[(Reduction_steps[y][1] - 1)];
                lights_up_on[(Reduction_steps[y][1] - 1)] = temp1;

                //Light off
                temp2 = lights_up_off[(Reduction_steps[y][0] - 1)];
                lights_up_off[(Reduction_steps[y][0] - 1)] = lights_up_off[(Reduction_steps[y][1] - 1)];
                lights_up_off[(Reduction_steps[y][1] - 1)] = temp2;

            }
        }


        //Checking which path shorter
        for(int x=0;x<9;x++) {
            if(lights_up_on[x] == 1) {
                numb_move_lights_on++;
            }
            if(lights_up_off[x] == 1) {
                numb_move_lights_off++;
            }
        }

        //If light off takes less amount of moves it will do that if not it will go with lights on
        //Lights off is default if the amount of moves is equal
        for(int x=0;x<9;x++) {
            if(lights_up_on[x] == 1 && numb_move_lights_on < numb_move_lights_off) {
                return x;
            }
            if(lights_up_off[x] == 1 && numb_move_lights_on >= numb_move_lights_off) {
                return x;
            }
        }
        //Returns a one if an error has occurred
        return -1;
    }

    private boolean checkforwin() {
        int flag;
        flag = color[0][0];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (flag != color[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setlevel() {
        int k = (int) getIntent().getIntExtra("level", 0);
        //int k=Integer.parseInt(s);
        Random rand = new Random();
        for (int l = 0; l < k; l++) {
            int i = rand.nextInt(3);
            int j = rand.nextInt(3);
            swap(i, j);
        }

    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
        Log.d(TAG, "setGameId: " + gameId);
        FirebaseDatabase.getInstance().getReference().child("games")
                .child(gameId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
                            return;
                        }
                        String key = dataSnapshot.getKey();
                        if (!key.equals("RESET")) {
                            int row = Integer.parseInt(key.substring(0, 1));
                            int col = Integer.parseInt(key.substring(2, 3));
                            Integer shape = dataSnapshot.getValue(Integer.class);


                        }
                    }


                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getValue() == null) {
                            return;
                        }
                        if (dataSnapshot.getKey().equals("RESET")) {
                            resetarray();

                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


    public void playSound (View v) {
        soundPool.play(lightSwitch,1,1,0,0,1);


    }

    protected void onDestroy(){

        super.onDestroy();;
        soundPool.release();
        soundPool=null ;
    }


}