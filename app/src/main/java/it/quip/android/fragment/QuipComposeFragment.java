package it.quip.android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.model.User;

public class QuipComposeFragment extends Fragment {

    public interface OnComposeQuipListener {
        void onComposeQuip(String text, User source);
    }

    private LinearLayout mLlProfile;
    private ImageView mIvProfile;
    private TextView mTvUserName;
    private EditText mEtQuipBody;
    private TextView mTvQuipCount;
    private EditText mEtQuipSource;

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
        mLlProfile = (LinearLayout) v.findViewById(R.id.quip_create_profile);
        mIvProfile = (ImageView) mLlProfile.findViewById(R.id.iv_profile);
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

        mTvUserName = (TextView) mLlProfile.findViewById(R.id.tv_user_name);
        mTvUserName.setText(user.getName());

        mEtQuipBody = (EditText) v.findViewById(R.id.et_quip_create_body);
        mEtQuipBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvQuipCount.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTvQuipCount = (TextView) v.findViewById(R.id.tv_quip_create_count);
        mTvQuipCount.setText("0");

        mEtQuipSource = (EditText) v.findViewById(R.id.tv_quip_source_user_name);
        mEtQuipSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO: Send character back to activity...
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
