package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_company;
import com.project45.ilovepadma.data.Data_join_visit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_project extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private List<Data_company> items;

    private ProjectListener listener;

    public adapter_project(Activity activity, List<Data_company> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (ProjectListener) activity;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Data_company data = items.get(position);

        holder.task_project_name.setTextColor(Color.parseColor("#cc3333"));
        holder.task_project_name.setText(data.getiInternalId());
        holder.task_project_name.setTypeface(null, Typeface.BOLD);
        holder.txt_tgl_project.setText(String.valueOf(data.getszDescription()));
        holder.task_jenis_project.setText(data.getiId());
        holder.txt_main_project.setText(data.getszId());
        holder.txt_main_project.setTextColor(Color.parseColor("#cc3333"));
        holder.txt_jml_sub_project.setText(data.getszName());
        holder.txt_status_project.setText(data.getstatus_project());
        holder.txt_status_project.setTextColor(Color.parseColor("#cc3333"));
        holder.txt_divisi_project.setText(data.getnama_divisi());
        holder.txt_divisi_project.setTextColor(Color.parseColor("#cc3333"));
        holder.txt_jml_task.setText(data.gettotal_selesai()+"/"+data.gettask_list());

        holder.txt_jml_sub_project.setOnClickListener(this.getSubProjectClick(position, data));
        holder.txt_jml_task.setOnClickListener(this.getTiketClick(position, data));

        view.setOnClickListener(this.getBodyProjectClick(position, data));
        return view;

    }

    private View.OnClickListener getBodyProjectClick(final int position, final Data_company project) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onProjectClick(position, project);
                }
            }
        };
    }

    private View.OnClickListener getSubProjectClick(final int position, final Data_company project) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onSubProjectClick(position, project);

                }
            }
        };
    }

    private View.OnClickListener getTiketClick(final int position, final Data_company project) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {

                    listener.onTiketClick(position, project);

                }
            }
        };
    }

    static class ViewHolder {

        @BindView(R.id.task_project_name)
        TextView task_project_name;
        @BindView(R.id.txt_tgl_project) TextView txt_tgl_project;
        @BindView(R.id.task_jenis_project) TextView task_jenis_project;
        @BindView(R.id.txt_main_project) TextView txt_main_project;
        @BindView(R.id.txt_jml_sub_project) TextView txt_jml_sub_project;
        @BindView(R.id.txt_status_project) TextView txt_status_project;
        @BindView(R.id.txt_divisi_project) TextView txt_divisi_project;
        @BindView(R.id.txt_jml_task) TextView txt_jml_task;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ProjectListener {
        void onProjectClick(int position, Data_company project);
        void onSubProjectClick(int position, Data_company project);
        void onTiketClick(int position, Data_company project);
    }
}
