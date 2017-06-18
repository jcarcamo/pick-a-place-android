package md.jcarcamo.pickaplace;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import md.jcarcamo.pickaplace.FacebookFriendsFragment.OnListFragmentInteractionListener;
import md.jcarcamo.pickaplace.dummy.DummyContent.DummyItem;
import md.jcarcamo.pickaplace.utils.FacebookUser;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFacebookFriendsRecyclerViewAdapter extends RecyclerView.Adapter<MyFacebookFriendsRecyclerViewAdapter.ViewHolder> {

    private SparseBooleanArray selectedItems;
    private List<FacebookUser> mValues;
    private final OnListFragmentInteractionListener mListener;

    public void updateValues(List<FacebookUser> values){
        mValues = values;
    }

    public MyFacebookFriendsRecyclerViewAdapter(List<FacebookUser> friends, OnListFragmentInteractionListener listener) {
        mValues = friends;
        mListener = listener;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_facebookfriends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).getId());
        holder.mContentView.setText(mValues.get(position).getName());
        holder.mView.setSelected(selectedItems.get(position, false));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFriendClick(holder.mItem);
                }
                // Save the selected positions to the SparseBooleanArray
                if (selectedItems.get(holder.getAdapterPosition(), false)) {
                    selectedItems.delete(holder.getAdapterPosition());
                    holder.mView.setSelected(false);
                }
                else {
                    selectedItems.put(holder.getAdapterPosition(), true);
                    holder.mView.setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mContentView;
        public FacebookUser mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
