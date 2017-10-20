package system.management.information.itms;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import static android.R.attr.value;

/**
 * Created by Janescience on 10/16/2017.
 */

public class MyIndexManualAdapter  extends RecyclerView.Adapter<MyIndexManualAdapter.MyViewHolder>{

    private int[] mImageset;


    public MyIndexManualAdapter( int[] myImageset) {
        mImageset = myImageset;
    }


    @Override
    public MyIndexManualAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_manual_row, parent, false);
        MyIndexManualAdapter.MyViewHolder vh = new MyIndexManualAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyIndexManualAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mImageset.length;
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public Integer indexPage,indexTopic;
        public TextView txtTopic;


        Typeface Fonts;




        public MyViewHolder(final View itemView) {
            super(itemView);


            Fonts = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Kanit-Light.ttf");

            cardView = (CardView) itemView.findViewById(R.id.card_view);

             itemView.findViewById(R.id.btnIndexEdit01).setOnClickListener(this);
             itemView.findViewById(R.id.btnIndexEdit02).setOnClickListener(this);
            txtTopic = (TextView) itemView.findViewById(R.id.txtTopic);

            txtTopic.setTypeface(Fonts);
        }

        @Override
        public void onClick(View v) {
            Fragment fragment = null;
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            switch (v.getId()) {
                case R.id.btnIndexEdit01:
                    indexPage=1;
                    indexTopic=1;
                    fragment = new IndexFragment();
                    passIndexSpinner(fragment,indexPage,indexTopic);
                    replaceFragment(fragment,activity);
                    break;
                case R.id.btnIndexEdit02:
                    indexPage=1;
                    indexTopic=2;
                    fragment = new IndexFragment();
                    passIndexSpinner(fragment,indexPage,indexTopic);
                    replaceFragment(fragment,activity);
                    break;
            }
        }

        public void passIndexSpinner(Fragment someFragment,Integer indexPage,Integer indexTopic){
            Bundle bundle = new Bundle();
            bundle.putInt("spinnerPage",indexPage);
            bundle.putInt("spinnerTopic",indexTopic);
            someFragment.setArguments(bundle);
        }

        public void replaceFragment(Fragment someFragment,AppCompatActivity activity) {
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_bottombar,someFragment).addToBackStack(null).commit();
        }
    }


}
