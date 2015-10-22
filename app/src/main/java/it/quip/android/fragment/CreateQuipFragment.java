package it.quip.android.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import it.quip.android.R;
import it.quip.android.model.User;
import it.quip.android.view.RoundedImageTransform;

public class CreateQuipFragment extends DialogFragment {

    private static final String USER_KEY = "USER";

    private User mUser;
    private LinearLayout mLlProfile;
    private ImageView mIvProfile;
    private TextView mTvUserName;
    private EditText mEtQuipBody;
    private TextView mTvQuipCount;
    private Button mBtQuip;

    public static CreateQuipFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable(USER_KEY, user);

        CreateQuipFragment f = new CreateQuipFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_quip, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        mUser = getArguments().getParcelable(USER_KEY);
        mLlProfile = (LinearLayout) v.findViewById(R.id.quip_create_profile);
        mIvProfile = (ImageView) mLlProfile.findViewById(R.id.iv_profile);
        try {
            Picasso.with(getActivity())
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

        mBtQuip = (Button) v.findViewById(R.id.bt_quip_create_share);
        mBtQuip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Create quip...
                CreateQuipFragment.this.dismiss();
            }
        });
    }
}
