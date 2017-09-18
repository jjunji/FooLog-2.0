package com.jjunji.android.foolog2.util;

import android.app.ProgressDialog;
import android.widget.ProgressBar;

import com.jjunji.android.foolog2.model.DayList;
import com.jjunji.android.foolog2.model.TagList;

import java.util.List;

/**
 * Created by jhjun on 2017-09-16.
 */

public interface ITask {

    public interface createDialog{
        public void showDialog(List<DayList> dayListBody);
        public void noDialog(List<DayList> dayListBody);
    }

    public interface settingAdapter{
        public void setAdapter(List<TagList> tagList);
        //public void showProgress(ProgressDialog progressDialog);
        public void showProgress();
        public void disProgress();
    }

}
