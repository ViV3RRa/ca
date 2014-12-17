package supercoolgroupname.smartsound;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mathias on 17-12-2014.
 */
public class ExternalCommunicator {
    public class ShowTimeEntry {
        public String time;
        public long duration;
        public String movieName;

        public ShowTimeEntry(String t, long d, String m) {
            time = t;
            duration = d;
            movieName = m;
        }
    }

    public String getTextFromUrl(URL url) {
        String result = "";
        try {
            // Create a URL for the desired page

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                result += str;
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return result;
    }

    public ArrayList<ShowTimeEntry> getShowTimes(Location location, long currentTime) throws MalformedURLException, JSONException {
        ArrayList<ShowTimeEntry> showTimeEntries = new ArrayList<ShowTimeEntry>();
        String json = getTextFromUrl(new URL("skovbasse.dk/contextawareness/showtimes.txt"));
        JSONObject obj = new JSONObject(json);
        String showTime = obj.getJSONObject("showTime").getString("time");
        String showDuration = obj.getJSONObject("showTime").getString("duration");
        String showName = obj.getJSONObject("showTime").getString("moviename");

        showTimeEntries.add(new ShowTimeEntry(showTime, Long.parseLong(showDuration), showName));

        return showTimeEntries;
    }

    public double getMedianForSound() throws MalformedURLException, JSONException {
        double result = 0;
        String json = getTextFromUrl(new URL("skovbasse.dk/contextawareness/treedecision.txt"));
        JSONObject obj = new JSONObject(json);
        String median = obj.getJSONObject("treedecision").getString("median");
        result = Double.parseDouble(median);

        return result;
    }
}
