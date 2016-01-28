package gcm.play.android.samples.com.gcmquickstart;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;

import java.io.IOException;

/**
 * Created by rlopez on 26/01/2016.
 */
public class TopicsUtil {
    public static final String[] TOPICS = {"red", "green", "blue"};
    private static final String TAG = TopicsUtil.class.getSimpleName();
    private static String myToken = "";

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    public static void subscribeToAllTopics(Context ctx, String token) throws IOException {
        Log.i(TAG, "subscribeToAllTopics");
        Log.i(TAG, "myToken.equals(token): " + myToken.equals(token));
        Log.i(TAG, "sharedPrefenceExist(ctx): " + sharedPrefenceExist(ctx));

        if(!myToken.equals(token) || !sharedPrefenceExist(ctx)){
            myToken = token;
            GcmPubSub pubSub = GcmPubSub.getInstance(ctx);
            for (String topic : TOPICS) {
                String currentTopic = "/topics/" + topic;
                pubSub.subscribe(token, currentTopic, null);
                saveTopicStateToSharedPreference(ctx, currentTopic, true);
            }
        }
    }
    // [END subscribe_topics]

    public static void saveTopicStateToSharedPreference(Context ctx, String topic, Boolean subscribeTopic) {
        Log.i(TAG, "saveTopicStateToSharedPreference: " + topic + " - value: " + subscribeTopic);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        sharedPreferences.edit().putBoolean(topic, subscribeTopic).apply();
    }

    /**
     * Subscribe to a GCM topic of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topic]
    public static void subscribeTopic(final Context ctx, final String topic) {
        Log.i(TAG, "subscribeTopic: " + topic);
        new Thread() {
            public void run() {
                try {
                    GcmPubSub pubSub = GcmPubSub.getInstance(ctx);
                    String currentTopic = "/topics/" + topic;
                    pubSub.subscribe(myToken, currentTopic, null);
                    saveTopicStateToSharedPreference(ctx, currentTopic, true);

                } catch(IOException v) {
                    System.out.println(v);
                }
            }
        }.start();
    }
    // [END subscribe_topic]

    /**
     * Unsubscribe to a GCM topic of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START unsubscribe_topic]
    public static void unsubscribeTopic(final Context ctx, final String topic) {
        Log.i(TAG, "unsubscribeTopic: " + topic);
        new Thread() {
            public void run() {
                try {
                    GcmPubSub pubSub = GcmPubSub.getInstance(ctx);
                    String currentTopic = "/topics/" + topic;
                    pubSub.unsubscribe(myToken, currentTopic);
                    saveTopicStateToSharedPreference(ctx, currentTopic, false);
                } catch(IOException v) {
                    System.out.println(v);
                }
            }
        }.start();
    }
    // [END unsubscribe_topic]

    public static Boolean isSubcribe(Context ctx, String topic){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getBoolean("/topics/" + topic, false);
    }

    private static Boolean sharedPrefenceExist(Context ctx){
        Log.i(TAG, "contains red: " + PreferenceManager.getDefaultSharedPreferences(ctx).contains(TOPICS[0]));
        Log.i(TAG, "contains green: " + PreferenceManager.getDefaultSharedPreferences(ctx).contains(TOPICS[1]));
        Log.i(TAG, "contains blue: " + PreferenceManager.getDefaultSharedPreferences(ctx).contains(TOPICS[2]));

        return PreferenceManager.getDefaultSharedPreferences(ctx).contains(TOPICS[0]) ||
                PreferenceManager.getDefaultSharedPreferences(ctx).contains(TOPICS[1]) ||
                PreferenceManager.getDefaultSharedPreferences(ctx).contains(TOPICS[2]);
    }
}
