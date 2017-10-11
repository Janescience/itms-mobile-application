package system.management.information.itms;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    private Typeface Fonts;
    FirebaseAuth firebaseAuth;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_menu, container, false);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        Button logout = (Button) rootview.findViewById(R.id.btLogout);
        Button notify = (Button) rootview.findViewById(R.id.btNotify);

        notify.setTypeface(Fonts);
        logout.setTypeface(Fonts);

        firebaseAuth = FirebaseAuth.getInstance();

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new android.view.ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));


                builder.setTitle("LOGOUT");
                builder.setMessage("Are you sure you want to exit?");
                builder.setCancelable(false);
                builder.setIcon(android.R.drawable.ic_dialog_info);


                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //logging out the user
                        firebaseAuth.signOut();
                        //closing activity
                        getActivity().finish();
                        //starting login activity
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), LoginActivity.class));

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                Snackbar.make(view, "Confirm Logout", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Inflate the layout for this fragment
        return rootview;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_bottombar, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btNotify:
                fragment = new NotificationFragment();
                replaceFragment(fragment);
                break;
        }
    }
}
