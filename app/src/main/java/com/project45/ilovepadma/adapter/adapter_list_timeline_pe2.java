package com.project45.ilovepadma.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_join_visit;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.timeline.add_post_everything;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_list_timeline_pe2 extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;

    private List<Data_join_visit> items;

    private ListJV listener;

    private int[] jml_klik1;
    private int[] jml_klik2;

    private String id_user;

    public adapter_list_timeline_pe2(Activity activity, List<Data_join_visit> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (ListJV) activity;
        this.context = activity;

        this.id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);


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

    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        boolean convertViewWasNull = false;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_ilv2, parent, false);
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

        holder.txt_nama_user.setText(data.getnama_salesman());
        if(data.getjenis_file().equals("video")) {
            holder.txt_kategori.setText("include video");
            holder.txt_kategori.setTextColor(Color.parseColor("#1e8dee"));
        }
        else {
            if(!data.getjv_foto().equals("")) {
                holder.txt_kategori.setText("include Image");
                holder.txt_kategori.setTextColor(Color.parseColor("#0bd0b0"));
            }
            else {
                holder.txt_kategori.setText("Text Only");
                holder.txt_kategori.setTextColor(Color.parseColor("#cb0a2f"));
            }
        }

        if(data.getjenis_file().equals("non_video") && !data.getjv_foto().equals(""))
        {
            String img = Server.URL2 + "image_upload/list_foto_timeline/"+data.getjv_foto();
            holder.image_pe.setVisibility(View.VISIBLE);
            Picasso.with(holder.image_pe.getContext())
                    .load(img)
                    .fit()
                    .placeholder(R.drawable.default_photo)
                    //.resize(300, 100)
                    //.onlyScaleDown()
                    .centerCrop()
                    .into(holder.image_pe);
        }
        else{
            holder.image_pe.setVisibility(View.GONE);
        }
        //holder.cart_prtitle.setBackgroundColor(Color.parseColor("#07000000")); //bg transparan
        /*
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
        */
        holder.table_pertanyaan.removeAllViews();
        if(!data.getjenis_post().equals("other")){
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params2.setMargins(3, 1, 3, 1);
            params2.weight = 1;
            params2.span = 1;

            TableRow.LayoutParams params3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params3.setMargins(3, 1, 3, 1);
            params3.weight = 1;
            params3.span = 2;

            TableRow row = new TableRow(context);
            row.setBackgroundColor(Color.WHITE);

            TextView txt_pertanyaan = new TextView(context);
            txt_pertanyaan.setText("Jenis Post");
            txt_pertanyaan.setBackgroundResource(R.color.holo_blue_light);
            txt_pertanyaan.setTextColor(Color.WHITE);
            txt_pertanyaan.setTextSize(15);
            txt_pertanyaan.setGravity(Gravity.LEFT);
            txt_pertanyaan.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_pertanyaan, params2);

            final TextView txt_jawaban = new TextView(context);
            txt_jawaban.setText(data.getjenis_post());
            txt_jawaban.setBackgroundResource(R.color.holo_blue_light);
            txt_jawaban.setTextColor(Color.WHITE);
            txt_jawaban.setTextSize(15);
            txt_jawaban.setGravity(Gravity.LEFT);
            txt_jawaban.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_jawaban, params3);
            holder.table_pertanyaan.addView(row);

            for(int x = 0;x < data.getPertanyaan_post_everything().size();x++){
                if((data.getjenis_post().equals("soe_rr_kunjungan") || data.getjenis_post().equals("soe_mv_delivery")) && data.getPertanyaan_post_everything().get(x).getTipe_pertanyaan().equals("spinner")){
                    TableRow row2 = new TableRow(context);
                    row2.setBackgroundColor(Color.WHITE);

                    TextView txt_pertanyaan2 = new TextView(context);
                    txt_pertanyaan2.setText("- "+data.getPertanyaan_post_everything().get(x).getPertanyaan()+" :");
                    txt_pertanyaan2.setBackgroundResource(R.color.holo_blue_light);
                    txt_pertanyaan2.setTextColor(Color.WHITE);
                    txt_pertanyaan2.setTextSize(15);
                    txt_pertanyaan2.setGravity(Gravity.LEFT);
                    txt_pertanyaan2.setTypeface(null, Typeface.BOLD);
                    // add to row
                    row2.addView(txt_pertanyaan2, params3);
                    holder.table_pertanyaan.addView(row2);

                    TableRow row3 = new TableRow(context);
                    row3.setBackgroundColor(Color.WHITE);

                    final TextView txt_jawaban2 = new TextView(context);
                    txt_jawaban2.setText(data.getPertanyaan_post_everything().get(x).getJawaban());
                    txt_jawaban2.setBackgroundResource(R.color.holo_blue_light);
                    txt_jawaban2.setTextColor(Color.WHITE);
                    txt_jawaban2.setTextSize(15);
                    txt_jawaban2.setGravity(Gravity.LEFT);
                    txt_jawaban2.setTypeface(null, Typeface.BOLD);
                    // add to row
                    row3.addView(txt_jawaban2, params3);
                    holder.table_pertanyaan.addView(row3);
                }
                else {
                    TableRow row2 = new TableRow(context);
                    row2.setBackgroundColor(Color.WHITE);

                    TextView txt_pertanyaan2 = new TextView(context);
                    txt_pertanyaan2.setText(data.getPertanyaan_post_everything().get(x).getPertanyaan());
                    txt_pertanyaan2.setBackgroundResource(R.color.holo_blue_light);
                    txt_pertanyaan2.setTextColor(Color.WHITE);
                    txt_pertanyaan2.setTextSize(15);
                    txt_pertanyaan2.setGravity(Gravity.LEFT);
                    txt_pertanyaan2.setTypeface(null, Typeface.BOLD);
                    // add to row
                    row2.addView(txt_pertanyaan2, params2);

                    final TextView txt_jawaban2 = new TextView(context);
                    txt_jawaban2.setText(data.getPertanyaan_post_everything().get(x).getJawaban());
                    txt_jawaban2.setBackgroundResource(R.color.holo_blue_light);
                    txt_jawaban2.setTextColor(Color.WHITE);
                    txt_jawaban2.setTextSize(15);
                    txt_jawaban2.setGravity(Gravity.LEFT);
                    txt_jawaban2.setTypeface(null, Typeface.BOLD);
                    // add to row
                    row2.addView(txt_jawaban2, params3);
                    holder.table_pertanyaan.addView(row2);
                }
            }
        }
        else {
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params2.setMargins(3, 1, 3, 1);
            params2.weight = 1;
            params2.span = 1;

            TableRow.LayoutParams params3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            params3.setMargins(3, 1, 3, 1);
            params3.weight = 1;
            params3.span = 2;

            TableRow row = new TableRow(context);
            row.setBackgroundColor(Color.WHITE);

            TextView txt_pertanyaan = new TextView(context);
            txt_pertanyaan.setText("Jenis Post");
            txt_pertanyaan.setBackgroundResource(R.color.holo_blue_light);
            txt_pertanyaan.setTextColor(Color.WHITE);
            txt_pertanyaan.setTextSize(15);
            txt_pertanyaan.setGravity(Gravity.LEFT);
            txt_pertanyaan.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_pertanyaan, params2);

            final TextView txt_jawaban = new TextView(context);
            txt_jawaban.setText(data.getjenis_post());
            txt_jawaban.setBackgroundResource(R.color.holo_blue_light);
            txt_jawaban.setTextColor(Color.WHITE);
            txt_jawaban.setTextSize(15);
            txt_jawaban.setGravity(Gravity.LEFT);
            txt_jawaban.setTypeface(null, Typeface.BOLD);
            // add to row
            row.addView(txt_jawaban, params3);
            holder.table_pertanyaan.addView(row);
        }

        holder.txt_list_tgl.setText(data.getdate_create());
        holder.txt_isi_timeline.setText(Html.fromHtml(data.getvol_tot_order(),new URLImageParser(holder.txt_isi_timeline, context),null));
        holder.txt_comment.setText(data.getjml_komen()+" komen");
        holder.txt_like.setText(data.getjml_like()+" like");

        if(data.getstatus_like().equals("1")){
            holder.image_like.setImageResource(R.drawable.like_icon_active);
        }
        else{
            holder.image_like.setImageResource(R.drawable.like_icon);
        }

        if(!data.getdepo().equals("")) {
            final String bm = Server.URL2 + "image_upload/list_foto_pribadi/" + data.getdepo();
            Picasso.with(holder.image_user.getContext())
                    .load(bm)
                    .fit()
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    //.resize(100, 100)
                    //.onlyScaleDown()
                    .centerCrop()
                    .into(holder.image_user);
        }
        else{
            Picasso.with(holder.image_user.getContext())
                    .load(R.drawable.dummy_pic)
                    .fit()
                    .placeholder(R.drawable.ic_camera_alt_black_48dp)
                    //.resize(100, 100)
                    //.onlyScaleDown()
                    .centerCrop()
                    .into(holder.image_user);
        }

        /*
        holder.tl_layout.setBackgroundResource(R.color.dot_light_screen2);
        if(data.getnilai_klik().equals("0")){
            holder.tl_layout.setBackgroundColor(Color.TRANSPARENT);
            Log.d("pos", position+" - "+ String.valueOf(data.getnilai_klik()));
        }
        else{
            holder.tl_layout.setBackgroundResource(R.color.dot_light_screen2);
            Log.d("pos", position+" - "+ String.valueOf(data.getnilai_klik()));
        }

         */

        if(data.geturutas_st().equals(id_user)){
            holder.image_more_menu.setVisibility(View.VISIBLE);
            holder.image_more_menu.setOnClickListener(this.getMoreMenuClick(position, data,holder));
        }
        else{
            holder.image_more_menu.setVisibility(View.GONE);
        }


        holder.image_pe.setOnClickListener(this.getPictClick(position, data,holder));

        holder.ll_koment.setOnClickListener(this.getCommentClick(position, data));

        holder.ll_like.setOnClickListener(this.getLikeClick(position, data,holder));

        view.setOnClickListener(this.getSuratClick(position, data));

        view.setOnLongClickListener(this.getSuratClick2(position, data,holder));

        return view;
    }

    private View.OnClickListener getMoreMenuClick(final int position, final Data_join_visit st , final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onMoreMenuClick(position, st);
                    //holder.image_like.setImageResource(R.drawable.like_icon_active);

                }
            }
        };
    }

    private View.OnClickListener getPictClick(final int position, final Data_join_visit st , final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onPictClick(position, st);
                    //holder.image_like.setImageResource(R.drawable.like_icon_active);

                }
            }
        };
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
                        holder.tl_layout.setBackgroundColor(Color.WHITE);
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
                        holder.tl_layout.setBackgroundColor(Color.WHITE);
                    }
                }

                return false;
            }
        };
    }

    static class ViewHolder {

        @BindView(R.id.txt_nama_user)
        TextView txt_nama_user;

        @BindView(R.id.txt_kategori)
        TextView txt_kategori;

        @BindView(R.id.txt_list_tgl)
        TextView txt_list_tgl;

        @BindView(R.id.txt_isi_timeline)
        TextView txt_isi_timeline;

        @BindView(R.id.tl_layout)
        CardView tl_layout;

        @BindView(R.id.image_user)
        CircularImageView image_user;

        @BindView(R.id.image_pe)
        ImageView image_pe;

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
        @BindView(R.id.image_more_menu)
        ImageView image_more_menu;

        @BindView(R.id.table_pertanyaan)
        TableLayout table_pertanyaan;

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
        void onPictClick(int position, Data_join_visit berita);
        void onMoreMenuClick(int position, Data_join_visit berita);
    }
}
