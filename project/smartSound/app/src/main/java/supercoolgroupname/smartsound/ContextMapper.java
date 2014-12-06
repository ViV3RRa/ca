package supercoolgroupname.smartsound;
import android.content.Context;

import java.util.Map;
/**
 * Created by krogh on 12/6/14.
 */
public class ContextMapper {
    private static Map<DerivedContext, ContextHandler> contextMap;
     static {
         contextMap.put(DerivedContext.cinema, new CinemaContextHandler());
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
