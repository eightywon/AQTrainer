package com.eightywon.aqtrainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class StageTwoFragment extends Fragment {
    public ImageButton testButton;
    static TextView txtStageDescTimer;
    static TextView txtStepDesc;
    static ImageView imageView1;
    static ImageView imageView2;
    static ImageView rightShot1;
    static ImageView rightShot2;
    static ImageView rightShot3;
    static ImageView rightShot4;
    static ImageView rightShot5;
    static ImageView leftShot1;
    static ImageView leftShot2;
    static ImageView leftShot3;
    static ImageView leftShot4;
    static ImageView leftShot5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_2, container,
                false);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage2Play);
        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);
        txtStepDesc=(TextView) rootView.findViewById(R.id.txtStepDesc);
        imageView1=(ImageView) rootView.findViewById(R.id.imageView1);
        imageView2=(ImageView) rootView.findViewById(R.id.imageView2);

        leftShot1=(ImageView) rootView.findViewById(R.id.leftShot1);
        leftShot1.setVisibility(View.INVISIBLE);

        leftShot2=(ImageView) rootView.findViewById(R.id.leftShot2);
        leftShot2.setVisibility(View.INVISIBLE);

        leftShot3=(ImageView) rootView.findViewById(R.id.leftShot3);
        leftShot3.setVisibility(View.INVISIBLE);

        leftShot4=(ImageView) rootView.findViewById(R.id.leftShot4);
        leftShot4.setVisibility(View.INVISIBLE);

        leftShot5=(ImageView) rootView.findViewById(R.id.leftShot5);
        leftShot5.setVisibility(View.INVISIBLE);

        rightShot1=(ImageView) rootView.findViewById(R.id.rightShot1);
        rightShot1.setVisibility(View.INVISIBLE);

        rightShot2=(ImageView) rootView.findViewById(R.id.rightShot2);
        rightShot2.setVisibility(View.INVISIBLE);

        rightShot3=(ImageView) rootView.findViewById(R.id.rightShot3);
        rightShot3.setVisibility(View.INVISIBLE);

        rightShot4=(ImageView) rootView.findViewById(R.id.rightShot4);
        rightShot4.setVisibility(View.INVISIBLE);

        rightShot5=(ImageView) rootView.findViewById(R.id.rightShot5);
        rightShot5.setVisibility(View.INVISIBLE);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                if (!isPlaying) {
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
                    MediaPlayerSingleton.setStage(2);
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