package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_cust_service;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_notification extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private List<Data_cust_service> items;

    private CustomerListener listener;

    public adapter_notification(Activity activity, List<Data_cust_service> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (CustomerListener) activity;
        this.context = activity;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Data_cust_service data = items.get(position);


        holder.txt_nama_user.setText(data.getnama_pelanggan());
        holder.txt_email_user.setText(data.getkat_complain());
        if(data.getstatus_complain().equals("0")) {
            holder.txt_nama_user.setTypeface(null, Typeface.BOLD);
            holder.txt_email_user.setTypeface(null, Typeface.BOLD);
        }
        else{
            holder.txt_nama_user.setTypeface(null, Typeface.NORMAL);
            holder.txt_email_user.setTypeface(null, Typeface.NORMAL);
        }


        holder.txt_isi_notification.setText(data.getisi_complain());
        holder.txt_tgl.setText(data.getcreated_date());

        if(data.getstatus_complain().equals("0")){

            Picasso.with(holder.img_notification2.getContext())
                    .load(R.drawable.ic_program_status_yellow_72)
                    .fit()
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    //.resize(100, 100)
                    //.onlyScaleDown()
                    .centerCrop()
                    .into(holder.img_notification2);
        }
        else if(data.getstatus_complain().equals("1")){

            Picasso.with(holder.img_notification2.getContext())
                    .load(R.drawable.ic_program_status_green_72)
                    .fit()
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    //.resize(100, 100)
                    //.onlyScaleDown()
                    .centerCrop()
                    .into(holder.img_notification2);
        }



        view.setOnClickListener(this.getCustomerClick(position, data));

        return view;
    }

    private View.OnClickListener getCustomerClick(final int position, final Data_cust_service customer) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCustomerOnClick(position, customer);
                }
            }
        };
    }

    static class ViewHolder {
        @BindView(R.id.img_notification)
        ImageView img_notification;
        @BindView(R.id.img_notification2)
        ImageView img_notification2;
        @BindView(R.id.txt_nama_user)
        TextView txt_nama_user;
        @BindView(R.id.txt_email_user)
        TextView txt_email_user;
        @BindView(R.id.txt_isi_notification)
        TextView txt_isi_notification;
        @BindView(R.id.txt_tgl)
        TextView txt_tgl;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CustomerListener {
        void onCustomerOnClick(int position, Data_cust_service customer);
    }
}
