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

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * CookieActivity.
 */
public class CookieActivity extends Activity {
  public static final String EXTRA_COOKIE = "com.contentful.androidcookies.EXTRA_COOKIE";

  @InjectView(R.id.tv_cookie) TextView tvCookie;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_cookie);

    WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
    stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
      @Override public void onLayoutInflated(WatchViewStub watchViewStub) {
        ButterKnife.inject(CookieActivity.this);
        tvCookie.setText(getIntent().getStringExtra(EXTRA_COOKIE));
      }
    });
  }
}
