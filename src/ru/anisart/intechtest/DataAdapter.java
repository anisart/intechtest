package ru.anisart.intechtest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class DataAdapter extends ArrayAdapter<DataModel> {

    private static final String TAG = "IntechTest";
    private static final boolean CACHE_IN_MEMORY = true;
    private static final boolean CACHE_ON_DISK = true;

    private List<DataModel> dataList;
    private Context context;

    public DataAdapter(Context context, List<DataModel> dataList) {
        super(context, R.layout.list_item, dataList);

        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public DataModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).hashCode(); //TODO: WHY???
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            TextView titleView = (TextView) view.findViewById(R.id.title_view);
            TextView descrView = (TextView) view.findViewById(R.id.descr_view);

            holder.imageView = imageView;
            holder.titleView = titleView;
            holder.descrView = descrView;

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DataModel dataModel = dataList.get(position);
        if (dataModel != null) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(CACHE_IN_MEMORY)
                    .cacheOnDisc(CACHE_ON_DISK)
                    .build();
            ImageLoader.getInstance().displayImage(dataModel.getImg(), holder.imageView, options);
            holder.titleView.setText(dataModel.getTitle());
            holder.descrView.setText(dataModel.getDescription());
        } else {
            Log.w(TAG, "dataModel " + position + " is null");
        }

        return view;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public TextView titleView;
        public TextView descrView;
    }
}