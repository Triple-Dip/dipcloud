package org.tripledip.diana.ui;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.tripledip.diana.service.SocketAcceptorTask;
import org.tripledip.diana.service.SocketListener;
import org.tripledip.rubberchicken.R;

import java.net.Socket;

/**
 * Created by Ben on 4/11/15.
 *
 * This fragment exposes the lifecycle of the GameService from a server point of view.
 *
 * It displays the servers LAN IP and allows the user to select a port to listen on.
 *
 * It lets the user start listing for client connections and displays the client addresses as they
 * connect.  It lets the user stop listening for clients.
 *
 * It lets the user start and stop the game.  It also lets the user stop GameService if they want.
 *
 * The activity that attaches this fragment needs to help.  It needs to pass in a GameService that
 * this fragment can work with.  It needs to pass in a listener that this fragment can call back to
 * when it's time to start and stop the game.
 *
 */
public class ServerConnectionFragment extends Fragment {

    public static final int PORT = 55555;

    private Button goButton;

    private ArrayAdapter<String> clientAdapter;

    private SocketAcceptorTask acceptorTask;

    public ServerConnectionFragment() {
        // Required empty public constructor
    }

    public static ServerConnectionFragment newInstance() {

        ServerConnectionFragment demoFragment = new ServerConnectionFragment();

        return demoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lan_demo_server, container, false);

        TextView ipText = (TextView) rootView.findViewById(R.id.server_ip_text);
        ipText.setText(getWifiAddress());

        TextView portText = (TextView) rootView.findViewById(R.id.server_port_text);
        portText.setText(Integer.toString(PORT));

        goButton = (Button) rootView.findViewById(R.id.start_button);
        goButton.setOnClickListener(new GoButtonListener());

        ListView clientList = (ListView) rootView.findViewById(R.id.client_list_view);
        clientAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        clientList.setAdapter(clientAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startAcceptorTask();
    }

    @Override
    public void onPause() {
        stopAcceptorTask();
        super.onPause();
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

    private void startAcceptorTask() {
        stopAcceptorTask();

        acceptorTask = new SocketAcceptorTask();
        acceptorTask.setListener(new ClientAcceptedListener());
        acceptorTask.execute(PORT);

        clearMessages();
        addMessage("Accepting clients...");
        goButton.setText("Play!");
    }

    private void stopAcceptorTask() {
        if (null != acceptorTask) {
            acceptorTask.cancelAcceptor();
            addMessage("No more clients!");
        }
        acceptorTask = null;
        goButton.setText("Reset.");
    }

    private void clearMessages() {
        clientAdapter.clear();
    }

    private void addMessage(String message) {
        clientAdapter.add(message);
    }

    private void addClient(Socket client) {
        ((ServerActivity) getActivity()).addSession(client);
        addMessage(client.getInetAddress().getHostAddress());
    }

    private class GoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == acceptorTask) {
                ((ServerActivity) getActivity()).stopSessions();
                startAcceptorTask();
            } else {
                stopAcceptorTask();
                ((ServerActivity) getActivity()).startSessions();
            }
        }
    }

    private class ClientAcceptedListener implements SocketListener {
        @Override
        public void onSocketConnected(Socket socket) {
            addClient(socket);
        }
    }
}
