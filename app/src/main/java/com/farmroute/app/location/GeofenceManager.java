package com.farmroute.app.location;

import android.location.Location;

import com.farmroute.app.data.local.Field;

import java.util.List;

/**
 * Simple point in circle matching. Given the current location and the set of
 * registered fields, returns the field whose geofence contains the point, or
 * null if none match. Kept deliberately lightweight so it works fully offline
 * with no map download.
 */
public class GeofenceManager {

    /** Returns the matching field, or null. If several match, the nearest centre wins. */
    public static Field matchField(double lat, double lng, List<Field> fields) {
        Field best = null;
        float bestDistance = Float.MAX_VALUE;
        float[] out = new float[1];

        for (Field field : fields) {
            Location.distanceBetween(lat, lng, field.latitude, field.longitude, out);
            if (out[0] <= field.radiusMeters && out[0] < bestDistance) {
                bestDistance = out[0];
                best = field;
            }
        }
        return best;
    }

    public static float distanceMeters(double lat1, double lng1, double lat2, double lng2) {
        float[] out = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, out);
        return out[0];
    }
}
