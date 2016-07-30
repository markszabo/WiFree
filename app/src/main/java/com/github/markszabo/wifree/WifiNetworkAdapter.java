package com.github.markszabo.wifree;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WifiNetworkAdapter extends ArrayAdapter<WifiNetwork> {
    public WifiNetworkAdapter(Context context, ArrayList<WifiNetwork> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WifiNetwork network = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_wifinetwork, parent, false);
        }
        // Lookup view for data population
        TextView tvSSID = (TextView) convertView.findViewById(R.id.tvSSID);
        TextView tvBSSID = (TextView) convertView.findViewById(R.id.tvBSSID);
        TextView tvIsVulnerable = (TextView) convertView.findViewById(R.id.tvIsVulnerable);
        // Populate the data into the template view using the data object
        tvSSID.setText(network.SSID);
        tvBSSID.setText(network.BSSID);
        tvIsVulnerable.setText(network.getVulnerabilityMessage());

        /*convertView.setTag(network);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void OnClick(View view){
                WifiNetwork network = (WifiNetwork) view.getTag();

            }
        });*/

        // Return the completed view to render on screen
        return convertView;
    }
}
