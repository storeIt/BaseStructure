package com.example.om.basestructure.helper;

import android.content.Context;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;

/**
 * Created by android on 04.03.18.
 */

public class LocationHelper {

    public static final float DEFAULT_RADIUS_IN_METERS = 1000;

    public static Geofence createGeofences(double latitude, double longitude, float radius, String id) {
        Geofence.Builder geofence = new Geofence.Builder();
        geofence
                .setRequestId(id)
                .setCircularRegion(latitude, longitude, radius);
        return geofence.build();
    }

    public static GeofencingRequest createGeofenceRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    public static GeofencingClient getGeofencingClient(Context context) {
        return new GeofencingClient(context);
    }
}
