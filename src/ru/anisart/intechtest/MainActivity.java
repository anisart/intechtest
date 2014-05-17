package ru.anisart.intechtest;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.*;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "IntechTest";
    private static final String API_URI = "http://62.152.36.68:8080/droid-test/common/data";
    private ListView listView;
    private GridView gridView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView) findViewById(R.id.list_view);
        gridView = (GridView) findViewById(R.id.grid_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_LONG).show();
                new RefreshTask().execute();
                return true;
            case R.id.action_view_mode:
                if (adapter != null) {
                    gridView.setAdapter(adapter);
                    gridView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    listView.setAdapter(null);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private class RefreshTask extends AsyncTask<Void, Void, Void> {  //TODO: SingleTask

        DataModel[] dataModels;

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "RefreshTask started");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
            //TODO: Network is unreachable
            dataModels = restTemplate.getForObject(API_URI, DataModel[].class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            List<DataModel> dataList = new ArrayList<DataModel>(Arrays.asList(dataModels));
            dataList.removeAll(Collections.singleton(null));
            dataList.addAll(dataList);
            adapter = new DataAdapter(getBaseContext(), dataList);
            listView.setAdapter(adapter);
            Log.d(TAG, "setAdapter()");
        }
    }

}