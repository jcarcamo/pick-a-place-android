package md.jcarcamo.pickaplace;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import md.jcarcamo.pickaplace.PollInvitesFragment.OnListFragmentInteractionListener;
import md.jcarcamo.pickaplace.utils.FacebookUser;
import md.jcarcamo.pickaplace.utils.PollInvites;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PollInvites} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPollInvitesRecyclerViewAdapter extends RecyclerView.Adapter<MyPollInvitesRecyclerViewAdapter.ViewHolder> {

    private List<PollInvites> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPollInvitesRecyclerViewAdapter(List<PollInvites> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void updateValues(List<PollInvites> values){
        mValues = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pollinvites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.openPoll(holder.mItem);
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
        public final TextView mContentView;
        public PollInvites mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
