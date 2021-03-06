package org.tripledip.diana.ui.startup;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import org.tripledip.diana.service.GameService;
import org.tripledip.diana.service.SocketListener;
import org.tripledip.diana.ui.demo.ColorButtonActivity;
import org.tripledip.diana.ui.game.crew.CrewActivity;
import org.tripledip.rubberchicken.R;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Ben on 4/11/15.
 * <p/>
 * This fragment exposes the lifecycle of the GameService from a client point of view.
 * <p/>
 * It let's the user pick an IP and port for connecting to the server and lets the user try to
 * connect to the server.
 * <p/>
 * It starts the game when connection is successful.  It lets the user quit the game.  It also lets
 * the user stop the GameService if they want.
 * <p/>
 * The activity that attaches this fragment needs to pass in a GameService that this fragment can
 * work with.
 */
public class ClientConnectionFragment extends Fragment {

    private Button goButton;
    private NumberPicker ipPicker1;
    private NumberPicker ipPicker2;
    private NumberPicker ipPicker3;
    private NumberPicker ipPicker4;
    private NumberPicker portPicker;

    private GameService gameService;

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public ClientConnectionFragment() {
        // Required empty public constructor
    }

    public static ClientConnectionFragment newInstance() {
        return new ClientConnectionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_connect_client, container, false);

        goButton = (Button) rootView.findViewById(R.id.start_button);
        goButton.setOnClickListener(new GoButtonListener());

        ipPicker1 = (NumberPicker) rootView.findViewById(R.id.ip_picker_1);
        ipPicker1.setMaxValue(255);
        ipPicker1.setMinValue(0);
        ipPicker2 = (NumberPicker) rootView.findViewById(R.id.ip_picker_2);
        ipPicker2.setMaxValue(255);
        ipPicker2.setMinValue(0);
        ipPicker3 = (NumberPicker) rootView.findViewById(R.id.ip_picker_3);
        ipPicker3.setMaxValue(255);
        ipPicker3.setMinValue(0);
        ipPicker4 = (NumberPicker) rootView.findViewById(R.id.ip_picker_4);
        ipPicker4.setMaxValue(255);
        ipPicker4.setMinValue(0);
        portPicker = (NumberPicker) rootView.findViewById(R.id.port_picker);
        portPicker.setMaxValue(65535);
        portPicker.setMinValue(0);

        if (null == savedInstanceState) {
            addressToPickers(getWifiAddress(), ServerConnectionFragment.DEFAULT_PORT);
        }

        return rootView;
    }

    private String getWifiAddress() {
        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        String ipString = String.format("%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return ipString;
    }

    private void addressToPickers(String ip, int port) {
        String[] ipParts = ip.split("\\.");
        ipPicker1.setValue(Integer.parseInt(ipParts[0]));
        ipPicker2.setValue(Integer.parseInt(ipParts[1]));
        ipPicker3.setValue(Integer.parseInt(ipParts[2]));
        ipPicker4.setValue(Integer.parseInt(ipParts[3]));
        portPicker.setValue(port);
    }

    private SocketAddress pickersToAddress() {
        StringBuilder ipBuilder = new StringBuilder();
        ipBuilder.append(ipPicker1.getValue());
        ipBuilder.append(".");
        ipBuilder.append(ipPicker2.getValue());
        ipBuilder.append(".");
        ipBuilder.append(ipPicker3.getValue());
        ipBuilder.append(".");
        ipBuilder.append(ipPicker4.getValue());

        String ip = ipBuilder.toString();
        int port = portPicker.getValue();

        return new InetSocketAddress(ip, port);
    }

    private void reset() {
        gameService.reset();
        goButton.setText(R.string.text_connect_to_server);
    }

    private void connect() {
        gameService.makeDipClient(pickersToAddress(), new SocketConnectedListener());
        goButton.setText(R.string.text_reset);
    }

    private void launchGame() {
        Log.i(ClientConnectionFragment.class.getName(), "launching game");
        startActivity(new Intent(getActivity(), CrewActivity.class));
    }

    private class GoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (gameService.isConnecting()) {
                reset();
            } else {
                connect();
            }
        }
    }

    private class SocketConnectedListener implements SocketListener {
        @Override
        public void onSocketConnected(Socket socket) {
            if (null == socket) {
                reset();
                return;
            }
            launchGame();
        }
    }
}
