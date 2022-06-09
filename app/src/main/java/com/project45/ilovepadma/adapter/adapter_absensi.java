package com.project45.ilovepadma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.absensi.list_absen;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_absensi extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int LAYOUT_HEADER= 0;
    private static final int LAYOUT_CHILD= 1;

    private JVListener listener;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<list_absen.ListItem> listItemArrayList;

    public adapter_absensi(Context context, ArrayList<list_absen.ListItem> listItemArrayList){

        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listItemArrayList = listItemArrayList;
        listener = (JVListener)context;
    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(listItemArrayList.get(position).isHeader()) {
            return LAYOUT_HEADER;
        }else
            return LAYOUT_CHILD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if(viewType==LAYOUT_HEADER){
            View view = inflater.inflate(R.layout.item_absen_header, parent, false);
            holder = new MyViewHolderHeader(view);
        }else {
            View view = inflater.inflate(R.layout.item_absen_body, parent, false);
            holder = new MyViewHolderChild(view,listener);
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder.getItemViewType()== LAYOUT_HEADER)
        {
            MyViewHolderHeader vaultItemHolder = (MyViewHolderHeader) holder;
            vaultItemHolder.txt_tgl.setText(listItemArrayList.get(position).gettime_check_in());
            vaultItemHolder.txt_hari.setText(listItemArrayList.get(position).gethari_check_in());
        }
        else {

            MyViewHolderChild vaultItemHolder = (MyViewHolderChild) holder;
            vaultItemHolder.txt_waktu.setText(listItemArrayList.get(position).gettime_check_in());
            vaultItemHolder.txt_alamat.setText(listItemArrayList.get(position).getaddress_check_in());

            if(listItemArrayList.get(position).gettime_check_out().equals(""))
            {
                vaultItemHolder.txt_check_in_out.setText(listItemArrayList.get(position).gettime_check_in());
            }
            else {
                vaultItemHolder.txt_check_in_out.setText(listItemArrayList.get(position).gettime_check_in() + " - " + listItemArrayList.get(position).gettime_check_out());
            }

            vaultItemHolder.btn_status.setText(listItemArrayList.get(position).getstatus_absensi());

            if(listItemArrayList.get(position).getfoto_absen().equals("")) {

                //String bm = Server.URL2 + "image_upload/list_foto_pribadi/" + listItemArrayList.get(position).getfoto_user();
                Picasso.with(context)
                        .load(R.drawable.dummy_pic)
                        //.fit()
                        .resize(100, 100)
                        .onlyScaleDown()
                        .centerCrop()
                        .into(vaultItemHolder.image_user);
            }
            else{

                String bm = Server.URL2 + "image_upload/list_foto_absen/" + listItemArrayList.get(position).getfoto_absen();
                Picasso.with(context)
                        .load(bm)
                        //.fit()
                        .resize(100, 100)
                        .onlyScaleDown()
                        .centerCrop()
                        .into(vaultItemHolder.image_user);

            }

            if(listItemArrayList.get(position).getstatus_absensi().equals("Not Completed")){
                vaultItemHolder.cv_child.setCardBackgroundColor(vaultItemHolder.cv_child.getResources().getColor(R.color.red_50));
                vaultItemHolder.btn_check_out.setVisibility(View.VISIBLE);
            }
            else{
                vaultItemHolder.cv_child.setCardBackgroundColor(vaultItemHolder.cv_child.getResources().getColor(R.color.green_50));
                vaultItemHolder.btn_check_out.setVisibility(View.GONE);
            }
        }
    }

    class MyViewHolderHeader extends RecyclerView.ViewHolder{

        TextView txt_tgl,txt_hari;

        public MyViewHolderHeader(View itemView) {
            super(itemView);

            txt_tgl = (TextView) itemView.findViewById(R.id.txt_tgl);
            txt_hari = (TextView) itemView.findViewById(R.id.txt_hari);
        }

    }

    class MyViewHolderChild extends RecyclerView.ViewHolder{

        TextView txt_alamat,txt_check_in_out,txt_waktu;
        Button btn_status,btn_check_out;
        CircularImageView image_user;
        CardView cv_child;

        public MyViewHolderChild(View itemView,final JVListener listener) {
            super(itemView);

            txt_waktu = (TextView) itemView.findViewById(R.id.txt_waktu);
            txt_alamat = (TextView) itemView.findViewById(R.id.txt_alamat);
            txt_check_in_out = (TextView) itemView.findViewById(R.id.txt_check_in_out);
            btn_status = (Button) itemView.findViewById(R.id.btn_status);
            btn_check_out = (Button) itemView.findViewById(R.id.btn_check_out);
            image_user = (CircularImageView) itemView.findViewById(R.id.image_user);
            cv_child = (CardView) itemView.findViewById(R.id.cv_child);

            btn_check_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e("ILP", "del click ");
                    if (listener != null) {
                        listener.onCheckOutClick(getPosition(), listItemArrayList);
                    }
                }
            });

            image_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e("ILP", "del click ");
                    if (listener != null) {
                        listener.onImageClick(getPosition(), listItemArrayList);
                    }
                }
            });
        }

    }

    public ArrayList<list_absen.ListItem> getAbsent() {
        //mPhotos.remove(0);
        return listItemArrayList;
    }

    public interface JVListener{
        void onCheckOutClick(int position, ArrayList<list_absen.ListItem> itemss);
        void onImageClick(int position, ArrayList<list_absen.ListItem> itemss);
    }
}
