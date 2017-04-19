package com.eightywon.aqtrainer;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.eightywon.aqtrainer.R.id.btnStage1Play;
import static com.eightywon.aqtrainer.R.id.txtSeekStatus;
import static java.util.Objects.isNull;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public int previousPage;

    ImageButton testButton;

    MediaPlayer mp1;
    MediaPlayer mp2;
    MediaPlayer mp3;
    MediaPlayer mp4;

    private static SeekBar sb;
    private static TextView tStatus;

    public static void seekBar() {
        /**
        final int MINUTE = 60*1000;
        final int SECOND = 1000;

        int durationInMilli=mp1.getDuration();
        int durationMin=(durationInMilli/MINUTE);
        int durationSec=(durationInMilli%MINUTE)/SECOND;
        **/

        if (!isNull(tStatus)) {
            //sb.setMax(mp1.getDuration());
            tStatus.setText(sb.getProgress()+" of "+sb.getMax());
        }


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progressValue;
                progressValue=progress;
                tStatus.setText(progress+" of "+sb.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void playStage(View v) {
        testButton=(ImageButton) findViewById(v.getId());
        if (v.getId()==R.id.btnStage1Play) {
            if (mp1.isPlaying()) {
                mp1.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp1.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        } else if (v.getId()==R.id.btnStage2Play) {
            if (mp2.isPlaying()) {
                mp2.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp2.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        } else if (v.getId()==R.id.btnStage3Play) {
            if (mp3.isPlaying()) {
                mp3.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp3.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        } else if (v.getId()==R.id.btnStage4Play) {
            if (mp4.isPlaying()) {
                mp4.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp4.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(MainActivity.this, "Scrolled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this, "Selected: "+position+", Previous: "+previousPage, Toast.LENGTH_SHORT).show();
                switch (previousPage) {
                    case 0:
                        if (mp1.isPlaying()) {
                            Toast.makeText(MainActivity.this, "here", Toast.LENGTH_SHORT).show();
                            mp1.pause();
                            mp1.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage1Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                    case 1:
                        if (mp2.isPlaying()) {
                            mp2.pause();
                            mp2.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage2Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                    case 2:
                        if (mp3.isPlaying()) {
                            mp3.pause();
                            mp3.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage3Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                    case 3:
                        if (mp4.isPlaying()) {
                            mp4.pause();
                            mp4.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage4Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                }
                previousPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        mp1=new MediaPlayer();
        mp2=new MediaPlayer();
        mp3=new MediaPlayer();
        mp4=new MediaPlayer();

        try {
            AssetFileDescriptor afd1=getAssets().openFd("DescStage1.mp3");
            mp1.setDataSource(afd1.getFileDescriptor(),afd1.getStartOffset(),afd1.getLength());
            AssetFileDescriptor afd2=getAssets().openFd("DescStage2.mp3");
            mp2.setDataSource(afd2.getFileDescriptor(),afd2.getStartOffset(),afd2.getLength());
            AssetFileDescriptor afd3=getAssets().openFd("DescStage3.mp3");
            mp3.setDataSource(afd3.getFileDescriptor(),afd3.getStartOffset(),afd3.getLength());
            AssetFileDescriptor afd4=getAssets().openFd("DescStage4.mp3");
            mp4.setDataSource(afd4.getFileDescriptor(),afd4.getStartOffset(),afd4.getLength());
            mp1.prepare();
            mp2.prepare();
            mp3.prepare();
            mp4.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "nope", Toast.LENGTH_SHORT).show();;
        }

        mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage1Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage2Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage3Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage4Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent=new Intent(this,DisplaySettings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /** NEW CODE STARTS HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * A placeholder fragment containing a simple view.
     */
    public static class fragStage1 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public fragStage1() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static fragStage1 newInstance(int sectionNumber, String s) {
            fragStage1 fragment = new fragStage1();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("someTitle",s);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_stage_1, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label_1);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            tStatus=(TextView) rootView.findViewById(R.id.txtSeekStatus);
            sb=(SeekBar) rootView.findViewById(R.id.seekBarStage1);
            seekBar();
            return rootView;
        }
    }

    public static class fragStage2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public fragStage2() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static fragStage2 newInstance(int sectionNumber, String s) {
            fragStage2 fragment = new fragStage2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("someTitle",s);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_stage_2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label_2);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public static class fragStage3 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public fragStage3() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static fragStage3 newInstance(int sectionNumber, String s) {
            fragStage3 fragment = new fragStage3();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("someTitle",s);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_stage_3, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label_3);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public static class fragStage4 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public fragStage4() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static fragStage4 newInstance(int sectionNumber, String s) {
            fragStage4 fragment = new fragStage4();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("someTitle",s);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.frag_stage_4, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label_4);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return fragStage1.newInstance(0, "Stage 1");
                case 1:
                    return fragStage2.newInstance(1, "Stage 2");
                case 2:
                    return fragStage3.newInstance(2, "Stage 3");
                case 3:
                    return fragStage4.newInstance(3, "Stage 4");
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Stage 1";
                case 1:
                    return "Stage 2";
                case 2:
                    return "Stage 3";
                case 3:
                    return "Stage 4";
            }
            return null;
        }
    }
}
