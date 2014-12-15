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

import com.contentful.java.cda.CDAClient;
import com.contentful.java.cma.CMAClient;

/**
 * Clients.
 */
public class Clients {
  static CDAClient sClientCda;
  static CMAClient sClientCma;

  private Clients() {
  }

  public synchronized static CDAClient cda() {
    if (sClientCda == null) {
      sClientCda = new CDAClient.Builder()
          .setSpaceKey(CFApp.get().getString(R.string.cf_space_id))
          .setAccessToken(CFApp.get().getString(R.string.cf_token_cda))
          .build();
    }
    return sClientCda;
  }

  public synchronized static CMAClient cma() {
    if (sClientCma == null) {
      sClientCma = new CMAClient.Builder()
          .setAccessToken(CFApp.get().getString(R.string.cf_token_cma))
          .build();
    }
    return sClientCma;
  }
}
