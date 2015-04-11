package org.tripledip.landemo;

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

import org.tripledip.rubberchicken.R;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ben on 4/11/15.
 */
public class ServerConnectionFragment extends Fragment {

    private ArrayAdapter<String> clientAdapter;

    private ServerSocket acceptor;

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

        Button goButton = (Button) rootView.findViewById(R.id.start_button);
        goButton.setOnClickListener(new GoButtonListener());

        ListView clientList = (ListView) rootView.findViewById(R.id.client_list_view);
        clientAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        clientList.setAdapter(clientAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        clientAdapter.clear();

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

        try {
            acceptor = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        acceptorTask = new SocketAcceptorTask();
        acceptorTask.setListener(new ClientAcceptedListener());
        acceptorTask.execute(acceptor);

        addMessage("Accepting clients...");
    }

    private void stopAcceptorTask() {
        if (null != acceptor) {
            try {
                acceptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            addMessage("No more clients.");
        }
        acceptor = null;

        if (null != acceptorTask) {
            acceptorTask.cancel(true);
        }
        acceptorTask = null;
    }

    private void addMessage(String message) {
        clientAdapter.add(message);
    }

    private void addClient(Socket client) {
        addMessage(client.getInetAddress().getHostAddress());
    }

    private class GoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            stopAcceptorTask();
        }
    }

    private class ClientAcceptedListener implements SocketAcceptorTask.Listener {
        @Override
        public void onSocketAccepted(Socket socket) {
            addClient(socket);
        }
    }
}
