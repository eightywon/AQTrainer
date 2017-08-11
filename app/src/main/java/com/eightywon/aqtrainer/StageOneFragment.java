package com.eightywon.aqtrainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class StageOneFragment extends Fragment {
    public ImageButton testButton;
    static TextView txtStageDescTimer;
    static TextView txtStepDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage1Play);
        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);
        txtStepDesc=(TextView) rootView.findViewById(R.id.txtStepDesc);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                if (!isPlaying) {
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
                    MediaPlayerSingleton.setStage(1);
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