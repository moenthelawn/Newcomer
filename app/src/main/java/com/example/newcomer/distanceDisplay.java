package com.example.newcomer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;


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
    private EditText editText2;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private ImageButton saveChanges;

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
     * @param param2 Parameter 2.
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
        saveChanges = inflate.findViewById(R.id.imageButton4);
        //Set the onclick listener for save changes

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            editText2 = inflate.findViewById(R.id.editText2);
            editText2.setText(Integer.toString(mParam1));
            saveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Now we set the onclick listener
                    mListener.onFragmentInteraction(Uri.parse("save_changes"));

                }
            });
        }

        return inflate;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
