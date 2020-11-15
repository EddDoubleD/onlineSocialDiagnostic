package ru.hardwork.onlinesocialdiagnosticapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.scenery.ViewAnimation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {

    FloatingActionButton addButton, fabJoin, fabInvite;
    boolean isRotate = false;


    public GroupFragment() {
        // Required empty public constructor
    }


    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        addButton = view.findViewById(R.id.addButton);
        fabJoin = view.findViewById(R.id.fabJoin);
        fabInvite = view.findViewById(R.id.fabInvite);
        ViewAnimation.init(fabJoin);
        ViewAnimation.init(fabInvite);

        addButton.setOnClickListener(v -> {
            isRotate = ViewAnimation.rotateFab(v, !isRotate);
            if(isRotate){
                ViewAnimation.showIn(fabJoin);
                ViewAnimation.showIn(fabInvite);
            }else{
                ViewAnimation.showOut(fabJoin);
                ViewAnimation.showOut(fabInvite);
            }
        });
        return view;
    }
}