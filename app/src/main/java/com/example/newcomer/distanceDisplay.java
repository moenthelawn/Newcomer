package com.example.newcomer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link distanceDisplay.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link distanceDisplay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class distanceDisplay extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2 ;
    private static final int RESULT_OK = 1 ;
    private EditText editText2;
    // TODO: Rename and change types of parameters
    private int mParam1;

    private EditText changeDistance;
    private OnFragmentInteractionListener mListener;

    public distanceDisplay() {
        // Required empty public constructor
    }
    public interface OnMessageReadListener{
        public void onMessageRead(String message);

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment distanceDisplay.
     */
    // TODO: Rename and change types and number of parameters
    public static distanceDisplay newInstance(int param1) {
        distanceDisplay fragment = new distanceDisplay();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_distance_display, container, false);
        Places.initialize(getContext(),"AIzaSyAjGcF4XC-OEVJHKPmPefDUxGjxiSCbFK8");
        PlacesClient placesClient = Places.createClient(getContext());

        changeDistance = inflate.findViewById(R.id.editText2);



        //Set the onclick listener for save changes
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            editText2 = inflate.findViewById(R.id.editText2);

            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Then send this back and we will update the distance display
                    int distance;
                    try{
                        distance = Integer.parseInt(s.toString().trim());
                    }
                    catch (Exception e){
                        Log.e("TEXTCHANGEERROR", "The distance was attempted to be changed from a null string");
                        distance = 0;
                    }

                    if (distance > 30){

                        //Please enter a new distance if the distance is going to be greater than 2000 km
                        editText2.setError("Distance cannot be chosen over 30 km");
                        editText2.requestFocus();

                    }
                    else if (s.toString().equals("") == false){
                        mListener.sendDistanceUpdate(distance);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (mParam1 >= 30){
                //Then we request the user to enter the correct value
                editText2.setError("Enter a custom distance");
                editText2.requestFocus();

            }
            else {
                editText2.setText(Integer.toString(mParam1));
            }

        }
        return inflate;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri,null);
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
        void onFragmentInteraction(Uri uri,LatLng latLng);
        void sendDistanceUpdate(int progress);
    }

}
