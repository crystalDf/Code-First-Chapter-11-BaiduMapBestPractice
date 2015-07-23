package com.star.baidumapbestpractice;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MapView mMapView;

    private LocationManager mLocationManager;

    private String mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.map_view);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providerList = mLocationManager.getProviders(true);

        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            mProvider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            mProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No location provider to use", Toast.LENGTH_LONG).show();
            return;
        }

        Location location = mLocationManager.getLastKnownLocation(mProvider);

        if (location != null) {
            LatLng target = new LatLng(location.getLatitude(), location.getLongitude());

            CoordinateConverter coordinateConverter = new CoordinateConverter();

            LatLng baiduTarget = coordinateConverter
                    .coord(target)
                    .from(CoordinateConverter.CoordType.GPS)
                    .convert();

            MapStatus mapStatus = new MapStatus.Builder()
                    .target(baiduTarget)
                    .zoom(16)
                    .build();

            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);

            mMapView.getMap().setMapStatus(mapStatusUpdate);

            mMapView.getMap().setMyLocationEnabled(true);

            MyLocationData myLocationData = new MyLocationData.Builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();

            BitmapDescriptor bitmapDescriptor =
                    BitmapDescriptorFactory.fromResource(R.mipmap.tmntdon);

//            OverlayOptions overlayOptions = new MarkerOptions()
//                    .position(baiduTarget)
//                    .icon(bitmapDescriptor);
//
//            mMapView.getMap().addOverlay(overlayOptions);

            mMapView.getMap().setMyLocationData(myLocationData);

            MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor
            );

            mMapView.getMap().setMyLocationConfigeration(myLocationConfiguration);

//            mMapView.getMap().setMyLocationEnabled(false);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
