package it.quip.android.fragment;


public class FriendSearchListFragment extends FriendsSearchListFragment {

    public static FriendSearchListFragment newInstance() {
        return new FriendSearchListFragment();
    }

    @Override
    protected Integer getMaxSelectCount() {
        return 1;
    }

}
