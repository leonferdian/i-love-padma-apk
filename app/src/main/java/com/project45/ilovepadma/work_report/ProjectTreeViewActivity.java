package com.project45.ilovepadma.work_report;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;
import com.project45.ilovepadma.R;
import com.project45.ilovepadma.data.Data_work_report;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.lang.reflect.Member;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectTreeViewActivity extends AppCompatActivity implements TreeViewListener {

    @BindView(R.id.containerTree)
    LinearLayout mContainerTree;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBarLoad)
    ProgressBar mProgressBarLoad;


    private Member mMember;

    private AndroidTreeView mTreeView;
    private TreeNode root = TreeNode.root();
    private TreeViewListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_project_tree_view);
        ButterKnife.bind(this);
        mListener = (TreeViewListener) this;
    }

    @Override
    public void onTreeClick(Data_work_report wr) {
        Toast.makeText(getApplicationContext(), "ss", Toast.LENGTH_SHORT).show();
    }

    public static class ProjectTreeHolder extends TreeNode.BaseNodeViewHolder<Data_work_report> {
        TextView textTitle,textTitle2;
        IconTextView textIconArrow;
        IconTextView textIcon;
        IconTextView iconMove;
        IconTextView imLock;
        TextView textCount;
        RelativeLayout mBack;
        TreeViewListener mListener;

        public ProjectTreeHolder(Context context, TreeViewListener mListener ) {
            super(context);
            this.mListener = mListener;
        }

        @Override
        public View createNodeView(TreeNode node, final Data_work_report value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.segment_tree_project2, null, false);
            textTitle = (TextView) view.findViewById(R.id.text_title);
            textTitle2 = (TextView) view.findViewById(R.id.text_title2);
            textIconArrow = (IconTextView) view.findViewById(R.id.icon_arrow);
            textIcon = (IconTextView) view.findViewById(R.id.icon);
            iconMove = (IconTextView) view.findViewById(R.id.move);
            textCount = (TextView)view.findViewById(R.id.text_count);
            mBack = (RelativeLayout) view.findViewById(R.id.backSub);
            imLock=(IconTextView)view.findViewById(R.id.lock);

            mBack.setBackgroundColor(ContextCompat.getColor(context, R.color.page_join_visit));
            textCount.setText(""+value.getjml_report());
            textCount.setVisibility(View.VISIBLE);
            //imLock.setText("{fa-unlock}");
            textTitle.setText(value.gettahun_wr()+" Week "+value.getminggu_wr());
            textTitle2.setText(" ( "+value.getdurasi()+" )");

            textIcon.setText("{fa-list-alt}");


            return view;
        }

        @Override
        public void toggle(boolean active) {
            textIconArrow.setText(active ? "{fa-chevron-down}" : "{fa-chevron-right}");
            super.toggle(active);
        }
    }


    public static class ProjectSubHolder extends TreeNode.BaseNodeViewHolder<Data_work_report> {
        TextView task_name;
        TextView task_date;
        TextView task_durasi;
        TreeViewListener mListener;

        public ProjectSubHolder(Context context, TreeViewListener mListener ) {
            super(context);
            this.mListener = mListener;
        }

        @Override
        public View createNodeView(TreeNode node, final Data_work_report value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.item_work_report_perdate, null, false);
            task_name = (TextView) view.findViewById(R.id.task_name);
            task_name.setTypeface(null, Typeface.BOLD);
            task_date = (TextView) view.findViewById(R.id.task_date);
            task_durasi = (TextView) view.findViewById(R.id.task_durasi);

            task_name.setText(value.getdate_create());
            task_date.setText(value.getjml_report()+" report ");
            task_durasi.setText(value.getdurasi());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onTreeClick(value);
                    }
                }
            });

            return view;
        }

        @Override
        public void toggle(boolean active) {
        }


    }
}
