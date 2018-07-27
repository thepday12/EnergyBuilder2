package edv2.energybuilder.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.model.ObjectField;
import edv2.energybuilder.model.ValueAndDate;
import edv2.energybuilder.utils.MyUtils;
import edv2.energybuilder.utils.StringDateComparator;

public class ObjectFieldAdapter extends RecyclerView
        .Adapter<ObjectFieldAdapter
        .DataObjectHolder> {
    private List<ObjectField> mDataset;
    private Context mContext;
    private Activity mActivity;
    private UpdateFieldListener updateFieldListener;
    private Gson gson = new Gson();
    private String objectId;
    private JSONObject jsonObject;



    public ObjectFieldAdapter(Activity activity, JSONObject jsonObject, String objectId, List<ObjectField> dataset, UpdateFieldListener updateFieldListener) {
        mActivity = activity;
        this.mContext = mActivity.getBaseContext();
        this.updateFieldListener= updateFieldListener;
        this.jsonObject = jsonObject;
        this.objectId =objectId;
        mDataset = dataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDateField;
        MaterialSpinner spinnerField;
        EditText etField;
        RelativeLayout rlDate;
        View view;
        public CustomEtListener myCustomEditTextListener;

        public DataObjectHolder(View itemView,CustomEtListener myLis) {
            super(itemView);
            this.view = itemView;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            spinnerField = itemView.findViewById(R.id.spinnerField);
            etField = itemView.findViewById(R.id.etField);
            rlDate = itemView.findViewById(R.id.rlDate);
            tvDateField = itemView.findViewById(R.id.tvDateField);

            myCustomEditTextListener = myLis;
            etField.addTextChangedListener(myCustomEditTextListener);
        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_object_field, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view, new CustomEtListener());
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final ObjectField object = mDataset.get(position);

        holder.tvTitle.setText(object.getName());
        holder.tvTitle.setTextColor(Color.BLACK);
        holder.tvTitle.setOnClickListener(null);
        if(object.getControlType().equals("n")){


            holder.etField.setVisibility(View.VISIBLE);
            holder.spinnerField.setVisibility(View.INVISIBLE);
            holder.rlDate.setVisibility(View.INVISIBLE);
            holder.etField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(12);

            holder.etField.setFilters(filterArray);
            holder.tvTitle.setTextColor(Color.parseColor("#2196F3"));

            holder.etField.setText(object.getValue());



            holder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        JSONObject jsonObjectDetails = jsonObject.getJSONObject("object_details");
                        Iterator<String> keys = jsonObjectDetails.keys();
                        List<ValueAndDate> valueAndDates = new ArrayList<>();
                        ArrayList yVals = new ArrayList<>();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if(key.startsWith(objectId)){
                                JSONObject innerJObject = jsonObjectDetails.getJSONObject(key);
                                try{
                                    float value = Float.valueOf(innerJObject.getString(object.getKey()));
                                    String date = key.replace(objectId+"_","");
                                    valueAndDates.add(new ValueAndDate(date,value));
                                }catch (Exception e){

                                }
                            }

                        }

                        Collections.sort(valueAndDates, new StringDateComparator());
                        List<ValueAndDate> tmp = new ArrayList<>();
                        for(int i =0; i<valueAndDates.size();i++){
                            Float value = i*1f;
                            ValueAndDate valueAndDate = valueAndDates.get(i);
                            yVals.add(i,new Entry(value,valueAndDate.getValue()));
                            valueAndDate.setCompareValue(value);
                            tmp.add(valueAndDate);
                        }

                        if(yVals.size()>0) {
                            showChartDialog(tmp,yVals);
                        }

                    } catch (JSONException e) {

                    }
                }
            });

            holder.myCustomEditTextListener.updatePosition(position,holder.etField);

        }else if(object.getControlType().equals("t")){

            holder.etField.setVisibility(View.VISIBLE);
            holder.spinnerField.setVisibility(View.INVISIBLE);
            holder.rlDate.setVisibility(View.INVISIBLE);

            holder.etField.setInputType(InputType.TYPE_CLASS_TEXT);
            holder.etField.setText(object.getValue());

            holder.myCustomEditTextListener.updatePosition(position,holder.etField);

        }else if(object.getControlType().equals("d")){
            holder.etField.setVisibility(View.INVISIBLE);
            holder.spinnerField.setVisibility(View.INVISIBLE);
            holder.rlDate.setVisibility(View.VISIBLE);

            String value = object.getValue();
            if(value.isEmpty()){
                value = MyUtils.getCurrentDate();
            }
            holder.tvDateField.setText(value);
            holder.rlDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String value = object.getValue();
                    if(value.isEmpty()){
                        value= MyUtils.getCurrentDate();
                    }
                    String[] values = value.split("-");
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            mActivity, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            String value = i+"-"+MyUtils.formatDateNumber(i1+1)+"-"+MyUtils.formatDateNumber(i2);
                            holder.tvDateField.setText(value);
                            object.setValue(value);
                            int p  = getPosition(object,0);
                            mDataset.set(p,object);
                            updateFieldListener.changeValue(p,object);
                        }
                    }, Integer.valueOf(values[0]), Integer.valueOf(values[1])-1, Integer.valueOf(values[2]));
                    datePickerDialog.show();
                }
            });
        }else {
            holder.etField.setVisibility(View.INVISIBLE);
            holder.spinnerField.setVisibility(View.VISIBLE);
            holder.rlDate.setVisibility(View.INVISIBLE);
            String value = object.getValue();
            List<String> list = object.getList();
            holder.spinnerField.setItems(object.getList());
            for(int i=0;i<list.size();i++){
                if(list.get(i).equals(value)){
                    holder.spinnerField.setSelectedIndex(i);
                    break;
                }
            }
            holder.spinnerField.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    object.setValue(item);
                    int p  = getPosition(object,position);
                    mDataset.set(p,object);
                    updateFieldListener.changeValue(p,object);
                }
            });
        }



//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, UpdateObjectActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(Global.EX_DATA,gson.toJson(mPoint));
//                intent.putExtra(Global.EX_ACTION,object.getType());
//                mContext.startActivity(intent);
//            }
//        });

    }

    private void showChartDialog(final List<ValueAndDate> dateList, ArrayList yVals) {
        // custom dialog
        final Dialog dialog = new Dialog(mActivity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chart);

        // set the custom dialog components - text, image and button
        LineChart lineChart =  dialog.findViewById(R.id.lineChart);


        Button btClose = dialog.findViewById(R.id.btClose);
        // if button is clicked, close the custom dialog
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
//        lineChart.setDesc("");
        lineChart.getXAxis().setSpaceMin(0.2f);
        lineChart.getXAxis().setSpaceMax(0.2f);
        lineChart.getAxisLeft().setSpaceTop(0.2f);
        lineChart.getAxisLeft().setSpaceBottom(0.2f);
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getLegend().setEnabled(false);
        lineChart.animateX(500);
        lineChart.getDescription().setEnabled(false);

        LineDataSet set = new LineDataSet(yVals, null);

        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String result = "";

//                for(ValueAndDate valueAndDate:dateList){
//                    if(valueAndDate.getCompareValue()==value){
//                        result =getShortDate(valueAndDate.getDate());
//                        break;
//                    }
//                }
                return getShortDate(dateList.get((int) value).getDate());
            }
        });


        LineData data = new LineData();
        data.addDataSet(set);

        lineChart.setData(data);
        lineChart.invalidate();
        dialog.show();
    }

    private String getShortDate(String date){
        String result = "";
        String[] arr = date.split("-");
        try {
            result = arr[1] + "/" + arr[2];
        }catch (Exception e){

        }
        return result;
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private int getPosition(ObjectField object,int position){
        String key = object.getKey();
        if(!key.isEmpty()) {
            for (int i = 0; i < mDataset.size(); i++) {
                if (key.equals(mDataset.get(i).getKey())) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    private class CustomEtListener implements TextWatcher {
        private int position = -1;
        private  EditText editText;

        /**
         * Updates the position according to onBindViewHolder
         *
         * @param position - position of the focused item
         */
        public void updatePosition(int position, EditText editText) {
            this.position = position;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editText!=null&&editText.hasFocus()){

                if (position > 0) {
                    // Change the value of array according to the position
                    ObjectField field = mDataset.get(position);
                    field.setValue(editable.toString());
                    mDataset.set(position, field);
                    updateFieldListener.changeValue(position, field);
                }
            }
        }
    }

}



