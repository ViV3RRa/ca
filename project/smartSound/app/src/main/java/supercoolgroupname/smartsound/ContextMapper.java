package supercoolgroupname.smartsound;
import android.content.Context;
import android.media.AudioManager;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by krogh on 12/6/14.
 */
public class ContextMapper {
    private static Map<DerivedContext, ContextHandler> contextMap;
     static {
         contextMap = new HashMap<DerivedContext, ContextHandler>();

         contextMap.put(DerivedContext.outside, new ContextHandler() {
             @Override
             public void handle(Context context) {
                 AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                 am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);

                 //TODO lower volume and stuff
             }
         });

         contextMap.put(DerivedContext.cinema, new ContextHandler() {
            @Override
            public void handle(Context context) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_RING, 0, 0);

                //TODO lower volume and stuff
            }
         });

         contextMap.put(DerivedContext.undefined, new ContextHandler() {
             @Override
             public void handle(Context context) {
                 //Intentionally empty
             }
         });
     }

    public static void handleContext(DerivedContext conStr, Context conCon){
        ContextHandler handler = contextMap.get(conStr);
        assert handler != null;
        handler.handle(conCon);
    }
}
