package com.project45.ilovepadma.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_work_report;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class adapter_work_report_bydate extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;

    private List<Data_work_report> items;

    private ListWR listener;

    public adapter_work_report_bydate(Activity activity, List<Data_work_report> items) {
        //super(context, 0, new ArrayList<Data_customer>());
        this.activity = activity;
        this.items = items;
        listener = (ListWR) activity;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_line, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            convertViewWasNull = true;
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Data_work_report data = items.get(position);

        holder.txt_waktu.setText(data.getwaktu_mulai());
        holder.txt_durasi.setText(data.getdurasi());
        holder.txt_job_name.setText(data.getstatus_agenda());

        if(data.getstatus_agenda().equals("plan"))
        {
            holder.txt_job_name.setTextColor(holder.txt_job_name.getResources().getColor(R.color.yellow_color));
        }
        else if(data.getstatus_agenda().equals("plan succeed"))
        {
            holder.txt_job_name.setTextColor(holder.txt_job_name.getResources().getColor(R.color.green_color));
        }
        else if(data.getstatus_agenda().equals("plan not succeed"))
        {
            holder.txt_job_name.setTextColor(holder.txt_job_name.getResources().getColor(R.color.page_red));
        }
        else{
            holder.txt_job_name.setTextColor(holder.txt_job_name.getResources().getColor(R.color.blue_color));
        }

        holder.txt_detail_job.setText(Html.fromHtml(data.getketerangan()));

        holder.txt_job_name.setOnClickListener(this.getSuratClick(position, data));
        holder.txt_waktu.setOnClickListener(this.getSuratClick(position, data));
        holder.txt_detail_job.setOnClickListener(this.getSuratClick(position, data));

        view.setOnClickListener(this.getSuratClick(position, data));

        return view;
    }

    private View.OnClickListener getSuratClick(final int position, final Data_work_report st) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReportClick(position, st);
                }
            }
        };
    }



    static class ViewHolder {


        @BindView(R.id.txt_waktu)
        TextView txt_waktu;
        @BindView(R.id.txt_job_name)
        TextView txt_job_name;
        @BindView(R.id.txt_detail_job)
        TextView txt_detail_job;
        @BindView(R.id.txt_durasi)
        TextView txt_durasi;

        int ref;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ListWR {
        void onReportClick(int position, Data_work_report berita);
    }
}
