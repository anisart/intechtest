package ru.anisart.intechtest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.*;

import java.util.*;


public class MainActivity extends ActionBarActivity {

    public static final String EXTRA_TITLE = MainActivity.class.getCanonicalName() + "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = MainActivity.class.getCanonicalName() + "EXTRA_DESCRIPTION";
    public static final String EXTRA_IMG = MainActivity.class.getCanonicalName() + "EXTRA_IMG";

    private static final String TAG = "IntechTest";
    private static final String API_URI = "http://62.152.36.68:8080/droid-test/common/data";
    private static final int COLUMNS_LIST = 1;
    private static final int COLUMNS_GRID = -1;

    private GridView gridView;
    private DataAdapter adapter;

    private boolean viewAsGrid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setNumColumns(COLUMNS_LIST);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                DataModel item = adapter.getItem(position);
                if (item != null) {
                    intent.putExtra(EXTRA_TITLE, item.getTitle());
                    intent.putExtra(EXTRA_DESCRIPTION, item.getDescription());
                    intent.putExtra(EXTRA_IMG, item.getImg());
                }
                startActivity(intent);
            }
        });
        refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshData();
                break;
            case R.id.action_view_mode:
                viewAsGrid = !viewAsGrid;
                if (adapter != null) {
                    adapter.setGridMode(viewAsGrid);
                }
                gridView.setNumColumns(viewAsGrid ? COLUMNS_GRID : COLUMNS_LIST);
                item.setIcon(viewAsGrid ? R.drawable.ic_action_view_as_list : R.drawable.ic_action_view_as_grid);
                item.setTitle(viewAsGrid ? R.string.action_list : R.string.action_grid);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        if (isNetworkAvailable()) {
            Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
            new RefreshTask().execute();
        } else {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class RefreshTask extends AsyncTask<Void, Void, Void> {

        DataModel[] dataModels;

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "RefreshTask started");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
            try {
                dataModels = restTemplate.getForObject(API_URI, DataModel[].class);
            } catch (RestClientException e) {
                Log.e(TAG, "RestClientException: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "RefreshTask ended");
            if (dataModels != null) {
                List<DataModel> dataList = new ArrayList<DataModel>(Arrays.asList(dataModels));
                dataList.removeAll(Collections.singleton(null));
                adapter = new DataAdapter(getBaseContext(), dataList, viewAsGrid);
                gridView.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Could not get data from server!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}