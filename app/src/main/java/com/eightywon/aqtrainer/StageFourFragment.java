package com.eightywon.aqtrainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class StageFourFragment extends Fragment {
    public ImageButton testButton;
    static TextView txtStageDescTimer;
    static TextView txtStepDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_4, container,
                false);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage4Play);
        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);
        txtStepDesc=(TextView) rootView.findViewById(R.id.txtStepDesc);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                if (!isPlaying) {
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
                    MediaPlayerSingleton.setStage(4);
                    MediaPlayerSingleton.setActivity(getActivity());
                    MediaPlayerSingleton.getInstance().play(getContext(),0,false);
                } else {
                    MediaPlayerSingleton.stopPlaying(getContext());
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                }
            }
        });
        return rootView;
    }
}