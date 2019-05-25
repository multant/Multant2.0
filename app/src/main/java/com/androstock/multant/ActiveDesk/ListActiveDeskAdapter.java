/*
package com.androstock.multant.ActiveDesk;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.androstock.multant.R;
import com.androstock.multant.ActiveDesk.ListActiveDeskViewHolder;
import com.androstock.multant.Task.TaskHome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActiveDeskAdapter extends BaseAdapter {
        private Activity activity;
        final List<String> data = new ArrayList<String>();

        public ListActiveDeskAdapter(Activity a, List<String> d) {
            activity = a;
            data=d;
        }
        public int getCount() {
            return data.size();
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ListActiveDeskViewHolder holder = null;
            if (convertView == null) {
                holder = new ListActiveDeskViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.active_desk_list_row, parent, false);
                holder.active_desk_image = (TextView) convertView.findViewById(R.id.active_desk_image);
                holder.active_desk_name = (TextView) convertView.findViewById(R.id.active_desk_name);
                convertView.setTag(holder);
            } else {
                holder = (ListActiveDeskViewHolder) convertView.getTag();
            }
            holder.active_desk_image.setId(position);
            holder.active_desk_name.setId(position);


            try{
                holder.active_desk_name.setText(active_desk_name);

                // Image
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(getItem(position));
                holder.active_desk_image.setTextColor(color);
                holder.active_desk_image.setText(Html.fromHtml("&#11044;"));
                // Image

            }catch(Exception e) {}
            return convertView;
        }
}


class ListActiveDeskViewHolder {
    TextView active_desk_image;
    TextView active_desk_name;
}
*/
