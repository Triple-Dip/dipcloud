package org.tripledip.diana.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.tripledip.rubberchicken.R;

public class OpeningActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        final Button captainButton = (Button) findViewById(R.id.captain_button);
        captainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpeningActivity.this, ServerConnectActivity.class));
            }
        });

        final Button crewButton = (Button) findViewById(R.id.crew_button);
        crewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpeningActivity.this, ClientConnectActivity.class));
            }
        });
    }
}
