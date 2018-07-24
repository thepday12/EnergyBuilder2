package edv2.energybuilder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.model.DetailPoint;
import edv2.energybuilder.model.Point;
import edv2.energybuilder.utils.Global;
import edv2.energybuilder.view.DeatailPointActivity;

public class PointsAdapter extends RecyclerView
        .Adapter<PointsAdapter
        .DataObjectHolder> {
    private List<Point> mDataset;
    private Context mContext;
    private Activity mActivity;
    private Gson gson = new Gson();

    public PointsAdapter(Activity activity, List<Point> dataset) {
        mActivity = activity;
        this.mContext = mActivity.getBaseContext();
        mDataset = dataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivComplete;
        View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            tvName = itemView.findViewById(R.id.tvName);
            ivComplete = itemView.findViewById(R.id.ivComplete);

        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_point, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final Point object = mDataset.get(position);

        holder.tvName.setText(object.getName());
        if(object.isComplete()) {
            holder.ivComplete.setImageResource(R.drawable.ic_check);
        }else{
            holder.ivComplete.setImageResource(R.drawable.ic_uncheck);
        }


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeatailPointActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Global.EX_DATA,gson.toJson(object));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

