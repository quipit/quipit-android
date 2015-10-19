package it.quip.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.quip.android.R;
import it.quip.android.model.Circle;

public class CircleHeaderFragment extends Fragment {

    private static final String CIRCLE = "it.quip.android.CIRCLE";

    private Circle circle;

    public static CircleHeaderFragment newInstance(Circle circle) {
        CircleHeaderFragment fragment = new CircleHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable(CIRCLE, circle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circle = getArguments().getParcelable(CIRCLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_circle_header, container, false);
        setupViews(v);
        return v;
    }

    private void setupViews(View v) {
        TextView tvName = (TextView) v.findViewById(R.id.tv_name);
        tvName.setText(circle.getName());

        TextView tvQuipsters = (TextView) v.findViewById(R.id.tv_quipsters);
        tvQuipsters.setText(circle.getMembers().size() + " quipsters");
    }

}
