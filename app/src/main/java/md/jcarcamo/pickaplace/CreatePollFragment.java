package md.jcarcamo.pickaplace;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.jcarcamo.pickaplace.utils.FacebookUser;
import md.jcarcamo.pickaplace.utils.FriendsGraphResponse;
import md.jcarcamo.pickaplace.utils.ViewPagerAdapter;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreatePollFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreatePollFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePollFragment extends Fragment {
    private static final String TAG = "CreatePollFragment";

    public static final int GET_LOCATION_REQUEST_CODE = 1;


    public static List<FacebookUser> friends;
    public List<FacebookUser> selectedFriends;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.location)
    TextView locationTextView;

    @BindView(R.id.selectedFriendsTextView)
    TextView selectedFriendsTv;

    @BindView(R.id.startPollButton)
    Button startPoll;

    protected void getFriends(){
        AccessToken token = AccessToken.getCurrentAccessToken();
        /* make the API call */
        GraphRequest gr = new GraphRequest(
                token,
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                            /* handle the result */
                        Log.d(TAG, "friends:success "+response.getRawResponse());
                        try {
                            ObjectMapper om = new ObjectMapper();
                            FriendsGraphResponse friendsResponse =
                                    om.readValue(response
                                            .getRawResponse(), FriendsGraphResponse.class
                                    );
                            friends = friendsResponse.getData();
                            notifyFriendsListChange();
                            //mListener.onComplete();

                            for (FacebookUser fbUser : friendsResponse.getData()){
                                Log.d(TAG, "jsonparse:success:name "+fbUser.getName());
                                Log.d(TAG, "jsonparse:success:id "+fbUser.getId());
                            }


                        }catch (IOException ioe){
                            Log.d(TAG, "jsonparse:IO Exception"+ioe.getMessage());
                        }

                    }
                }
        );
        gr.executeAsync();
    }

    public CreatePollFragment() {
        // Required empty public constructor
        friends = new ArrayList<>();
        this.getFriends();
        selectedFriends = new ArrayList<>();
    }

    public void notifyFriendsListChange(){
        FacebookFriendsFragment friendsFragment = (FacebookFriendsFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment);
        friendsFragment.notifyDataSetChanged(CreatePollFragment.friends);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreatePollFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePollFragment newInstance() {
        CreatePollFragment fragment = new CreatePollFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_poll, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCreatePollInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.location)
    public void getStartingLocation(){
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            startActivityForResult(intent, GET_LOCATION_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case(GET_LOCATION_REQUEST_CODE):{
                switch (resultCode){
                    case(RESULT_OK):{
                        Place pl = PlaceAutocomplete.getPlace(getActivity(), data);
                        locationTextView.setText(pl.getName());
                        Log.i(TAG, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());
                        break;
                    }
                    case(PlaceAutocomplete.RESULT_ERROR):{
                        Status stat = PlaceAutocomplete.getStatus(getActivity(), data);
                        Log.d(TAG, "onActivityResult: " + stat.getStatusMessage());
                        break;
                    }
                    case(RESULT_CANCELED):{
                        Log.i(TAG, "onActivityResult: Cancelled by the user");
                        break;
                    }
                    default:{
                        Log.i(TAG, "onActivityResult: Unknown resultCode");
                        break;
                    }
                }
                break;

            }
            default:{
                Log.i(TAG, "onActivityResult: Unknown Request Code");
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @OnClick(R.id.startPollButton)
    public void startPoll(){
        Log.d(TAG,"Here I'll save to firebase");
    }

    public void onFriendClick(FacebookUser friend) {
        Log.d(TAG,"friend selected " + friend.getId());
        boolean isFriendAlreadySelected = selectedFriends.contains(friend);
        if(!isFriendAlreadySelected){
            selectedFriends.add(friend);
            if (selectedFriendsTv.getText().equals("Select Some Friends")){
                selectedFriendsTv.setText(friend.getName());
            }else{
                selectedFriendsTv.setText(selectedFriendsTv.getText() + ", " + friend.getName());
            }

        }else{
            selectedFriends.remove(friend);
            StringBuilder sb = new StringBuilder();
            for(FacebookUser currentFriend : selectedFriends ){
                sb.append(currentFriend.getName()).append(",");
            }
            if (sb.length() > 1){
                sb.setLength(sb.length() - 1);
            }else{
                sb.append("Select Some Friends");
            }

            selectedFriendsTv.setText(sb.toString());
        }

        //Intent result = new Intent();
        //Parcelable parcel = Parcels.wrap(item);
        //result.putExtra("LLOOKUP", parcel);
        //setResult(RESULT_OK, result);
        //finish();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreatePollInteraction(Uri uri);
        public abstract void onComplete();
    }

}
