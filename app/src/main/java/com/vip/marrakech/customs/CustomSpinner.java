package com.vip.marrakech.customs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.vip.marrakech.R;


public class CustomSpinner extends androidx.appcompat.widget.AppCompatTextView {

    private BaseAdapter aAdapter;
   // private OnSpinnerValueSelectListener aListener;
    private AlertDialog dialog;
    private String aTitle;
    private int parentWidth, parentHeight;
    private PopupWindow popupWindow;

   /* private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> aAdapterView, View aView, int i, long aL) {
            if (aListener != null) {
                aListener.onSpinnerSelect((DMPatientInfo) aAdapter.getItem(i), i);
                popupWindow.dismiss();
            }
        }
    };*/


    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
        }

    }
    public void clear() {
        setText("");
    }

    public void select() {
        showDropDown(this);
    }


    public void setAdapter(BaseAdapter aAdapter) {
        this.aAdapter = aAdapter;
    }

    public void openSpinner() {
        AlertDialog.Builder dialog_view = new AlertDialog.Builder(getContext());

        View lView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, null);
        dialog_view.setView(lView);

        ListView listView = (ListView) lView.findViewById(R.id.listView);
        TextView tv_title = (TextView) lView.findViewById(R.id.tv_title);
        if (aTitle != null)
            tv_title.setText(aTitle);

        if (aAdapter != null)
            listView.setAdapter(aAdapter);
        else {
            Log.e("ERROR:::", "ADAPTER NULL");
        }

        // set the item click listener
       // listView.setOnItemClickListener(listener);

        dialog = dialog_view.create();
        dialog.show();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(0, 0);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setIcon(int id) {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0);
    }

    public void setTitle(String aTitle) {
        this.aTitle = aTitle;
    }
/*

    public void setOnSpinnerValueSelectListener(OnSpinnerValueSelectListener aListener) {
        this.aListener = aListener;
    }
*/

  /*  public interface OnSpinnerValueSelectListener {
        void onSpinnerSelect(DMPatientInfo patientInfo, int position);
    }
*/
    private void showDropDown(View v) {
        // initialize a pop up window type
        popupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        View lView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, null);
        ListView listView = (ListView) lView.findViewById(R.id.listView);
        TextView tv_title = (TextView) lView.findViewById(R.id.tv_title);
        tv_title.setVisibility(GONE);
        if (aTitle != null)
            tv_title.setText(aTitle);

        if (aAdapter != null)
            listView.setAdapter(aAdapter);
        else {
            Log.e("ERROR:::", "ADAPTER NULL");
        }
        // set the item click listener
        //listView.setOnItemClickListener(listener);
        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(getMeasuredWidth());
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(4);
        }
        popupWindow.setContentView(lView);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(parentWidth);
        // set the list view as pop up window content
        Rect location = locateView(v);
        popupWindow.showAtLocation(v, Gravity.TOP|Gravity.LEFT, location.left, location.bottom);
        popupWindow.showAsDropDown(v);
    }

    public static Rect locateView(View v){
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }
}