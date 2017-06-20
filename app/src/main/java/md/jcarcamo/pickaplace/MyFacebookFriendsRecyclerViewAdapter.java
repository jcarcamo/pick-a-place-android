package md.jcarcamo.pickaplace;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import md.jcarcamo.pickaplace.FacebookFriendsFragment.OnListFragmentInteractionListener;
import md.jcarcamo.pickaplace.dummy.DummyContent.DummyItem;
import md.jcarcamo.pickaplace.utils.FacebookUser;
import md.jcarcamo.pickaplace.utils.FriendsGraphResponse;

import java.io.IOException;
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

    public void clearSelected(){
        selectedItems.clear();
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
        holder.getFriendImage();

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
        public final ImageView mImageView;
        public FacebookUser mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.id);
        }

        protected void getFriendImage(){
            AccessToken token = AccessToken.getCurrentAccessToken();
        /* make the API call */
            GraphRequest gr = new GraphRequest(
                    token,
                    "/"+mItem.getId()+"/picture?height=48&redirect=false",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            /* handle the result */
                            try {
                                JSONObject data = response.getJSONObject().getJSONObject("data");

                                String url = data.getString("url");
                                Picasso.with(mView.getContext()).load(url).into(mImageView);
                                Log.d("FriendsViewHolder", "image:success");

                            }catch (JSONException ioe){
                                Log.d("FriendsViewHolder", "jsonparse:IO Exception"+ioe.getMessage());
                            }

                        }
                    }
            );
            gr.executeAsync();
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
