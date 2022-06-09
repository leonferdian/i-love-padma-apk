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

public class adapter_cust_service2 extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private List<Data_cust_service> items;

    private CustomerListener listener;

    public adapter_cust_service2(Activity activity, List<Data_cust_service> items) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cust_service2, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Data_cust_service data = items.get(position);


        holder.text1.setText("From : "+data.getnama_pelanggan());
        holder.text1.setTypeface(null, Typeface.BOLD);
        holder.text2.setText(data.getcreated_date()+" | "+data.getisi_complain());
        holder.text3.setText("To : "+data.getsign_to());

        //isi_report.setText(data.getisi_report());
        holder.text4.setText(Html.fromHtml(data.getsubject_complain()));
        //holder.text4.setText(data.getcreated_date());
        holder.text5.setText("Respon : "+data.getjml_respon());
        holder.text6.setText(data.getstatus_complain());
        if(data.getstatus_complain().equals("OPEN")){
            holder.text6.setTextColor(holder.text6.getResources().getColor(R.color.oil));
        }
        else if(data.getstatus_complain().equals("PROSES")) {
            holder.text6.setTextColor(holder.text6.getResources().getColor(R.color.yellow_color));
        }
        else if(data.getstatus_complain().equals("CLOSE")) {
            holder.text6.setTextColor(holder.text6.getResources().getColor(R.color.green_color));
        }
        else if(data.getstatus_complain().equals("FINISH")) {
            holder.text6.setTextColor(holder.text6.getResources().getColor(R.color.page_cust_service));
        }
        else if(data.getstatus_complain().equals("REJECT")) {
            holder.text6.setTextColor(holder.text6.getResources().getColor(R.color.page_red));
        }

        holder.text7.setText("( "+data.getrespon_new()+" New )");
        holder.text7.setTextColor(holder.text7.getResources().getColor(R.color.page_red));
        if(data.getrespon_new().equals("0")){
            holder.text7.setVisibility(View.GONE);
        }

        String bm = Server.URL2 +"image_upload/list_foto_pribadi/"+data.getfoto_pelanggan();
        Picasso.with(holder.image1.getContext())
                .load(bm)
                .placeholder(R.drawable.ic_camera_alt_black_48dp)
                .fit().centerCrop().into(holder.image1);



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
        @BindView(R.id.image_1)
        ImageView image1;
        @BindView(R.id.text_1)
        TextView text1;
        @BindView(R.id.text_2)
        TextView text2;
        @BindView(R.id.text_3)
        TextView text3;
        @BindView(R.id.text_4)
        TextView text4;
        @BindView(R.id.text_5)
        TextView text5;
        @BindView(R.id.text_6)
        TextView text6;
        @BindView(R.id.text_7)
        TextView text7;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CustomerListener {
        void onCustomerOnClick(int position, Data_cust_service customer);
    }
}
