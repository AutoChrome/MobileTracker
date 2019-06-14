package com.example.a10108309.phoneapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class FilterDialog extends DialogFragment {

    FilterDialogListener callback;

    private Button btn_submit;
    private EditText et_brand, et_device;

    public FilterDialog(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_dialog, container);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        et_device = dialog.findViewById(R.id.et_device);
        et_brand = dialog.findViewById(R.id.et_brand);

        if(getContext() instanceof NewsSearchActivity){
            et_brand.setVisibility(View.INVISIBLE);
            et_device.setHint("Search term");
        }

        btn_submit = dialog.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_device.getText().toString().length() < 1){
                    Toast.makeText(getActivity(), "You must provide a device name to continue.", Toast.LENGTH_SHORT).show();
                }else{
                    ArrayList<String> query = new ArrayList<String>();

                    query.add(et_device.getText().toString());
                    query.add(et_brand.getText().toString());

                    callback.onDialogDismiss(query);
                    getDialog().dismiss();
                }
            }
        });
    }

    public void setOnFragmentDismissListener(FilterDialogListener callback){
        this.callback = callback;
    }


    public interface FilterDialogListener{
        public void onDialogDismiss(ArrayList<String> query);
    }
}
