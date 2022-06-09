package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_join_visit;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_list_timeline extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;

    private List<Data_join_visit> items;

    private ListJV listener;

    private int[] jml_klik1;
    private int[] jml_klik2;

    public adapter_list_timeline(Activity activity, List<Data_join_visit> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (ListJV) activity;
        this.context = activity;


    }

    public List<Data_join_visit> getSurat() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_preseller, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }


        jml_klik1 = new int[items.size()];
        jml_klik2 = new int[items.size()];
        jml_klik2[position]=0;

        Data_join_visit data = items.get(position);

        holder.txt_sales_name.setText(data.getnama_salesman()+" Berkunjung ke ");
        holder.cart_prtitle.setText(data.getnama_customer());
        holder.cart_prtitle.setBackgroundColor(Color.parseColor("#07000000")); //bg transparan
        if(data.gettoko_tutup().equals("yes")){
            holder.cart_prtitle.setBackgroundResource(R.color.yellow_color);
        }
        else {
            if (data.getout_arena().equals("yes")) {
                holder.cart_prtitle.setBackgroundResource(R.color.maroon);
            }
            else{
                holder.cart_prtitle.setBackgroundColor(Color.parseColor("#07000000")); //bg transparan
            }
        }
        holder.txt_list_tgl.setText(data.getdate_create());
        holder.cart_prprice.setText(data.getalamat_cust());
        holder.txt_foto_display.setText(data.getvol_tot_order());
        holder.txt_brg_dijual.setText(data.getjml_brg_dijual());
        holder.txt_urutan_st.setText(data.geturutas_st());
        holder.txt_kunjungan_ke.setText(data.getkunjugan_ke());
        holder.txt_hot_name.setText("HOT : "+data.getnama_hot()+" - Depo : "+data.getdepo());
        holder.txt_comment.setText(data.getjml_komen()+" komen");
        holder.txt_like.setText(data.getjml_like()+" like");

        if(data.getstatus_like().equals("1")){
            holder.image_like.setImageResource(R.drawable.like_icon_active);
        }
        else{
            holder.image_like.setImageResource(R.drawable.like_icon);
        }

        if(!data.getjv_foto().equals("")) {
            final String bm = Server.URL2 + "image_upload_sfa/list_foto_outlet/" + data.getjv_foto();
            Picasso.with(holder.image_cartlist.getContext())
                    .load(bm)
                    .fit()
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    //.resize(100, 100)
                    //.onlyScaleDown()
                    .centerCrop()
                    .into(holder.image_cartlist);
        }

        holder.tl_layout.setBackgroundResource(R.color.dot_light_screen2);
        if(data.getnilai_klik().equals("0")){
            holder.tl_layout.setBackgroundColor(Color.TRANSPARENT);
            Log.d("pos", position+" - "+ String.valueOf(data.getnilai_klik()));
        }
        else{
            holder.tl_layout.setBackgroundResource(R.color.dot_light_screen2);
            Log.d("pos", position+" - "+ String.valueOf(data.getnilai_klik()));
        }

        holder.ll_koment.setOnClickListener(this.getCommentClick(position, data));

        holder.ll_like.setOnClickListener(this.getLikeClick(position, data,holder));

        view.setOnClickListener(this.getSuratClick(position, data));

        view.setOnLongClickListener(this.getSuratClick2(position, data,holder));

        return view;
    }

    private View.OnClickListener getCommentClick(final int position, final Data_join_visit st) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onCommentClick(position, st);

                }
            }
        };
    }

    private View.OnClickListener getLikeClick(final int position, final Data_join_visit st , final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onLikeClick(position, st);
                    //holder.image_like.setImageResource(R.drawable.like_icon_active);

                }
            }
        };
    }

    private View.OnClickListener getSuratClick(final int position, final Data_join_visit st) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    jml_klik1[position]++;
                    if(jml_klik1[position]==2) {
                        listener.onSuratOnClick(position, st);
                        jml_klik1[position]=0;
                    }


                    Log.d("klik", position+" - "+ String.valueOf(jml_klik1[position]));
                }
            }
        };
    }

    private View.OnLongClickListener getSuratClick2(final int position, final Data_join_visit st, final ViewHolder holder) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (listener != null) {

                    if(st.getnilai_klik().equals("0")) {
                        listener.onSuratOnClick2(position, st);
                        //jml_klik2[position] = 1;
                        st.setnilai_klik("1");
                        holder.tl_layout.setBackgroundResource(R.color.dot_light_screen2);
                        Log.d("klik", position+" - "+ String.valueOf(st.getnilai_klik()));
                        jml_klik1[position]=0;
                    }
                    else if(st.getnilai_klik().equals("1")){
                        listener.onSuratOnClick2(position, st);
                        //jml_klik2[position] = 0;
                        st.setnilai_klik("0");
                        holder.tl_layout.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("klik", position+" - "+ String.valueOf(st.getnilai_klik()));
                        jml_klik1[position]=0;
                    }
                    //jml_klik2[position]++;
                }
                else{
                    if(jml_klik2[position]==0) {
                        holder.tl_layout.setBackgroundResource(R.color.dot_light_screen2);
                    }
                    else if(jml_klik2[position]==1){
                        holder.tl_layout.setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                return false;
            }
        };
    }

    static class ViewHolder {


        @BindView(R.id.cart_prtitle)
        TextView cart_prtitle;
        @BindView(R.id.txt_list_tgl)
        TextView txt_list_tgl;
        @BindView(R.id.cart_prprice)
        TextView cart_prprice;
        @BindView(R.id.txt_foto_display)
        TextView txt_foto_display;
        @BindView(R.id.txt_sales_name)
        TextView txt_sales_name;
        @BindView(R.id.txt_brg_dijual)
        TextView txt_brg_dijual;
        @BindView(R.id.txt_hot_name)
        TextView txt_hot_name;
        @BindView(R.id.tl_layout)
        LinearLayout tl_layout;
        @BindView(R.id.image_cartlist)
        ImageView image_cartlist;
        @BindView(R.id.ll_koment)
        LinearLayout ll_koment;
        @BindView(R.id.txt_comment)
        TextView txt_comment;
        @BindView(R.id.ll_like)
        LinearLayout ll_like;
        @BindView(R.id.image_like)
        ImageView image_like;
        @BindView(R.id.txt_like)
        TextView txt_like;
        @BindView(R.id.txt_urutan_st)
        TextView txt_urutan_st;
        @BindView(R.id.txt_kunjungan_ke)
        TextView txt_kunjungan_ke;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ListJV {
        void onSuratOnClick(int position, Data_join_visit berita);
        void onSuratOnClick2(int position, Data_join_visit berita);
        void onCommentClick(int position, Data_join_visit berita);
        void onLikeClick(int position, Data_join_visit berita);
    }
}
