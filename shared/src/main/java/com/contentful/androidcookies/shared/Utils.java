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

package com.contentful.androidcookies.shared;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Utils.
 */
public class Utils {
  private Utils() {
  }

  public static void sendMessage(GoogleApiClient client, String path) {
    sendMessage(client, path, null);
  }

  public static void sendMessage(final GoogleApiClient client, final String path,
      final byte[] payload) {
    new Thread(new Runnable() {
      @Override public void run() {
        NodeApi.GetConnectedNodesResult result =
            Wearable.NodeApi.getConnectedNodes(client).await();

        for (Node node : result.getNodes()) {
          Wearable.MessageApi.sendMessage(client, node.getId(), path, payload).await();
        }
      }
    }).start();
  }
}
