package it.quip.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import it.quip.android.R;

public class CreateCircleFragment extends Fragment {

    private EditText etCircleName;

    public static CreateCircleFragment newInstance() {
        return new CreateCircleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_circle, container, false);
        etCircleName = (EditText) v.findViewById(R.id.et_circle_name);
        return v;
    }

    public String getCircleName() {
        return etCircleName.getText().toString();
    }

}
