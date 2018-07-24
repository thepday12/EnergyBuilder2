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


import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.model.Route;
import edv2.energybuilder.utils.Global;
import edv2.energybuilder.view.PointsActivity;

public class RoutesAdapter extends RecyclerView
        .Adapter<RoutesAdapter
        .DataObjectHolder> {
    private List<Route> mDataset;
    private Context mContext;
    private Activity mActivity;

    public RoutesAdapter(Activity activity, List<Route> dataset) {
        mActivity = activity;
        this.mContext = mActivity.getBaseContext();
        mDataset = dataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvTotal,tvPercent;
        View view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            tvName = itemView.findViewById(R.id.tvName);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvPercent = itemView.findViewById(R.id.tvPercent);

        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_route, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final Route object = mDataset.get(position);

        holder.tvName.setText(object.getName());
        holder.tvTotal.setText("Points: "+object.getTotal());
        int complete = object.getComplete();
        int percent = complete*100/object.getTotal();
        holder.tvPercent.setText("Complete: "+complete+" ("+percent+"%)");


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PointsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Global.EX_ID,object.getId());
                intent.putExtra(Global.EX_NAME,object.getName());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

