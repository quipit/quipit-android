package it.quip.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import it.quip.android.repository.circle.CircleRepository;
import it.quip.android.repository.circle.CircleResponseHandler;
import it.quip.android.repository.circle.ParseCircleRepository;
import it.quip.android.service.ImagePickerService;

import static it.quip.android.util.StringUtils.isBlank;

public class CircleHeaderFragment extends Fragment {

    private static final String CIRCLE = "it.quip.android.CIRCLE";

    private static final int PICK_IMAGE_REQUEST = 1408;
    private static final int TAKE_CAMERA_IMAGE_REQUEST = 1556;

    private EditText etName;
    private TextView tvName;
    private TextView tvQuipsters;

    private ImageView ivAvatar;
    private ImageView ivBackground;
    private ImageView ivCamera;

    private Circle mCircle;
    private CircleRepository circlesRepo;

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

    public Bitmap getAvatar() {
        return getBitmap(ivAvatar);
    }

    public Bitmap getBackground() {
        return getBitmap(ivBackground);
    }

    private Bitmap getBitmap(ImageView imageView) {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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
        circlesRepo = new ParseCircleRepository();

        fetchCircle();
    }

    private void fetchCircle() {
        circlesRepo.getCircle(mCircle, new CircleResponseHandler() {
            @Override
            public void onSuccess(Circle circle) {
                // TODO: make an onFailure - this will fail if the circle is not found
                mCircle = circle;
                updateViewState();
            }
        });
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

    private void updateViewState() {
        updateQuipsterCount();

        String avatarUrl = mCircle.getAvatarImageURL();
        if (avatarUrl != null) {
            loadAvatarImage(Picasso.with(getContext()).load(avatarUrl));
        }

        String backgroundUrl = mCircle.getBackgroundImageURL();
        if (backgroundUrl != null) {
            loadBackgroundImage(Picasso.with(getContext()).load(backgroundUrl));
        }
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

    private void launchImageSelect(ImageView whichImageView) {
        mImageBeingEdited = whichImageView.getId();
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
            case R.id.iv_avatar:
                loadAvatarImage(Picasso.with(getContext()).load(imageUri));
                break;
            case R.id.iv_background:
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
                launchImageSelect(ivBackground);
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImageSelect(ivAvatar);
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
