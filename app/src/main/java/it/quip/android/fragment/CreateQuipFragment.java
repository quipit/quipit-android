package it.quip.android.fragment;

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

import it.quip.android.R;
import it.quip.android.model.User;
import it.quip.android.view.RoundedImageTransform;

public class CreateQuipFragment extends Fragment {

    private User mUser;
    private LinearLayout mLlProfile;
    private ImageView mIvProfile;
    private TextView mTvUserName;
    private EditText mEtQuipBody;
    private TextView mTvQuipCount;

    public static CreateQuipFragment newInstance() {
        return new CreateQuipFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_quip, container, false);
        mLlProfile = (LinearLayout) v.findViewById(R.id.quip_create_profile);
        mIvProfile = (ImageView) mLlProfile.findViewById(R.id.iv_profile);
        try {
            Picasso.with(getContext())
                    .load("drawable/edgarjuarez.jpeg")
                    .transform(new RoundedImageTransform(10, 0))
                    .into(mIvProfile);

        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("TweetsAdapter", "Unable to load!");
        }

        mTvUserName = (TextView) mLlProfile.findViewById(R.id.tv_user_name);
        mTvUserName.setText(mUser.getName());

        mEtQuipBody = (EditText) v.findViewById(R.id.et_quip_create_body);

        mTvQuipCount = (TextView) v.findViewById(R.id.tv_quip_create_count);
        mTvQuipCount.setText("0");
        mTvQuipCount.addTextChangedListener(new TextWatcher() {
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

        return v;
    }



}
