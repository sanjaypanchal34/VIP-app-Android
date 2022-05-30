package com.vip.marrakech.vendor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.vip.marrakech.R;

import adil.dev.lib.materialnumberpicker.adapter.NumberPickerAdapter;
import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;

public class MyNumberPicker extends Dialog implements NumberPickerAdapter.ItemClickCallBack, NumberPickerAdapter.ValueAvailableListener {

    Context mContext;
    int selectNumber = 0;
    int start = 0;
    int last = 0;
    NumberPickerDialog.NumberPickerCallBack callBack;
    private static MyNumberPicker instance = null;

    public MyNumberPicker(Context context, int start, int last, NumberPickerDialog.NumberPickerCallBack callBack) {
        super(context);
        if(start>last){
            throw new IllegalStateException("Start value must be smaller than last value");
        }
        this.callBack = callBack;
        this.mContext = context;
        this.selectNumber = start;
        this.start = start;
        this.last = last;
    }

    public static MyNumberPicker getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_number_picker_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        instance = this;
        initViews();
        initValues();
        initValuesInViews();
        setOnClickListener();
    }

    RecyclerView recyclerView;
    TextView okView, cancelView;
    EditText selectedTextView;

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        okView = (TextView) findViewById(R.id.ok);
        cancelView = (TextView) findViewById(R.id.cancel);
        selectedTextView = (EditText) findViewById(R.id.dialog_selected_value);
    }

    private void initValues() {

    }

    LinearLayoutManager linearLayoutManager;

    private void initValuesInViews() {
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (last - start <= -1)
            recyclerView.setItemViewCacheSize(100000);
        else
            recyclerView.setItemViewCacheSize(last - start);
        selectedTextView.setText(String.valueOf(start));
        selectNumber = start;

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(new NumberPickerAdapter(mContext, this, this, start, last));
    }

    private void setOnClickListener() {


        selectedTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Vibrator vb = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                try {
                    int value = Integer.parseInt(charSequence.toString());

                    if (value >= start && value <= last) {
                        ((NumberPickerAdapter) recyclerView.getAdapter()).findForItemToShow(value);
                    } else if (value < start) {
                        selectNumber = value;
//                        selectedTextView.setError("value can not be lower than " + start);
                        vb.vibrate(50);
                    } else if (value > last) {
                        selectNumber = value;
//                        selectedTextView.setError("value can not be higher than " + last);
                        vb.vibrate(50);
                    }
                } catch (NumberFormatException e) {
                    selectedTextView.setError(mContext.getString(R.string.MPD_empty_message));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTextView.getText().toString().isEmpty()||selectedTextView.getText().toString().equals("-")) {
                    selectedTextView.setError(mContext.getString(R.string.MPD_empty_message));
                } else if (selectNumber < start || selectNumber > last) {
                    selectedTextView.setError(mContext.getString(R.string.MPD_incorrect_message));
                } else {
                    callBack.onSelectingValue(selectNumber);
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onItemClicked(int selectedNumber, int position) {
        this.selectNumber = selectedNumber;
        selectedTextView.setText(String.valueOf(selectedNumber));
        selectedTextView.setError(null);
        linearLayoutManager.scrollToPositionWithOffset(position, 0);
    }

    @Override
    public void onValueAvailable(int value, int position) {
        this.selectNumber = value;
        linearLayoutManager.scrollToPositionWithOffset(position, 0);
    }

    public interface NumberPickerCallBack {
        public void onSelectingValue(int value);
    }
}