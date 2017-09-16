package com.jjunji.android.foolog2.util;

import com.jjunji.android.foolog2.model.DayList;

import java.util.List;

/**
 * Created by jhjun on 2017-09-16.
 */

public interface ITask {

    public interface createDialog{
        public void showDialog(List<DayList> dayListBody);
    }


}
