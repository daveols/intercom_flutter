package io.maido.intercom;
import com.google.firebase.messaging.*;
import io.intercom.android.sdk.push.IntercomPushClient;
import android.content.Intent;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.Map;

public class PushInterceptService extends FirebaseMessagingService {
    private static final String TAG = "PushInterceptService";
    private static final String ACTION_REMOTE_MESSAGE = "io.flutter.plugins.firebasemessaging.NOTIFICATION";
    private static final String EXTRA_REMOTE_MESSAGE = "notification";
    private final IntercomPushClient intercomPushClient = new IntercomPushClient();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Map message = remoteMessage.getData();

        if (intercomPushClient.isIntercomPush(message)) {
            Log.d(TAG, "Intercom message received");
            intercomPushClient.handlePush(getApplication(), message);
        } else {
            Log.d(TAG, "Push message received, not for Intercom");
            Intent intent = new Intent(ACTION_REMOTE_MESSAGE);
            intent.putExtra(EXTRA_REMOTE_MESSAGE, remoteMessage);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}
