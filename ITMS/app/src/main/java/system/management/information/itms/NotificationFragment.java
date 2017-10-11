package system.management.information.itms;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "NotificationFragment";
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        if (getActivity().getIntent().getExtras() != null) {
            for (String key : getActivity().getIntent().getExtras().keySet()) {
                Object value = getActivity().getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        rootView.findViewById(R.id.subscribeButton).setOnClickListener(this);
        rootView.findViewById(R.id.unsubscribeButton).setOnClickListener(this);
        rootView.findViewById(R.id.logTokenButton).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.subscribeButton:
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                Log.d(TAG, "SubscribeToTopic");
                Toast.makeText(getActivity(), "SubscribeToTopic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.unsubscribeButton:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                Log.d(TAG, "UnsubscribeFromTopic");
                Toast.makeText(getActivity(), "UnsubscribeFromTopic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logTokenButton:
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG, "Token : " + token);
                Toast.makeText(getActivity(), "Token : " + token, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
