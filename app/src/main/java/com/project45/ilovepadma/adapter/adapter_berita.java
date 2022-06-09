package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_berita;
import com.project45.ilovepadma.global.Api;
import com.project45.ilovepadma.global.URLImageParser;
import com.project45.ilovepadma.notes.detail_note;
import com.project45.ilovepadma.util.Server;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_berita extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;

    private List<Data_berita> items;

    private ListJV listener;

    private String id_user;

    public adapter_berita(Activity activity, List<Data_berita> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (ListJV) activity;
        this.context = activity;

        this.id_user = Api.sharedpreferences.getString(Api.TAG_ID, null);


    }

    public List<Data_berita> getSurat() {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berita, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }




        Data_berita data = items.get(position);

        holder.judul_berita.setText(data.getjudul_berita());

        String img = Server.URL2 + "inc/berita/file_gambar/"+data.getgambar_berita();
        holder.image_berita.setVisibility(View.VISIBLE);
        Picasso.with(holder.image_berita.getContext())
                .load(img)
                .fit()
                .placeholder(R.drawable.default_photo)
                //.resize(300, 100)
                //.onlyScaleDown()
                //.centerCrop()
                .into(holder.image_berita);

        holder.tgl_berita.setText(data.gettgl_berita());
        //holder.isi_berita.setText(data.getisi_berita());
        holder.isi_berita.setText(Html.fromHtml(data.getisi_berita(),new URLImageParser(holder.isi_berita, context),null));


        view.setOnClickListener(this.getBeritaClick(position, data,holder));

        return view;
    }


    private View.OnClickListener getBeritaClick(final int position, final Data_berita st , final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onBeritaClick(position, st);

                }
            }
        };
    }


    static class ViewHolder {

        @BindView(R.id.judul_berita)
        TextView judul_berita;

        @BindView(R.id.tgl_berita)
        TextView tgl_berita;

        @BindView(R.id.isi_berita)
        TextView isi_berita;

        @BindView(R.id.image_berita)
        ImageView image_berita;

        @BindView(R.id.lyt_parent)
        LinearLayout lyt_parent;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ListJV {
        void onBeritaClick(int position, Data_berita berita);

    }
}
