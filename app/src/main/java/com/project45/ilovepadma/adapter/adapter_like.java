package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_point;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_like extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;

    private List<Data_point> items;

    private ListPoint listener;

    public adapter_like(Activity activity, List<Data_point> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (ListPoint) activity;
        this.context = activity;
    }

    public List<Data_point> getSurat() {
        //mPhotos.remove(0);
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        boolean convertViewWasNull = false;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_like, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Data_point data = items.get(position);

        holder.task_name.setText(data.getkomen_from());
        holder.task_name.setTypeface(null, Typeface.BOLD);
        holder.task_date.setText(data.getdate_create());
        //holder.task_from.setText(data.getkomen_from());


        //view.setOnClickListener(this.getCustomerClick(position, data));

        return view;
    }

    private View.OnClickListener getSuratClick(final int position, final Data_point st) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSuratOnClick(position, st);
                }
            }
        };
    }



    static class ViewHolder {


        @BindView(R.id.task_name)
        TextView task_name;
        @BindView(R.id.task_from)
        TextView task_from;
        @BindView(R.id.task_date)
        TextView task_date;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ListPoint {
        void onSuratOnClick(int position, Data_point berita);
    }
}
