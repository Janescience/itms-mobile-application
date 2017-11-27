package system.management.information.itms.Core.Users.GetAll;

import java.util.List;

import system.management.information.itms.Models.User;

/**
 * Created by Janescience on 11/23/2017.
 */

public class GetUserPresenter implements GetUserContract.Presenter, GetUserContract.OnGetAllUsersListener {
    private GetUserContract.View mView;
    private GetUserInteractor mGetUsersInteractor;

    public GetUserPresenter(GetUserContract.View view) {
        this.mView = view;
        mGetUsersInteractor = new GetUserInteractor(this);
    }

    @Override
    public void getAllUsers() {
        mGetUsersInteractor.getAllUsersFromFirebase();
    }

    @Override
    public void getChatUsers() {
        mGetUsersInteractor.getChatUsersFromFirebase();
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        mView.onGetAllUsersSuccess(users);
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        mView.onGetAllUsersFailure(message);
    }
}