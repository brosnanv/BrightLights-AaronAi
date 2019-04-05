package group_one.brightlights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class instructPg3 extends AppCompatActivity implements View.OnClickListener {

        private Button home;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.instructpg3);

        home =(Button) findViewById(R.id.home);
        home.setOnClickListener(this);


    }

       public void onClick(View v)
        {
            Intent intent = new Intent(this, LevelMenuClass.class);
            startActivity(intent);

        }


    }