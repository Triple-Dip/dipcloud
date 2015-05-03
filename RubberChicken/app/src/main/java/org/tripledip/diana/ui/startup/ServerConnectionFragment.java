package org.tripledip.diana.ui.startup;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.tripledip.diana.service.GameService;
import org.tripledip.diana.service.SocketListener;
import org.tripledip.diana.ui.demo.ColorButtonActivity;
import org.tripledip.diana.ui.game.GameFragment;
import org.tripledip.rubberchicken.R;

import java.net.Socket;

/**
 * Created by Ben on 4/11/15.
 * <p/>
 * This fragment exposes the lifecycle of the GameService from a server point of view.
 * <p/>
 * It displays the servers LAN IP and allows the user to select a port to listen on.
 * <p/>
 * It lets the user start listing for client connections and displays the client addresses as they
 * connect.  It lets the user stop listening for clients.
 * <p/>
 * It lets the user start and stop the game.
 * <p/>
 * The activity that attaches this fragment needs to pass in a GameService that this fragment can
 * work with.
 */
public class ServerConnectionFragment extends GameFragment {

    public static final int DEFAULT_PORT = 55555;

    private Button goButton;
    private EditText portPicker;
    private ArrayAdapter<String> clientAdapter;

    private GameService gameService;

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public ServerConnectionFragment() {
        // Required empty public constructor
    }

    public static ServerConnectionFragment newInstance() {
        return new ServerConnectionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_connect_server, container, false);

        TextView ipText = (TextView) rootView.findViewById(R.id.server_ip_text);
        ipText.setText(getWifiAddress());

        portPicker = (EditText) rootView.findViewById(R.id.port_picker);
        portPicker.setText(Integer.toString(DEFAULT_PORT));

        goButton = (Button) rootView.findViewById(R.id.start_button);
        goButton.setOnClickListener(new GoButtonListener());

        ListView clientList = (ListView) rootView.findViewById(R.id.client_list_view);
        clientAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        clientList.setAdapter(clientAdapter);

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

    private int getPortFromInput() {
        try {
            return Integer.parseInt(portPicker.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            portPicker.setText(Integer.toString(DEFAULT_PORT));
            return DEFAULT_PORT;
        }
    }

    private void clearMessages() {
        clientAdapter.clear();
    }

    private void addMessage(String message) {
        clientAdapter.add(message);
    }

    private void resetAndStartAccepting() {
        clearMessages();
        gameService.makeDipServer(getPortFromInput(), new ClientAcceptedListener());
        goButton.setText(R.string.text_start_game);
        addMessage(getString(R.string.text_accepting_clients));
    }

    private void stopAccepting() {
        gameService.activateDipServer();
        goButton.setText(R.string.text_accept_from_client);
        addMessage(getString(R.string.text_no_more_clients));
        launchGame();
    }

    private void launchGame() {
        Log.i(ServerConnectionFragment.class.getName(), "launching game");
        startActivity(new Intent(getActivity(), ColorButtonActivity.class));
    }

    private class GoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (gameService.isAccepting()) {
                stopAccepting();
            } else {
                resetAndStartAccepting();
            }
        }
    }

    private class ClientAcceptedListener implements SocketListener {
        @Override
        public void onSocketConnected(Socket socket) {
            final String prefix = getString(R.string.text_client_prefix);
            addMessage(prefix + socket.getInetAddress().toString());
        }
    }
}
