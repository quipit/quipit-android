package it.quip.android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.model.User;

public class QuipComposeFragment extends DialogFragment {

    public interface OnComposeQuipListener {
        void onComposeQuip(String text, User source);
    }

    private LinearLayout mLlProfile;
    private ImageView mIvProfile;
    private TextView mTvUserName;
    private EditText mEtQuipBody;
    private TextView mTvQuipCount;
    private Button mBtQuip;

    public static QuipComposeFragment newInstance() {
        return new QuipComposeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
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

        mBtQuip = (Button) v.findViewById(R.id.bt_quip_create_share);
        mBtQuip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompose();
            }
        });
    }

    private void onCompose() {
        ((OnComposeQuipListener) getActivity()).onComposeQuip(mEtQuipBody.getText().toString(), null);
    }

}
