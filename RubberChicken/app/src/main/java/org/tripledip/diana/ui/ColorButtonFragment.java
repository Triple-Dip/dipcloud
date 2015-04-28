package org.tripledip.diana.ui;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.rubberchicken.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorButtonFragment extends Fragment implements ScrudListener<Molecule>, View.OnClickListener {

    public static final String LEFT_COLOUR = "leftColor";
    public static final String RIGHT_COLOUR = "rightColor";
    public static final String COLOUR_CHANNEL = "colourChannel";

    private String name;
    private int colorId;
    private DipAccess dipAccess;

    private TextView nameText;
    private View leftContainer;
    private View rightContainer;

    public ColorButtonFragment() {
        // Required empty public constructor
    }

    public static ColorButtonFragment newInstance(String name, int colorId, DipAccess dipAccess) {

        ColorButtonFragment colorButtonFragment = new ColorButtonFragment();

        colorButtonFragment.setName(name);
        colorButtonFragment.setColorId(colorId);
        colorButtonFragment.setDipAccess(dipAccess);

        return colorButtonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_color_button, container, false);
        nameText = (TextView) rootView.findViewById(R.id.colorFragName);
        leftContainer = rootView.findViewById(R.id.leftColor);
        rightContainer = rootView.findViewById(R.id.rightColor);

        nameText.setText(name);
        nameText.setOnClickListener(this);

        setNameContainerColor(colorId);
        setLeftContainerColor(Color.DKGRAY);
        setRightContainerColor(Color.DKGRAY);

        return rootView;
    }

    public void setNameContainerColor(int colorId) {
        nameText.getBackground().setColorFilter(colorId, PorterDuff.Mode.SRC_ATOP);
    }

    public void setLeftContainerColor(int colorId) {
        leftContainer.getBackground().setColorFilter(colorId, PorterDuff.Mode.SRC_ATOP);
    }

    public void setRightContainerColor(int colorId) {
        rightContainer.getBackground().setColorFilter(colorId, PorterDuff.Mode.SRC_ATOP);
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

    public DipAccess getDipAccess() {
        return dipAccess;
    }

    public void setDipAccess(DipAccess dipAccess) {
        this.dipAccess = dipAccess;
        if (null != dipAccess) {
            dipAccess.getChannelListeners().registerListener(COLOUR_CHANNEL, this);
        }
    }


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

    private void setUiColorsFromMolecule(final Molecule molecule) {

        final Runnable updateUi = new Runnable() {
            @Override
            public void run() {
                Atom leftAtom = molecule.findById(LEFT_COLOUR);
                Atom rightAtom = molecule.findById(RIGHT_COLOUR);

                if (null != leftAtom) {
                    setLeftContainerColor(leftAtom.getIntData());
                }

                if (null != rightAtom) {
                    setRightContainerColor(rightAtom.getIntData());
                }
            }
        };

        getActivity().runOnUiThread(updateUi);

    }

    @Override
    public void onClick(View v) {
        setRightContainerColor(Color.DKGRAY);
        setLeftContainerColor(Color.DKGRAY);

        long sequenceNumber = dipAccess.getNimbase().nextSequenceNumber();
        Molecule molecule = new Molecule(COLOUR_CHANNEL,
                new Atom(LEFT_COLOUR, sequenceNumber, colorId),
                new Atom(RIGHT_COLOUR, sequenceNumber, colorId));
        dipAccess.proposeUpdate(molecule);
    }
}
