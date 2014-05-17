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
    private boolean gridMode;

    public DataAdapter(Context context, List<DataModel> dataList, boolean gridMode) {
        super(context, R.layout.list_item, dataList);

        this.context = context;
        this.dataList = dataList;
        this.gridMode = gridMode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null || ((ViewHolder) convertView.getTag()).gridMode != gridMode) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(gridMode ? R.layout.grid_item : R.layout.list_item, null);

            holder.gridMode = gridMode;
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.titleView = (TextView) convertView.findViewById(R.id.title_view);
            if (!gridMode) {
                holder.descrView = (TextView) convertView.findViewById(R.id.descr_view);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DataModel dataModel = dataList.get(position);
        if (dataModel != null) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(CACHE_IN_MEMORY)
                    .cacheOnDisc(CACHE_ON_DISK)
                    .build();
            ImageLoader.getInstance().displayImage(dataModel.getImg(), holder.imageView, options);

            holder.titleView.setText(dataModel.getTitle());
            if (!gridMode) {
                holder.descrView.setText(dataModel.getDescription());
            }
        } else {
            Log.w(TAG, "dataModel " + position + " is null");
        }

        return convertView;
    }

    public void setGridMode(boolean gridMode) {
        if (this.gridMode != gridMode) {
            this.gridMode = gridMode;
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        public boolean gridMode;
        public ImageView imageView;
        public TextView titleView;
        public TextView descrView;
    }
}