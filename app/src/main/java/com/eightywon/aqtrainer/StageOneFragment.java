package com.eightywon.aqtrainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StageOneFragment extends Fragment {
    public Button stageButton;
    static TextView txtStageDescTimer;
    static TextView txtStepDesc;
    static ImageView target1;
    static ImageView target1Highlight;
    static ImageView shot1;
    static ImageView shot2;
    static ImageView shot3;
    static ImageView shot4;
    static ImageView shot5;
    static ImageView shot6;
    static ImageView shot7;
    static ImageView shot8;
    static ImageView shot9;
    static ImageView shot10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);

        stageButton=(Button) rootView.findViewById(R.id.btnStage1Play);
        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);
        txtStepDesc=(TextView) rootView.findViewById(R.id.txtStepDesc);
        target1=(ImageView) rootView.findViewById(R.id.target1);
        target1Highlight=(ImageView) rootView.findViewById(R.id.target1Highlight);

        target1Highlight.setVisibility(View.INVISIBLE);

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
                    stageButton.setText(R.string.btnStopStage);
                    stageButton.setBackgroundResource(R.drawable.button_clicked);
                    MediaPlayerSingleton.setStage(1);
                    MediaPlayerSingleton.setActivity(getActivity());
                    MediaPlayerSingleton.getInstance().play(getContext(),0,false);
                } else {
                    MediaPlayerSingleton.stopPlaying(getContext());
                    stageButton.setText(R.string.btnStartStage);
                    stageButton.setBackgroundResource(R.drawable.button);
                }
            }
        });
        return rootView;
    }

    void setTxtStageDescTimer(String desc) {
        txtStageDescTimer.setText(desc);
    }

    void setTxtStepDesc(String desc) {
        txtStepDesc.setText(desc);
    }
}