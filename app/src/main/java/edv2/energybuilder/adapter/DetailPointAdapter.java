package edv2.energybuilder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.model.DetailPoint;
import edv2.energybuilder.model.Point;
import edv2.energybuilder.utils.Global;
import edv2.energybuilder.view.UpdateObjectActivity;

public class DetailPointAdapter extends RecyclerView
        .Adapter<DetailPointAdapter
        .DataObjectHolder> {
    private List<DetailPoint> mDataset;
    private Point mPoint;
    private Context mContext;
    private Activity mActivity;
    private Gson gson = new Gson();

    public DetailPointAdapter(Activity activity,Point point) {
        mActivity = activity;
        this.mContext = mActivity.getBaseContext();
        mPoint = point;
        List<DetailPoint> detailPoints = new ArrayList<>();
        if(point.getFl().getTotal()>0){
            detailPoints.add(point.getFl());
        }if(point.getEu().getTotal()>0){
            detailPoints.add(point.getEu());
        }if(point.getTa().getTotal()>0){
            detailPoints.add(point.getTa());
        }if(point.getEq().getTotal()>0){
            detailPoints.add(point.getEq());
        }
        mDataset = detailPoints;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;
        View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);

        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_detail_point, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final DetailPoint object = mDataset.get(position);

        holder.tvName.setText(object.getName());
        holder.tvCount.setText(String.valueOf(object.getTotal()));



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateObjectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Global.EX_DATA,gson.toJson(mPoint));
                intent.putExtra(Global.EX_ACTION,object.getType());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

