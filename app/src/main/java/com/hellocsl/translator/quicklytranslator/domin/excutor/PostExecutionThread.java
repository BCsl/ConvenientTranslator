package com.hellocsl.translator.quicklytranslator.domin.excutor;

import rx.Scheduler;

/**回调调度
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/19.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();

}
