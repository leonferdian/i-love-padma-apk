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
import com.project45.ilovepadma.data.Data_customer;
import com.project45.ilovepadma.outlet.list_outlet2;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_customer3 extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private List<Data_customer> items;

    GPSTracker gps;
    float distance;
    String rr;

    AdapterCallback callback;

    public interface AdapterCallback{
        void onCustomerOnClick(int position, Data_customer customer);
    }

    public adapter_customer3(Activity activity, List<Data_customer> items, AdapterCallback callback) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.callback = callback;
        this.activity = activity;
        this.items = items;
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

    public List<Data_customer> getCustomers() {
        //mPhotos.remove(0);
        return items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;

        boolean convertViewWasNull = false;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer3, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Data_customer data = items.get(position);

        holder.ref = position;
        holder.text1.setText(data.getName());
        holder.text1.setTypeface(null, Typeface.BOLD);
        holder.text2.setText(data.getOutlet_type());


        //isi_report.setText(data.getisi_report());
        holder.text3.setText(Html.fromHtml(data.getAddress()));
        holder.text4.setText("Latitude Not Set");
        holder.text5.setText("Longitude Not Set");

        if(data.getLng() != 0 && data.getLat() != 0){
            gps = new GPSTracker(context,activity);

            if(gps.canGetLocation()) {
                distance = gps.getDistance2(data.getLat(),data.getLng());
                rr = doubleToStringNoDecimal(distance);
            }
            else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
            holder.text4.setText(data.getLat()+"");
            holder.text5.setText(data.getLng()+" | "+rr+" KM");

        }

        holder.text6.setText("Add By : "+data.getOwner_name());

        String bm = Server.URL2 +"image_upload/list_foto_outlet/"+data.getimage_customer();
        Picasso.with(holder.image1.getContext())
                .load(bm)
                .placeholder(R.drawable.ic_camera_alt_black_48dp)
                .fit().centerCrop().into(holder.image1);

        holder.finishcard.setVisibility(View.GONE);
        /*
        if(!data.getid_survey().equals("")){
            holder.finishcard.setVisibility(View.VISIBLE);
        }

         */

        if(data.getStatus_general().equals("DEALING") && data.getStatus_detail().equals("BEING")) {
            holder.text7.setText(data.getStatus_detail()+" "+data.getStatus_general());
        } else {
            holder.text7.setText(data.getStatus_detail()+" Outlet");
        }

        view.setOnClickListener(this.getCustomerClick(position, data));
        return view;
    }

    private View.OnClickListener getCustomerClick(final int position, final Data_customer customer) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.onCustomerOnClick(position,customer);
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
        @BindView(R.id.finishcard)
        ImageView finishcard;
        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public static String doubleToStringNoDecimal(float d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);;
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }


}
