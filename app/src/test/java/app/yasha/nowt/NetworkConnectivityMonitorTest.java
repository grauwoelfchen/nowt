package app.yasha.nowt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import java.lang.System;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkCapabilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class NetworkConnectivityMonitorTest {
	// This rule is needed to wait for a value by LiveData
  // https://developer.android.com/topic/libraries/architecture/livedata
  @Rule
	public InstantTaskExecutorRule instantTaskExecutorRule =
			new InstantTaskExecutorRule();

  @Test
  public void networkConnectivityMonitor_DetectsNotConnected() {
    Context context = (Context)ApplicationProvider.getApplicationContext();
    ConnectivityManager manager = (ConnectivityManager)context
			.getSystemService(Context.CONNECTIVITY_SERVICE);

    // connecting to a network without any transport
    NetworkCapabilities cap = ShadowNetworkCapabilities.newInstance();
    shadowOf(manager).setNetworkCapabilities(
        manager.getActiveNetwork(), cap);

    ActivityController controller = Robolectric
      .buildActivity(AppCompatActivity.class)
      .create()
      .start();
    AppCompatActivity activity = (AppCompatActivity)controller.get();

    NetworkConnectivityMonitor connectivityMonitor =
      new NetworkConnectivityMonitor(context);

    connectivityMonitor.observe(activity, new Observer<Boolean>() {
      @Override
      public void onChanged(Boolean isConnected) {
				System.err.printf("isConnected: %s\n", isConnected);
        // TODO: is there any good way to ensure that onChange is called?
				assertFalse(isConnected);
      }
    });

    connectivityMonitor.registerNetworkCallback();
  }

  @Test
  public void networkConnectivityMonitor_DetectsConnected() {
    Context context = (Context)ApplicationProvider.getApplicationContext();
    ConnectivityManager manager = (ConnectivityManager)context
			.getSystemService(Context.CONNECTIVITY_SERVICE);

    // connecting to a network via WIFI
    NetworkCapabilities cap = ShadowNetworkCapabilities.newInstance();
    shadowOf(cap).addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
    shadowOf(manager).setNetworkCapabilities(
        manager.getActiveNetwork(), cap);

    ActivityController controller = Robolectric
      .buildActivity(AppCompatActivity.class)
      .create()
      .start();
    AppCompatActivity activity = (AppCompatActivity)controller.get();

    NetworkConnectivityMonitor connectivityMonitor =
      new NetworkConnectivityMonitor(context);
    connectivityMonitor.observe(activity, new Observer<Boolean>() {
      @Override
      public void onChanged(Boolean isConnected) {
				System.err.printf("isConnected: %s\n", isConnected);
        // TODO: is there any good way to ensure that onChange is called?
				assertTrue(isConnected);
      }
    });

    connectivityMonitor.registerNetworkCallback();
  }
}
