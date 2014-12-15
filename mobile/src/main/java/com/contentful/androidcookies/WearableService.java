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

import android.content.Intent;
import com.contentful.androidcookies.shared.Constants;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * WearableService.
 */
public class WearableService extends WearableListenerService {
  @Override public void onMessageReceived(MessageEvent messageEvent) {
    super.onMessageReceived(messageEvent);
    String path = messageEvent.getPath();
    if (Constants.PATH_CREATE_COOKIE.equals(path)) {
      startService(new Intent(this, CookieService.class)
          .setAction(CookieService.ACTION_CREATE_COOKIE)
          .putExtra(CookieService.EXTRA_COOKIE, new String(messageEvent.getData())));
    } else if (Constants.PATH_REQUEST_COOKIE.equals(path)) {
      startService(new Intent(this, CookieService.class)
          .setAction(CookieService.ACTION_REQUEST_COOKIE));
    }
  }
}
