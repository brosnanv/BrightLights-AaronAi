package group_one.brightlights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class instructPg extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_pg);
        Button Instruct1;
        Instruct1 =(Button)findViewById(R.id.instruct1);
        Instruct1.setOnClickListener(this);
    }

    public void onClick (View v) {
        Intent intent = new Intent(this, InstructPg2.class);
        startActivity(intent);

    }


}


