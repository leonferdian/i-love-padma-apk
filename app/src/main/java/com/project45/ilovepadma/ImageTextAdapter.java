package com.project45.ilovepadma;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

/**
 * Created by Adi Surya on 11/29/2016.
 */

public class ImageTextAdapter extends ArrayAdapter<ItemImageText> {

    public ImageTextAdapter(Context context){
        super(context, 0, new ArrayList<ItemImageText>());
        //mListener = (SecondaryListingListener) context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.segment_text_with_icon, parent, false);
            holder = new ViewHolder();
            holder.textTitle = (TextView) view.findViewById(R.id.text_1);
            holder.textValue = (TextView) view.findViewById(R.id.text_2);
            holder.textIcon = (IconTextView) view.findViewById(R.id.text_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final ItemImageText itemImageText = getItem(position);
        holder.textTitle.setText(itemImageText.text);
        holder.textValue.setText(itemImageText.value);
        holder.textIcon.setText(itemImageText.icon);

        //return super.getView(position, convertView, parent);
        return view;
    }

    static class ViewHolder {
        //public ImageView image_1;
        public TextView textTitle;
        public IconTextView textIcon;
        public TextView textValue;
    }

}
