package com.example.custom2.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.example.custom2.R;

public class PassDialog extends DialogFragment {
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.pass_dialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        EditText text=view.findViewById(R.id.Pass);
        Bundle args=getArguments();
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        String pas=args.getString("pas");
        view.findViewById(R.id.PassNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.PassYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pas.equals(text.getText().toString())) {
                    dismiss();
                }
            }
        });
        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}

