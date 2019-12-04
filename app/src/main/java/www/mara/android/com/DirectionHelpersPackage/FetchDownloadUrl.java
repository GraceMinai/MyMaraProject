package www.mara.android.com.DirectionHelpersPackage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import www.mara.android.com.ActivitiesPackage.MainActivity;

public class FetchDownloadUrl extends AsyncTask<String, Void, String>
{

    Context mContext;
    String directionMode = "driving";

    public FetchDownloadUrl(MainActivity mainActivity) {
    }

    @Override
    protected String doInBackground(String... strings)
    {
        //For storing data from web service
        String directionData = "";
        directionMode = strings[1];

        try
        {
            //Fetching data from the web service
            directionData = downloadUrl(strings[0]);
            Log.d("my log", "Background task data" + directionData.toString());

        } catch (IOException e)
        {
            Log.d("Background Task", e.toString());
        }
        return directionData;


    }

    // Executes in UI thread, after the execution of
    // doInBackground()


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        PointsParser parserTask = new PointsParser(mContext, directionMode);
        // Invokes the thread for parsing the JSON data

        parserTask.execute(result);


    }

    /** A method to download json data from url
     * Getting the direction using http url connection */

    private String downloadUrl(String directionUrl) throws IOException
    {
        String data = "";
        InputStream  inputStream = null;
        HttpURLConnection  httpURLConnection = null;

        try
        {
            URL url = new URL(directionUrl);
            //Creating an http connection to communicate with the URL
            httpURLConnection = (HttpURLConnection) url.openConnection();

            //Connecting to URL
            httpURLConnection.connect();

            //Reading direction data from the URL
            inputStream = httpURLConnection.getInputStream();
            BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);

            }
            data = stringBuffer.toString();
            Log.d("My log", "Downloaded URL" + data.toString());
            bufferedReader.close();
        }
        catch (Exception e)
        {
           Log.d("My log", "Exception downloading url" + e.toString());
        }
        finally
        {

        }
        return data;


    }
}
