package org.tripledip.diana.ui.demo;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tripledip.diana.service.GameService;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.rubberchicken.R;

/**
 * Our original demo for testing out the dip.  Send people your color!
 */
public class ColorButtonFragment extends Fragment {

    public static final String LEFT_COLOUR = "leftColor";
    public static final String RIGHT_COLOUR = "rightColor";
    public static final String COLOUR_CHANNEL = "colourChannel";

    private String name;
    private int colorId;
    private GameService gameService;

    private TextView nameText;
    private View leftContainer;
    private View rightContainer;

    public ColorButtonFragment() {
        // Required empty public constructor
    }

    public static ColorButtonFragment newInstance(String name, int colorId, GameService gameService) {
        ColorButtonFragment colorButtonFragment = new ColorButtonFragment();
        colorButtonFragment.setName(name);
        colorButtonFragment.setColorId(colorId);
        colorButtonFragment.setGameService(gameService);
        return colorButtonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color_button, container, false);

        nameText = (TextView) rootView.findViewById(R.id.colorFragName);
        leftContainer = rootView.findViewById(R.id.leftColor);
        rightContainer = rootView.findViewById(R.id.rightColor);

        nameText.setText(name);
        nameText.setOnClickListener(onClickListener);

        setViewContainerColor(nameText, colorId);
        setViewContainerColor(leftContainer, Color.DKGRAY);
        setViewContainerColor(rightContainer, Color.DKGRAY);

        return rootView;
    }

    public GameService getGameService() {
        return gameService;
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
        bootstrapAtoms();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    private void bootstrapAtoms() {
        Molecule bootstrap = makeMolecule(Color.DKGRAY);
        for (Atom a : bootstrap) {
            gameService.getDipAccess().getNimbase().add(a);
        }
    }

    protected void registerListeners() {
        gameService.getDipAccess()
                .getChannelListeners()
                .registerListener(COLOUR_CHANNEL, moleculeScrudListener);
    }

    protected void unregisterListeners() {
        gameService.getDipAccess()
                .getChannelListeners()
                .unregisterListener(COLOUR_CHANNEL, moleculeScrudListener);
    }

    private void setViewContainerColor(View view, int colorId) {
        view.getBackground().setColorFilter(colorId, PorterDuff.Mode.SRC_ATOP);
    }

    private Molecule makeMolecule(int colorId) {
        long sequenceNumber = gameService.getDipAccess().getNimbase().nextSequenceNumber();
        return new Molecule(COLOUR_CHANNEL,
                new Atom(LEFT_COLOUR, sequenceNumber, colorId),
                new Atom(RIGHT_COLOUR, sequenceNumber, colorId));
    }

    private void setUiColorsFromMolecule(final Molecule molecule) {

        final Runnable updateUi = new Runnable() {
            @Override
            public void run() {
                Atom leftAtom = molecule.findById(LEFT_COLOUR);
                Atom rightAtom = molecule.findById(RIGHT_COLOUR);

                if (null != leftAtom) {
                    setViewContainerColor(leftContainer, leftAtom.getIntData());
                }

                if (null != rightAtom) {
                    setViewContainerColor(rightContainer, leftAtom.getIntData());
                }
                Log.i(ColorButtonFragment.class.getName(), "update");
            }
        };

        getActivity().runOnUiThread(updateUi);
    }

    private ScrudListener<Molecule> moleculeScrudListener = new ScrudListener<Molecule>() {
        @Override
        public void onAdded(Molecule thing) {
            setUiColorsFromMolecule(thing);
        }

        @Override
        public void onUpdated(Molecule thing) {
            setUiColorsFromMolecule(thing);
        }

        @Override
        public void onRemoved(Molecule thing) {

        }

        @Override
        public void onSent(Molecule thing) {

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(ColorButtonFragment.class.getName(), "click");
            setViewContainerColor(rightContainer, Color.DKGRAY);
            setViewContainerColor(leftContainer, Color.DKGRAY);
            gameService.getDipAccess().proposeUpdate(makeMolecule(colorId));
        }
    };

}
