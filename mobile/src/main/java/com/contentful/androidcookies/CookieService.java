/*
 * Copyright (C) 2014 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.androidcookies;

import android.app.IntentService;
import android.content.Intent;
import com.contentful.androidcookies.shared.Constants;
import com.contentful.androidcookies.shared.Utils;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cda.model.CDAArray;
import com.contentful.java.cda.model.CDAEntry;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import java.util.HashMap;
import java.util.Random;
import retrofit.RetrofitError;

/**
 * CookieService.
 */
public class CookieService extends IntentService {
  public static final String ACTION_CREATE_COOKIE =
      "com.contentful.androidcookies.ACTION_CREATE_COOKIE";

  public static final String ACTION_REQUEST_COOKIE =
      "com.contentful.androidcookies.ACTION_REQUEST_COOKIE";

  public static final String EXTRA_COOKIE =
      "com.contentful.androidcookies.EXTRA_COOKIE";

  public CookieService() {
    super("CookieService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    String action = intent.getAction();

    if (ACTION_CREATE_COOKIE.equals(action)) {
      actionCreateCookie(intent);
    } else if (ACTION_REQUEST_COOKIE.equals(action)) {
      actionRequestCookie(intent);
    }
  }

  private void actionCreateCookie(Intent intent) {
    RetrofitError error = null;

    try {
      // Create a new Entry
      CMAEntry entry = Clients.cma().entries().create(
          getString(R.string.cf_space_id),
          getString(R.string.cf_content_type_id),
          new CMAEntry().setField("text", intent.getStringExtra(EXTRA_COOKIE), "en-US"));

      // Publish the Entry
      Clients.cma().entries().publish(entry);
    } catch (RetrofitError e) {
      error = e;
    }

    // Notify
    GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wearable.API)
        .build();

    googleApiClient.blockingConnect();

    if (error == null) {
      Utils.sendMessage(googleApiClient, Constants.PATH_PUBLISHED_COOKIE);
    } else {
      Utils.sendMessage(googleApiClient, Constants.PATH_ERROR, error.getMessage().getBytes());
    }
  }

  private void actionRequestCookie(Intent intent) {
    String cookie = null;
    RetrofitError error = null;

    try {
      CDAArray result = Clients.cda().entries().fetchAll(new HashMap<String, String>() { {
        put("limit", "1");
      } });

      int total = result.getTotal();
      if (total == 0) {
        cookie = getString(R.string.cookies_empty);
      } else {
        final int offset = new Random().nextInt(total);

        result = Clients.cda().entries().fetchAll(new HashMap<String, String>() { {
          put("limit", "1");
          put("skip", Integer.toString(offset));
        } });

        cookie = (String) ((CDAEntry) result.getItems().get(0)).getFields().get("text");
      }
    } catch (RetrofitError e) {
      error = e;
    }

    // Notify
    GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wearable.API)
        .build();

    googleApiClient.blockingConnect();

    if (error == null) {
      Utils.sendMessage(googleApiClient, Constants.PATH_DISPLAY_COOKIE, cookie.getBytes());
    } else {
      Utils.sendMessage(googleApiClient, Constants.PATH_ERROR, error.getMessage().getBytes());
    }
  }
}
