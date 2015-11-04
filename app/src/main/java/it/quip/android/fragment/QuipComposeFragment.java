package it.quip.android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.activity.CreateQuipActivity;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.model.User;

public class QuipComposeFragment extends Fragment {

    public interface OnSearchFriend {
        void onSearchFriend();
    }

    private ImageView mIvProfile;
    private TextView mTvUserName;
    private EditText mEtQuipBody;
    private TextView mTvQuipSource;

    public static QuipComposeFragment newInstance() {
        return new QuipComposeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_quip_compose, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        User user = QuipitApplication.getCurrentUser();
        mIvProfile = (ImageView) v.findViewById(R.id.iv_profile);
        try {
            Picasso.with(getActivity())
                    .load(user.getImageUrl())
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransformation(4, Color.WHITE))
                    .into(mIvProfile);

        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("TweetsAdapter", "Unable to load!");
        }

        mTvUserName = (TextView) v.findViewById(R.id.tv_user_name);
        mTvUserName.setText(user.getName());

        mEtQuipBody = (EditText) v.findViewById(R.id.et_quip_create_body);

        mTvQuipSource = (TextView) v.findViewById(R.id.et_quip_create_source);
        mTvQuipSource.setOnClickListener(
                new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         ((CreateQuipActivity) getContext()).onSearchFriend();
                     }
                 }
        );
    }

    public String getBody() {
        return mEtQuipBody.getText().toString();
    }

    public void setSourceName(String body) {
        mTvQuipSource.clearFocus();
        mTvQuipSource.setText(body);
    }

}
