package md.jcarcamo.pickaplace;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import md.jcarcamo.pickaplace.utils.DividerItemDecoration;
import md.jcarcamo.pickaplace.utils.PollInvites;
import md.jcarcamo.pickaplace.utils.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PollInvitesFragment extends Fragment {

    Query userPollsRef;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyPollInvitesRecyclerViewAdapter mAdapter;

    public static List<PollInvites> pollInvites;
    private OnListFragmentInteractionListener mListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PollInvitesFragment() {
        pollInvites = new ArrayList<>();
        userPollsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(AccessToken.getCurrentAccessToken().getUserId())
                .child("polls").limitToLast(30);
        userPollsRef.addValueEventListener(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> objectMap = (HashMap<String,Object>) dataSnapshot.getValue();
                if(objectMap != null) {
                    for (Object obj : objectMap.values()) {
                        if (obj instanceof Map) {
                            Map<String, Object> mapObj = (Map<String, Object>) obj;
                            PollInvites pollInvite = new PollInvites();
                            pollInvite.setTitle((String) mapObj.get("title"));
                            pollInvite.setId((String) mapObj.get("id"));
                            if(mapObj.get("position") != null) {
                                pollInvite.setPosition((Long) mapObj.get("position"));
                            }else{
                                pollInvite.setPosition(0);
                            }
                            if (!pollInvites.contains(pollInvite))
                                pollInvites.add(pollInvite);
                        }
                    }
                    mAdapter.updateValues(pollInvites);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PollInvitesFragment newInstance(int columnCount) {
        PollInvitesFragment fragment = new PollInvitesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        pollInvites.clear();
    }

    @Override
    public void onResume(){
        super.onResume();
        userPollsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(AccessToken.getCurrentAccessToken().getUserId())
                .child("polls").limitToLast(30);
        userPollsRef.addValueEventListener(valueEventListener);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pollinvites_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(), R.drawable.divider));
            mAdapter = new MyPollInvitesRecyclerViewAdapter(pollInvites, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void openPoll(PollInvites item);
    }
}
