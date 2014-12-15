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
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.contentful.androidcookies.shared.Constants;
import com.contentful.androidcookies.shared.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import java.util.List;

/**
 * MainActivity.
 */
public class MainActivity extends Activity {
  private static final int SPEECH_REQUEST_CODE = 1;
  private GoogleApiClient googleApiClient;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initApiClient();

    WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
    stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
      @Override public void onLayoutInflated(WatchViewStub watchViewStub) {
        ButterKnife.inject(MainActivity.this);
      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
      List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      if (results != null && results.size() > 0) {
        Utils.sendMessage(googleApiClient, Constants.PATH_CREATE_COOKIE, results.get(0).getBytes());
      }
    }
  }

  @OnClick(R.id.btn_create)
  void onClickCreate() {
    startActivityForResult(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM),
        SPEECH_REQUEST_CODE);
  }

  @OnClick(R.id.btn_request)
  void onClickRequest() {
    Utils.sendMessage(googleApiClient, Constants.PATH_REQUEST_COOKIE);
  }

  private void initApiClient() {
    googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wearable.API)
        .build();

    googleApiClient.connect();
  }
}
