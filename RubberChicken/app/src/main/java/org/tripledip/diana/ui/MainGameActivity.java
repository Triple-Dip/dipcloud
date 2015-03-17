package org.tripledip.diana.ui;

import android.app.Activity;
import android.os.Bundle;

import org.tripledip.rubberchicken.R;

public class MainGameActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game_flexible);

        //TODO: attach fragments and stuff.
        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .add(R.id.top_right_container, new StatusReportFragment())
                    .commit();
        }
    }

}
