package ru.anisart.intechtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailActivity extends ActionBarActivity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        TextView titleView = (TextView) findViewById(R.id.detail_title_view);
        TextView descrView = (TextView) findViewById(R.id.detail_descr_view);
        ImageView imageView = (ImageView) findViewById(R.id.detail_image_view);

        Intent intent = getIntent();
        titleView.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE));
        descrView.setText(intent.getStringExtra(MainActivity.EXTRA_DESCRIPTION));
        ImageLoader.getInstance().displayImage(intent.getStringExtra(MainActivity.EXTRA_IMG), imageView);
    }
}