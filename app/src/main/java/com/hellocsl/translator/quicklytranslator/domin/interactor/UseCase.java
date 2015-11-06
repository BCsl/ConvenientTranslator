package com.hellocsl.translator.quicklytranslator.domin.interactor;


import com.hellocsl.translator.quicklytranslator.domin.excutor.PostExecutionThread;
import com.hellocsl.translator.quicklytranslator.domin.excutor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/19 0019.
 */
public abstract class UseCase {

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private Subscription subscription = Subscriptions.empty();

    private Action1 nextAction;

    public void setNextAction(Action1 nextAction) {
        this.nextAction = nextAction;
    }

    protected UseCase(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread) {
        if(threadExecutor==null){
            throw new IllegalStateException("threadExecutor not null allow!!!");
        }
        if(postExecutionThread==null){
            throw new IllegalStateException("postExecutionThread not null allow!!!");
        }
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    /**
     * 构造 {@link Observable} ，进行各个用例需要进行的操作
     */
    protected abstract Observable buildUseCaseObservable();

    /**
     * 执行
     */
    public void execute(Subscriber UseCaseSubscriber) {
        if (nextAction != null) {
            this.subscription = this.buildUseCaseObservable().doOnNext(nextAction)
                    .subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(postExecutionThread.getScheduler())
                    .subscribe(UseCaseSubscriber);
        }else{
            this.subscription = this.buildUseCaseObservable()
                    .subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(postExecutionThread.getScheduler())
                    .subscribe(UseCaseSubscriber);
        }
    }


    /**
     */
    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
