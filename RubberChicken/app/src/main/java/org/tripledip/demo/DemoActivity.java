package org.tripledip.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.behavior.SuperDip;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.rubberchicken.R;

public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        SuperDip superDip = new SuperDip(new Nimbase());
        Molecule molecule = new Molecule(DemoFragment.COLOUR_CHANNEL,
                new Atom(DemoFragment.LEFT_COLOUR, 0, Color.GREEN),
                new Atom(DemoFragment.RIGHT_COLOUR, 0, Color.GREEN));
        superDip.proposeAdd(molecule);

        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .add(R.id.gridColours, DemoFragment.newInstance("Butts", Color.BLUE, superDip))
                    .commit();
        }
    }

}
