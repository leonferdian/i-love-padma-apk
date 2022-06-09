package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_work_report;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_list_respon extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;

    private List<Data_work_report> items;

    private ListPoint listener;

    public adapter_list_respon(Activity activity, List<Data_work_report> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (ListPoint) activity;
        this.context = activity;
    }

    public List<Data_work_report> getSurat() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_respon, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Data_work_report data = items.get(position);

        holder.task_name.setText(data.getcreate_by());
        holder.task_name.setTypeface(null, Typeface.BOLD);
        holder.task_date.setText(data.getdate_create());
        holder.task_from.setText(data.getketerangan());

        String img = Server.URL2 + "image_upload/list_foto_complain_respon/"+data.getcomplain_foto();
        Picasso.with(holder.image1.getContext())
                .load(img)
                .placeholder(R.drawable.default_photo)
                .fit().centerCrop().into(holder.image1);

        if(data.getid_user().equals(data.getuser_login())){
            holder.layout_utama.setBackgroundColor(holder.layout_utama.getResources().getColor(R.color.vol_yel));
           // holder.task_name.setTextColor(holder.task_name.getResources().getColor(R.color.yellow_color));
        }


        holder.image1.setOnClickListener(this.getImageClick(position, data));

        view.setOnClickListener(this.getSuratClick(position, data));

        return view;
    }

    private View.OnClickListener getSuratClick(final int position, final Data_work_report st) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSuratOnClick(position, st);
                }
            }
        };
    }

    private View.OnClickListener getImageClick(final int position, final Data_work_report st) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onImageOnClick(position, st);
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
        @BindView(R.id.image_1)
        ImageView image1;
        @BindView(R.id.layout_utama)
        LinearLayout layout_utama;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ListPoint {
        void onSuratOnClick(int position, Data_work_report berita);
        void onImageOnClick(int position, Data_work_report berita);
    }
}
