package net.eclissi.lucasop.ioboss.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import net.eclissi.lucasop.ioboss.R;

/**
 * Created by lucasoprana on 02/02/2017.
 */

public class EditNameDialogFragment extends DialogFragment implements View.OnClickListener{

    private EditText mEditText;

    public EditNameDialogFragment() {


    }

    public static EditNameDialogFragment newIstance(String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState){
        View view = inflater.inflate(R.layout.fragment_cv_detail,container);
        view.findViewById(R.id.radioLdwell).setOnClickListener(this);
        view.findViewById(R.id.radioLin).setOnClickListener(this);
        view.findViewById(R.id.radioLout).setOnClickListener(this);

        this.getDialog().setTitle("Edit geo Fence");

        return view;
    }

    @Override
    public void onClick(View v) {
        Log.i("Blue", "click radio");

    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedIstanceState){
        super.onViewCreated(view, savedIstanceState);

        mEditText = (EditText) view.findViewById(R.id.wrFenceName);
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }


}
