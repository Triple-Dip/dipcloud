package org.tripledip.diana.ui.demo;

import android.graphics.Color;

import org.tripledip.diana.ui.game.GameActivity;
import org.tripledip.rubberchicken.R;

import java.util.Random;

/**
 * Created by Ben on 5/2/15.
 */
public class ColorButtonActivity extends GameActivity {

    ColorButtonFragment colorButtonFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    protected void findOrAttachFragments() {
        colorButtonFragment = (ColorButtonFragment)
                getFragmentManager().findFragmentById(R.id.game_frame);

        if (null != colorButtonFragment) {
            return;
        }

        final String name = getString(R.string.app_name);
        final int color = randomColor();
        colorButtonFragment = ColorButtonFragment.newInstance(name, color, gameService);
        getFragmentManager().beginTransaction()
                .add(R.id.game_frame, colorButtonFragment)
                .commit();
    }

    @Override
    protected void registerListeners() {
        colorButtonFragment.registerListeners();
    }

    @Override
    protected void unregisterListeners() {
        colorButtonFragment.unregisterListeners();
    }

    private int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

}
