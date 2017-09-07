package com.eightywon.aqtrainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StageThreeFragment extends Fragment {
    public Button stageButton;
    TextView txtStageDescTimer;
    TextView txtStepDesc;

    ImageView target1;
    ImageView target2;
    ImageView target3;
    ImageView target1Highlight;
    ImageView target2Highlight;
    ImageView target3Highlight;

    ImageView shot1;
    ImageView shot2;
    ImageView shot3;
    ImageView shot4;
    ImageView shot5;
    ImageView shot6;
    ImageView shot7;
    ImageView shot8;
    ImageView shot9;
    ImageView shot10;

    static public View fragView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_stage_3, container,
                false);

        fragView=rootView;
        stageButton=(Button) rootView.findViewById(R.id.btnStagePlay);
        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);
        txtStepDesc=(TextView) rootView.findViewById(R.id.txtStepDesc);

        target1=(ImageView) rootView.findViewById(R.id.target1);
        target2=(ImageView) rootView.findViewById(R.id.target2);
        target3=(ImageView) rootView.findViewById(R.id.target3);
        target1Highlight=(ImageView) rootView.findViewById(R.id.target1Highlight);
        target2Highlight=(ImageView) rootView.findViewById(R.id.target2Highlight);
        target3Highlight=(ImageView) rootView.findViewById(R.id.target3Highlight);

        target1Highlight.setVisibility(View.INVISIBLE);
        target2Highlight.setVisibility(View.INVISIBLE);
        target3Highlight.setVisibility(View.INVISIBLE);

        shot1=(ImageView) rootView.findViewById(R.id.shot1);
        shot1.setVisibility(View.INVISIBLE);

        shot2=(ImageView) rootView.findViewById(R.id.shot2);
        shot2.setVisibility(View.INVISIBLE);

        shot3=(ImageView) rootView.findViewById(R.id.shot3);
        shot3.setVisibility(View.INVISIBLE);

        shot4=(ImageView) rootView.findViewById(R.id.shot4);
        shot4.setVisibility(View.INVISIBLE);

        shot5=(ImageView) rootView.findViewById(R.id.shot5);
        shot5.setVisibility(View.INVISIBLE);

        shot6=(ImageView) rootView.findViewById(R.id.shot6);
        shot6.setVisibility(View.INVISIBLE);

        shot7=(ImageView) rootView.findViewById(R.id.shot7);
        shot7.setVisibility(View.INVISIBLE);

        shot8=(ImageView) rootView.findViewById(R.id.shot8);
        shot8.setVisibility(View.INVISIBLE);

        shot9=(ImageView) rootView.findViewById(R.id.shot9);
        shot9.setVisibility(View.INVISIBLE);

        shot10=(ImageView) rootView.findViewById(R.id.shot10);
        shot10.setVisibility(View.INVISIBLE);

        stageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                if (!isPlaying) {
                    MainActivity.fragView=rootView;
                    stageButton.setText(R.string.btnStopStage);
                    stageButton.setBackgroundResource(R.drawable.button_clicked);
                    MediaPlayerSingleton.setStage(3);
                    MediaPlayerSingleton.setActivity(getActivity());
                    MediaPlayerSingleton.getInstance().play(getContext(),0,false);
                } else {
                    MediaPlayerSingleton.getInstance().stopPlaying(getContext());
                    stageButton.setText(R.string.btnStartStage);
                    stageButton.setBackgroundResource(R.drawable.button);
                }
            }
        });
        return rootView;
    }
}