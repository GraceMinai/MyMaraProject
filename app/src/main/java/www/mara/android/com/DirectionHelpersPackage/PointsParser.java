package www.mara.android.com.DirectionHelpersPackage;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** A class to parse the Google Directions in JSON format */
public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
{
    TaskLoadedCallback taskLoadedCallback;
    String directionMode = "driving";


    public  PointsParser(Context mContext, String directionMode)
    {
        this.taskLoadedCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;

    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
    {
        JSONObject jsonObject;
        List<List<HashMap<String, String>>> routes = null;
        try
        {
            jsonObject = new JSONObject(jsonData[0]);
            Log.d("mylog", jsonData[0].toString());
            DirectionJSONParser directionJSONParser = new DirectionJSONParser();
            Log.d("mylog", directionJSONParser.toString());

            //Start parsing data
            routes = directionJSONParser.parse(jsonObject);
            Log.d("mylog" , "Executing routes");
            Log.d("mylog", routes.toString());



        }
        catch (Exception e)
        {
            Log.d("mylog", e.toString());
            e.printStackTrace();
        }
        return routes;

    }

    // Executes in UI thread, after the parsing process

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> results)
    {
        ArrayList<LatLng> points;
        PolylineOptions polylineOptions = null;

        //Traveling through all routes
        for (int i=0; i < results.size(); i++)
        {
            points = new ArrayList<>();
            polylineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = results.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++)
            {
                HashMap<String, String> point  = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);

            }

            // Adding all the points in the route to the PolylineOptions
            polylineOptions.addAll(points);
            if (directionMode.equalsIgnoreCase("walking"))
            {
                polylineOptions.width(10);
                polylineOptions.color(Color.MAGENTA);
            }
            else
                {
                    polylineOptions.width(15);
                    polylineOptions.color(Color.BLUE);
                }Log.d("mylog", "onPostExecute lineoptions decoded");

        }

        // Drawing polyline in the Google Map for the i-th route
        if (polylineOptions != null)
        {
            taskLoadedCallback.onTaskDone(polylineOptions);
        }
        else 
            {
                Log.d("mylog", "without Polylines drawn");
            }
    }
}
