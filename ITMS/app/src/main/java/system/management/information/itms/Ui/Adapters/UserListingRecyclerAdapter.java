package system.management.information.itms.Ui.Adapters;

/**
 * Created by Janescience on 11/24/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import system.management.information.itms.R;
import system.management.information.itms.Models.User;

import java.util.List;

public class UserListingRecyclerAdapter  extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
    private List<User> mUsers;
    private Context mContext;

    public UserListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        mContext = holder.txtUserAlphabet.getContext();

        if (user.name != null){
            String alphabet = user.image;

            holder.txtUsername.setText(user.name);
            Picasso.with(mContext).load(Uri.parse(alphabet)).into(holder.txtUserAlphabet);
        }
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView txtUserAlphabet;
        private TextView txtUsername;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (CircleImageView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
        }
    }
}
