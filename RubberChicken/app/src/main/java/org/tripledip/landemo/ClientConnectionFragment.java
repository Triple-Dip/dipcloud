package org.tripledip.landemo;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import org.tripledip.rubberchicken.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Ben on 4/11/15.
 */
public class ClientConnectionFragment extends Fragment {

    private SocketConnectorTask connectorTask;

    private Button goButton;

    private NumberPicker ipPicker1;
    private NumberPicker ipPicker2;
    private NumberPicker ipPicker3;
    private NumberPicker ipPicker4;
    private NumberPicker portPicker;

    public ClientConnectionFragment() {
        // Required empty public constructor
    }

    public static ClientConnectionFragment newInstance() {

        ClientConnectionFragment demoFragment = new ClientConnectionFragment();

        return demoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lan_demo_client, container, false);

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

        addressToPickers(getWifiAddress(), ServerConnectionFragment.PORT);

        return rootView;
    }

    @Override
    public void onPause() {
        stopConnectorTask();
        disconnect();
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

    private void startConnectorTask() {
        stopConnectorTask();

        connectorTask = new SocketConnectorTask();
        connectorTask.setListener(new SocketConnectedListener());
        connectorTask.execute(pickersToAddress());

        goButton.setText("Connecting...");
    }

    private void stopConnectorTask() {
        if (null != connectorTask) {
            connectorTask.cancelConnector();
        }
        connectorTask = null;
    }

    private void disconnect() {
        stopConnectorTask();
        ((ClientActivity) getActivity()).stopClient();
        goButton.setText("Connect!");
    }

    private void addConnection(Socket socket) {
        ((ClientActivity) getActivity()).startClient(socket);
        goButton.setText("Disconnect.");
    }

    private class GoButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == connectorTask) {
                startConnectorTask();
            } else {
                disconnect();
            }
        }
    }

    private class SocketConnectedListener implements SocketConnectorTask.Listener {
        @Override
        public void onSocketConnected(Socket socket) {
            if (null == socket) {
                disconnect();
                return;
            }
            addConnection(socket);
        }
    }
}
