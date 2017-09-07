package com.eightywon.aqtrainer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

class Utils {
    int lastSec=0;
    boolean firstTime=false;
    Handler hCountDownPrepStage=new Handler();
    Handler hCountDownFireStage=new Handler();
    Handler hCountDownDescStage=new Handler();
    private static int countDownFireStageInterval;

    boolean getPlayPrepAnnouncements(Context cont) {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(cont);
        return prefs.getBoolean("chkpPlayPrep",false);
    }

    int getPrepTime(Context cont) {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(cont);
        String prepTime=prefs.getString("lpSettingsPrepTime","");
        return Integer.parseInt(prepTime);
    }

    private boolean getAnnounceStageTime(Context cont) {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(cont);
        return prefs.getBoolean("chkpStageTimerAnnounce",false);
    }

    private boolean getRedAlertMode(Context cont) {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(cont);
        return prefs.getBoolean("chkpRedAlertMode",false);
    }

    boolean getPlayStageDescription(Context cont) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(cont);
        return prefs.getBoolean("chkpPlayStageDesc",false);
    }

    Runnable countDownPrepStage = new Runnable() {
        @Override
        public void run() {
            View view=activity.findViewById(previousPage);
            TextView txtStageDescTimer = (TextView) view.findViewById(R.id.txtStageDescTimer);
            int remaining=MediaPlayerSingleton.getInstance().getRemaining();

            int secs=0;
            int mins=0;
            if (remaining>=0) {
                if (remaining>=60) {
                    mins=remaining/60;
                    secs=remaining%60;
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:%d",mins,secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:0%d",mins,secs));
                            }
                        }
                    }
                } else {
                    secs=remaining%60;
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:%d",secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:0%d",secs));
                            }
                        }
                    }
                }
            }
            lastSec=secs;
            firstTime=false;
            hCountDownPrepStage.postDelayed(countDownPrepStage,200);
        }
    };

    Runnable countDownFireStage = new Runnable() {
        @Override
        public void run() {
            if (getAnnounceStageTime(context)) {
                String announceInterval;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                announceInterval = prefs.getString("lpSettingsAnnounceInterval", "");
                countDownFireStageInterval = Integer.parseInt(announceInterval);
            }

            View view=activity.findViewById(previousPage);
            TextView txtStageDescTimer = (TextView) view.findViewById(R.id.txtStageDescTimer);
            int remaining=MediaPlayerSingleton.getInstance().getRemaining();

            boolean redAlertMode=getRedAlertMode(context);
            int secs=0;
            int mins=0;
            String howLong;
            if (remaining>=0) {
                if (remaining>=60) {
                    mins=remaining/60;
                    secs=remaining%60;
                    howLong=String.valueOf(mins)+" minutes ";
                    if (secs>0) howLong+=String.valueOf(secs)+" seconds.";
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:%d",mins,secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:0%d",mins,secs));
                            }
                        }
                    }
                } else {
                    secs=remaining%60;
                    if (remaining<=10 && redAlertMode) {
                        howLong=String.valueOf(secs);
                    } else {
                        howLong=String.valueOf(secs)+" seconds.";
                    }
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:%d",secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:0%d",secs));
                            }
                        }
                    }
                }
                if ((remaining<=10 && redAlertMode) || (getAnnounceStageTime(context) && remaining%countDownFireStageInterval==0) || firstTime) {
                    if ((secs!=lastSec || firstTime) && remaining>0 && (getAnnounceStageTime(context) || (redAlertMode && remaining<=10))) {
                        MainActivity.textToSpeech.speak(howLong, TextToSpeech.QUEUE_FLUSH, null, "");
                    }
                }
            }
            lastSec=secs;
            firstTime=false;
            hCountDownFireStage.postDelayed(countDownFireStage, 200);
        }
    };

    Runnable countDownDescStage = new Runnable() {
        @Override
        public void run() {

            View view=activity.findViewById(previousPage);
            ImageView imageView1=(ImageView) view.findViewById(R.id.target1);
            ImageView imageView2=(ImageView) view.findViewById(R.id.target2);
            ImageView imageView3=(ImageView) view.findViewById(R.id.target3);
            ImageView imageView4=(ImageView) view.findViewById(R.id.target4);
            ImageView imageViewh1=(ImageView) view.findViewById(R.id.target1Highlight);
            ImageView imageViewh2=(ImageView) view.findViewById(R.id.target2Highlight);
            ImageView imageViewh3=(ImageView) view.findViewById(R.id.target3Highlight);
            ImageView imageViewh4=(ImageView) view.findViewById(R.id.target4Highlight);

            ImageView shot1=(ImageView) view.findViewById(R.id.shot1);
            ImageView shot2=(ImageView) view.findViewById(R.id.shot2);
            ImageView shot3=(ImageView) view.findViewById(R.id.shot3);
            ImageView shot4=(ImageView) view.findViewById(R.id.shot4);
            ImageView shot5=(ImageView) view.findViewById(R.id.shot5);
            ImageView shot6=(ImageView) view.findViewById(R.id.shot6);
            ImageView shot7=(ImageView) view.findViewById(R.id.shot7);
            ImageView shot8=(ImageView) view.findViewById(R.id.shot8);
            ImageView shot9=(ImageView) view.findViewById(R.id.shot9);
            ImageView shot10=(ImageView) view.findViewById(R.id.shot10);

            int remaining=MediaPlayerSingleton.getInstance().getRemaining();

            switch (MediaPlayerSingleton.getStage()) {
                case 1:
                    if (remaining==10) {
                        shot1.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==9) {
                        shot2.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==8) {
                        shot3.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==7) {
                        shot4.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==6) {
                        shot5.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==5) {
                        imageViewh1.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                        imageView1.setPadding(1, 1, 1, 1);
                        shot6.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==4) {
                        shot7.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==3) {
                        shot8.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==2) {
                        imageView1.setVisibility(View.VISIBLE);
                        imageViewh1.setVisibility(View.INVISIBLE);
                        imageView1.setPadding(0, 0, 0, 0);
                        shot9.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==1) {
                        shot10.setVisibility(View.VISIBLE);
                        break;
                    }
                    break;
                case 2:
                    if (remaining==14) {
                        imageViewh1.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                    } else if (remaining==13) {
                        shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==12) {
                        shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==11) {
                        shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        shot4.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        shot5.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        imageView1.setVisibility(View.VISIBLE);
                        imageViewh1.setVisibility(View.INVISIBLE);
                        imageViewh2.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                    } else if (remaining==7) {
                        shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==6) {
                        imageViewh2.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                        shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        shot8.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        shot10.setVisibility(View.VISIBLE);
                    } else if (remaining==2) {
                        imageView2.setVisibility(View.VISIBLE);
                        imageViewh2.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 3:
                    if (remaining==18) {
                        imageViewh1.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                    } else if (remaining==12) {
                        imageView1.setVisibility(View.VISIBLE);
                        imageViewh1.setVisibility(View.INVISIBLE);
                    } else if (remaining==17) {
                        shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==16) {
                        shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==13) {
                        shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        imageViewh2.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                        shot4.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        shot5.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==7) {
                        imageView2.setVisibility(View.VISIBLE);
                        imageViewh2.setVisibility(View.INVISIBLE);
                    } else if (remaining==6) {
                        imageViewh3.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.INVISIBLE);
                        shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        shot8.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        shot10.setVisibility(View.VISIBLE);
                    } else if (remaining==2) {
                        imageView3.setVisibility(View.VISIBLE);
                        imageViewh3.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 4:
                    if (remaining==13) {
                        imageViewh1.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                        imageViewh2.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                    } else if (remaining==12) {
                        shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==11) {
                        shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        shot4.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        shot5.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.VISIBLE);
                        imageViewh1.setVisibility(View.INVISIBLE);
                        imageView2.setVisibility(View.VISIBLE);
                        imageViewh2.setVisibility(View.INVISIBLE);
                        imageViewh3.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.INVISIBLE);
                        imageViewh4.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.INVISIBLE);
                    } else if (remaining==7) {
                        shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==6) {
                        shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        shot8.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        shot10.setVisibility(View.VISIBLE);
                    } else if (remaining==2) {
                        imageView3.setVisibility(View.VISIBLE);
                        imageViewh3.setVisibility(View.INVISIBLE);
                        imageView4.setVisibility(View.VISIBLE);
                        imageViewh4.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
            hCountDownDescStage.postDelayed(countDownDescStage,200);
        }
    };

    private Activity activity;
    private int previousPage;
    private Context context;

    Utils(Activity _activity, int previousPage, Context context){
        this.activity = _activity;
        this.previousPage = previousPage;
        this.context = context;
    }
}