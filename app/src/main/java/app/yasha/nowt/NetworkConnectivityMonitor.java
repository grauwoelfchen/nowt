package app.yasha.nowt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

// NetworkConnectivityMonitor
public class NetworkConnectivityMonitor
  extends LiveData<Boolean> {
  private static final String TAG = "NetworkConnectivityMonitor";

  protected Context context;
  protected NetworkConnectivityCallback callback;
  protected ConnectivityManager connectivityManager;

  // https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback
  public class NetworkConnectivityCallback
      extends ConnectivityManager.NetworkCallback {
    private static final String TAG = "NetworkConnectivityCallback";

    // This is called if no network is found or the network request cannot be
    // fulfilled.
    @Override
    public void onUnavailable() {
      super.onUnavailable();
      Log.d(TAG, "(onUnavailable)");
      postValue(false);
    }

    @Override
    public void onCapabilitiesChanged(
      @NonNull Network network, NetworkCapabilities networkCapabalities) {
      super.onCapabilitiesChanged(network, networkCapabalities);
      Log.d(TAG, "(onCapabilitiesChanged) " + String.format(
        "network: %s, networkCapabilities: %s", network, networkCapabalities));
      boolean status = hasAnyTransport(networkCapabalities);
      postValue(status);
    }
  }

  public NetworkConnectivityMonitor(Context context) {
    this.context = context;
    this.connectivityManager = (ConnectivityManager)context
      .getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  protected void registerNetworkCallback() {
    Log.d(TAG, "(registerNetworkCallback)");
    try {
      boolean status = checkCapabilities();
      postValue(status);

      this.callback = new NetworkConnectivityCallback();
      connectivityManager.registerDefaultNetworkCallback(callback);
    } catch (Exception e) {
      // NOTE:
      // If the app does not have ACCESS_NETWORK_STATE permission, then this
      // may cause an exception like:
      // Neither user 10407 nor current process has
      // android.permission.ACCESS_NETWORK_STATE.
      Log.d(TAG, "(registerNetworkCallback) " +
        String.format("e: %s", e.getMessage()));
      postValue(false);
    }
  }

  protected void unregisterNetworkCallback() {
    Log.d(TAG, "(unregisterNetworkCallback)");
    connectivityManager.unregisterNetworkCallback(callback);
  }

  // Checks an active network's capabilities. Due to avoid race conditions (for
  // the use of one of ConnectivityManager methods), do not call this within
  // the network callbacks.
  private boolean checkCapabilities() {
    Network network = connectivityManager.getActiveNetwork();
    if (network == null) {
      return false;
    } else {
      NetworkCapabilities cap =
        connectivityManager.getNetworkCapabilities(network);
      return hasAnyTransport(cap);
    }
  }

  private boolean hasAnyTransport(@Nullable NetworkCapabilities cap) {
    if (cap == null) {
      return false;
    } else {
      return (
        cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
        cap.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
      );
    }
  }
}
