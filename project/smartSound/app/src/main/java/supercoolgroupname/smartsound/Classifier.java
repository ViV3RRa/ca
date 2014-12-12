package supercoolgroupname.smartsound;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by krogh on 12/6/14.
 */
public class Classifier {

    public interface ClassifierListener {
        public void onContextChange(DerivedContext con);
    }

    private static ClassifierListener listener = null;

    public static void registerListener(ClassifierListener listen)
    {
        listener = listen;
    }

    private static DerivedContext context = DerivedContext.undefined;

    private static final int window_size = 1000;
    private static List<Double> soundData = new ArrayList<Double>();
    public static void appendReading(double soundReading)
    {
        soundData.add(soundReading);
        if(soundData.size() >= window_size)
        {
            //Pair<Double,Double> avg_stddiv = calculate_avg_and_stddiv();
            double median = calculate_median();
            context = findContext(median);
            listener.onContextChange(context);
            soundData.subList(0,window_size/2).clear();
        }
    }

    private static DerivedContext findContext(double median)
    {
       // double avg = avg_stddiv.first;
        //double stdDiv = avg_stddiv.second;
        Log.d("median: ","" + median);
        Log.d("size: ","" + soundData.size());

        // TODO: Classifier tree here
        if(median <= 41.0364){
            return DerivedContext.outside;
        }else{
            return DerivedContext.cinema;
        }
        /*median <= 41.0364: Lobby (50.0)
        median > 41.0364: Cinema (91.0)
        if(avg <= 50.143902)
            if(stdDiv <= 20.105542)
                return DerivedContext.outside;
            else
                return DerivedContext.cinema;
        else if(stdDiv > 20.105542)
            if(avg <= 46.789065)
                if (stdDiv <= 28.602908)
                    return DerivedContext.outside;
                else
                    return DerivedContext.cinema;
            else
                return DerivedContext.cinema;
        else
            return DerivedContext.cinema;*/
    }

    /*private static Pair<Double, Double> calculate_avg_and_stddiv()
    {
        double avg = 0;
        for(double reading : soundData)
        {
            avg += reading;
        }
        avg /= soundData.size();

        double sumDiv = 0;
        for(double reading : soundData)
        {
            sumDiv += Math.pow(avg-reading, 2);
        }
        sumDiv /= window_size;
        double stdVar = Math.sqrt(sumDiv);

        return new Pair<Double,Double>(avg, stdVar);
    }*/

    private static double calculate_median(){
        List<Double> newSoundData = new ArrayList<Double>(soundData);
        Collections.sort(newSoundData);
        double median = newSoundData.get(newSoundData.size()/2);

        return median;
    }

    public static DerivedContext getContext(){
        return context;
    }
}
