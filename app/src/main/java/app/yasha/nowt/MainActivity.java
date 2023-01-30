package app.yasha.nowt;

import android.os.Bundle;
import androidx.lifecycle.Observer;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
  private static final String TAG = "MainActivity";

  protected NetworkConnectivityMonitor connectivityMonitor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    connectivityMonitor = new NetworkConnectivityMonitor(context);
    connectivityMonitor.observe(this, new Observer<Boolean>() {
      @Override
      public void onChanged(Boolean isConnected) {
        if (isConnected) {
          Timber.d(TAG, "(onChanged) Connected");
        } else {
          Timber.d(TAG, "(onChanged) Not connected");
        }
      }
    });
  }

  @Override
  protected void onPause() {
    connectivityMonitor.unregisterNetworkCallback();
    super.onPause();
    Timber.d(TAG, "(onPause)");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Timber.d(TAG, "(onResume)");

    connectivityMonitor.registerNetworkCallback();
  }
}
