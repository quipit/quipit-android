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
import it.quip.android.service.ImagePickerService;

import static it.quip.android.util.StringUtils.isBlank;

public class CircleHeaderFragment extends Fragment {

    private static final String CIRCLE = "it.quip.android.CIRCLE";

    private static final int PICK_IMAGE_REQUEST = 1408;
    private static final int TAKE_CAMERA_IMAGE_REQUEST = 1556;

    private static final int BACKGROUND_IMAGE = 1239;
    private static final int AVATAR_IMAGE = 2871;

    private int imageBeingEdited;

    private EditText etName;
    private TextView tvName;

    private ImageView ivAvatar;
    private ImageView ivBackground;

    private Circle circle;
    private boolean isEditingName = false;

    private ImagePickerService imagePicker;

    public static CircleHeaderFragment newInstance(Circle circle) {
        CircleHeaderFragment fragment = new CircleHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable(CIRCLE, circle);
        fragment.setArguments(args);
        return fragment;
    }

    public Circle getCircle() {
        return circle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = new ImagePickerService(getString(R.string.app_name));
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
        TextView tvQuipsters = (TextView) v.findViewById(R.id.tv_quipsters);
        String quipsters = String.format(getString(R.string.label_quipsters), circle.getMembers().size());
        tvQuipsters.setText(quipsters);

        ivBackground = (ImageView) v.findViewById(R.id.iv_background);
        ivAvatar = (ImageView) v.findViewById(R.id.iv_avatar);
        loadAvatarImage(Picasso.with(getContext()).load(R.drawable.quipit));

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImageSelect(AVATAR_IMAGE);
            }
        });

        ImageView ivCamera = (ImageView) v.findViewById(R.id.iv_camera_background);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImageSelect(BACKGROUND_IMAGE);
            }
        });

        etName = (EditText) v.findViewById(R.id.et_name);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startEditingName();
                return true;
            }
        });

        if (circleHasName()) {
            stopEditingName();
        } else {
            startEditingName();
        }

        View vBackground = v.findViewById(R.id.header);
        vBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditingName) {
                    setCircleName();
                }
            }
        });
    }

    private void launchImageSelect(int whichImageView) {
        imageBeingEdited = whichImageView;
        startActivityForResult(imagePicker.pickImage(), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (PICK_IMAGE_REQUEST == requestCode) {
                onImagePicked(data);
            } else if (TAKE_CAMERA_IMAGE_REQUEST == requestCode) {
                onImageTaken();
            }
        }
    }

    private void onImagePicked(Intent data) {
        Uri pickedImage = imagePicker.onImagePicked(data);
        loadImage(pickedImage);
    }

    private void onImageTaken() {
        Uri takenImage = imagePicker.onImageTaken();
        loadImage(takenImage);
    }

    private void loadImage(Uri imageUri) {
        switch (imageBeingEdited) {
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
            circle.setName(newName);
            stopEditingName();
        } else if (hasExistingName) {
            stopEditingName();
        }
    }

    private void startEditingName() {
        isEditingName = true;
        tvName.setVisibility(View.INVISIBLE);
        etName.setVisibility(View.VISIBLE);
        etName.setText("");
        etName.requestFocus();
    }

    private void stopEditingName() {
        isEditingName = false;
        etName.setVisibility(View.INVISIBLE);
        tvName.setVisibility(View.VISIBLE);
        tvName.setText(circle.getName());
    }

    private boolean circleHasName() {
        return !isBlank(circle.getName());
    }

}
