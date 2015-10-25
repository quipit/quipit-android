package it.quip.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.model.Circle;
import it.quip.android.model.User;
import it.quip.android.service.ImagePickerService;

import static it.quip.android.util.StringUtils.isBlank;

public class CircleHeaderFragment extends Fragment {

    private static final String CIRCLE = "it.quip.android.CIRCLE";

    private static final int PICK_IMAGE_REQUEST = 1408;
    private static final int TAKE_CAMERA_IMAGE_REQUEST = 1556;

    private static final int BACKGROUND_IMAGE = 1239;
    private static final int AVATAR_IMAGE = 2871;

    private EditText etName;
    private TextView tvName;
    private TextView tvQuipsters;

    private ImageView ivAvatar;
    private ImageView ivBackground;
    private ImageView ivCamera;

    private Circle mCircle;

    private int mImageBeingEdited;
    private boolean mInEditingMode = false;

    private ImagePickerService mImagePicker;

    public static CircleHeaderFragment newInstance(Circle circle) {
        CircleHeaderFragment fragment = new CircleHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable(CIRCLE, circle);
        fragment.setArguments(args);
        return fragment;
    }

    public Circle getCircle() {
        return mCircle;
    }

    public void setEditing(boolean editing) {
        mInEditingMode = editing;
        updateEditingState();
    }

    public void addMember(User member) {
        mCircle.addMember(member);
        updateQuipsterCount();
    }

    public void removeMember(User member) {
        mCircle.removeMember(member);
        updateQuipsterCount();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImagePicker = new ImagePickerService(getString(R.string.app_name));
        mCircle = getArguments().getParcelable(CIRCLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_circle_header, container, false);
        setupViews(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateEditingState();
    }

    private void setupViews(View v) {
        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvName.setText(mCircle.getName());

        etName = (EditText) v.findViewById(R.id.et_name);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setCircleName();
                }
            }
        });

        tvQuipsters = (TextView) v.findViewById(R.id.tv_quipsters);
        updateQuipsterCount();

        ivCamera = (ImageView) v.findViewById(R.id.iv_camera_background);
        ivBackground = (ImageView) v.findViewById(R.id.iv_background);
        ivAvatar = (ImageView) v.findViewById(R.id.iv_avatar);
        loadAvatarImage(Picasso.with(getContext()).load(R.drawable.quipit));

        View vBackground = v.findViewById(R.id.header);
        vBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.clearFocus();
            }
        });
    }

    private void updateQuipsterCount() {
        int numQuipsters = mCircle.getMembers().size();
        String quipsters = String.format(getString(R.string.label_quipsters), numQuipsters);
        tvQuipsters.setText(quipsters);
    }

    private void launchImageSelect(int whichImageView) {
        mImageBeingEdited = whichImageView;
        startActivityForResult(mImagePicker.pickImage(), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (PICK_IMAGE_REQUEST == requestCode) {
                loadImage(mImagePicker.onImagePicked(data));
            } else if (TAKE_CAMERA_IMAGE_REQUEST == requestCode) {
                loadImage(mImagePicker.onImageTaken());
            }
        }
    }

    private void loadImage(Uri imageUri) {
        switch (mImageBeingEdited) {
            case AVATAR_IMAGE:
                loadAvatarImage(Picasso.with(getContext()).load(imageUri));
                break;
            case BACKGROUND_IMAGE:
                loadBackgroundImage(Picasso.with(getContext()).load(imageUri));
                break;
        }
    }

    private void loadBackgroundImage(RequestCreator imageRequest) {
        imageRequest
                .fit()
                .centerCrop()
                .into(ivBackground);
    }

    private void loadAvatarImage(RequestCreator imageRequest) {
        imageRequest
                .fit()
                .centerCrop()
                .transform(new CircleTransformation(4, Color.WHITE))
                .into(ivAvatar);
    }

    private void setCircleName() {
        String newName = etName.getText().toString();
        boolean hasValidNewName = !isBlank(newName);
        boolean hasExistingName = circleHasName();

        if (hasValidNewName) {
            mCircle.setName(newName);
            stopEditingName();
        } else if (hasExistingName) {
            stopEditingName();
        }
    }

    private void updateEditingState() {
        if (null == getView()) {
            return;
        }

        if (mInEditingMode) {
            showEditingGuides();
        } else {
            hideEditingGuides();
        }

        if (circleHasName()) {
            stopEditingName();
        } else {
            // If editing mode is turned off but the circle doesn't have a name, users should
            // be able to enter one (and then not edit it anymore)
            startEditingName();
        }
    }

    private void showEditingGuides() {
        ivCamera.setVisibility(View.VISIBLE);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImageSelect(BACKGROUND_IMAGE);
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImageSelect(AVATAR_IMAGE);
            }
        });

        tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startEditingName();
                return true;
            }
        });
    }

    private void hideEditingGuides() {
        // We don't hide the edit text here because if the user is in the middle of typing something
        // when the editing state is changed, we should still allow them to finish (but then not
        // allow subsequent edits).
        ivCamera.setVisibility(View.INVISIBLE);
        ivCamera.setOnClickListener(null);
        ivAvatar.setOnClickListener(null);
        tvName.setOnLongClickListener(null);
    }

    private void startEditingName() {
        tvName.setVisibility(View.INVISIBLE);
        etName.setVisibility(View.VISIBLE);
        etName.setText("");
        etName.requestFocus();
    }

    private void stopEditingName() {
        etName.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.VISIBLE);
        tvName.setText(mCircle.getName());
    }

    private boolean circleHasName() {
        return !isBlank(mCircle.getName());
    }

}
