package system.management.information.itms.Core.Users.Add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Janescience on 11/24/2017.
 */

public interface AddUserContract {
    interface View {
        void onAddUserSuccess(String message);

        void onAddUserFailure(String message);
    }

    interface Presenter {
        void addUser(Context context, FirebaseUser firebaseUser);
    }

    interface Interactor {
        void addUserToDatabase(Context context, FirebaseUser firebaseUser);
    }

    interface OnUserDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
