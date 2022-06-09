package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_note2 extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private List<Data_note> items;

    private NoteListener listener;

    public adapter_note2(Activity activity, List<Data_note> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (NoteListener) activity;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_note, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Data_note data = items.get(position);

        //holder.task_project_name.setTextColor(Color.parseColor("#cc3333"));
        holder.text_note_from.setText("From: "+data.getnama_user());
        holder.task_project_name.setText(data.getsubject_note());
        //holder.task_project_name.setTypeface(null, Typeface.BOLD);
        holder.txt_tgl_project.setText(String.valueOf(data.gettgl_note()));
        holder.task_jenis_project.setText(data.getjenis_note());
        holder.txt_jml_sub_project.setText(data.getnmr_tiket());
        holder.txt_status_tiket.setText(data.getstatus_note());
        holder.lay_jml_tiket.setOnClickListener(this.getJmlTiketClick(position, data));

        if(data.getstatus_note().equals("OPEN")){
            holder.lay_status_tiket.setBackgroundColor(Color.parseColor("#FFB7B7"));
        }
        else if(data.getstatus_note().equals("PROSES")){
            holder.lay_status_tiket.setBackgroundColor(Color.parseColor("#FFFF00"));
        }
        else if(data.getstatus_note().equals("FINISH")){
            holder.lay_status_tiket.setBackgroundColor(Color.parseColor("#B1B1FF"));
        }

        view.setOnClickListener(this.getBodyNoteClick(position, data));
        return view;

    }

    private View.OnClickListener getBodyNoteClick(final int position, final Data_note project) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onTiketClick(position, project);
                }
            }
        };
    }

    private View.OnClickListener getJmlTiketClick(final int position, final Data_note project) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onJmlTiketClick(position, project);

                }
            }
        };
    }

    static class ViewHolder {

        @BindView(R.id.text_note_from)
        TextView text_note_from;
        @BindView(R.id.task_project_name)
        TextView task_project_name;
        @BindView(R.id.txt_tgl_project) TextView txt_tgl_project;
        @BindView(R.id.task_jenis_project) TextView task_jenis_project;
        @BindView(R.id.txt_jml_sub_project) TextView txt_jml_sub_project;
        @BindView(R.id.lay_jml_tiket)
        LinearLayout lay_jml_tiket;
        @BindView(R.id.lay_status_tiket)
        LinearLayout lay_status_tiket;
        @BindView(R.id.txt_status_tiket) TextView txt_status_tiket;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface NoteListener {
        void onTiketClick(int position, Data_note note);
        void onJmlTiketClick(int position, Data_note note);
    }
}
