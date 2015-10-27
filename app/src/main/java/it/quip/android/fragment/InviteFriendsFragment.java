package it.quip.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.adapter.UsersArrayAdapter;
import it.quip.android.model.User;
import it.quip.android.repository.user.UsersResponseHandler;
import it.quip.android.view.OverlayListView;

public class InviteFriendsFragment extends Fragment {

    public interface OnFriendsListChangedListener {
        void onFriendInvited(User friend);
        void onFriendUninvited(User friend);
    }

    private EditText etFriendName;

    private User mUser;
    private List<User> mFriends;

    private UsersArrayAdapter aFilteredFriends;

    private ArrayList<User> invitedFriends;
    private UsersArrayAdapter aInvitedFriends;

    private OnFriendsListChangedListener onFriendsListChangedListener;

    public static InviteFriendsFragment newInstance() {
        return new InviteFriendsFragment();
    }

    public void setOnFriendsListChangedListener(OnFriendsListChangedListener onFriendsListChangedListener) {
        this.onFriendsListChangedListener = onFriendsListChangedListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invite_friends, container, false);

        etFriendName = (EditText) v.findViewById(R.id.et_friend_name);

        OverlayListView friendsOverlay = (OverlayListView) v.findViewById(R.id.overlay_friends);
        friendsOverlay.setAdapter(aFilteredFriends);
        friendsOverlay.setOnItemSelectedListener(new OverlayListView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position, View view) {
                inviteFriend(aFilteredFriends.getItem(position));
            }
        });

        ListView lvInvitedFriends = (ListView) v.findViewById(R.id.lv_invited_friends);
        lvInvitedFriends.setAdapter(aInvitedFriends);

        setupFriendNameField();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = QuipitApplication.getCurrentUser();
        mFriends = new ArrayList<>();
        populateFriends();

        ArrayList<User> filteredFriends = new ArrayList<>();
        aFilteredFriends = new UsersArrayAdapter(getContext(), filteredFriends);

        invitedFriends = new ArrayList<>();
        aInvitedFriends = new UsersArrayAdapter(getContext(), invitedFriends);
        aInvitedFriends.setOnLongClickListener(new UsersArrayAdapter.OnLongClickListener<User>() {
            @Override
            public boolean onLongClick(int position, User user) {
                uninviteFriend(user);
                return true;
            }
        });
    }

    private void populateFriends() {
        QuipitApplication.getUserRepo().getFriends(mUser, new UsersResponseHandler() {
            @Override
            public void onSuccess(List<User> users) {
                mFriends.clear();
                mFriends.addAll(users);
            }
        });
    }

    private void inviteFriend(User friend) {
        aInvitedFriends.add(friend);

        etFriendName.setText("");
        aFilteredFriends.clear();

        if (onFriendsListChangedListener != null) {
            onFriendsListChangedListener.onFriendInvited(friend);
        }
    }

    private void uninviteFriend(User friend) {
        aInvitedFriends.remove(friend);
        if (onFriendsListChangedListener != null) {
            onFriendsListChangedListener.onFriendUninvited(friend);
        }
    }

    private void setupFriendNameField() {
        etFriendName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFriends(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchFriends(String name) {
        aFilteredFriends.clear();

        for (User friend : mFriends) {
            if (!"".equals(name)
                    && !myself(friend)
                    && hasFriendWithName(friend, name)
                    && !alreadyInvited(friend)) {
                aFilteredFriends.add(friend);
            }
        }
    }

    private boolean hasFriendWithName(User friend, String name) {
        return friend.getName().toLowerCase().contains(name.toLowerCase());
    }

    private boolean myself(User friend) {
        // TODO: We will need to do this with the object ids
        return mUser.getName().equals(friend.getName());
    }

    private boolean alreadyInvited(User friend) {
        return invitedFriends.contains(friend);
    }

}
