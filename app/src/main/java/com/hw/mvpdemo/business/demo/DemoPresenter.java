package com.hw.mvpdemo.business.demo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hw.mvpbase.basenetwork.consumer.BaseApiErrorConsumer;
import com.hw.mvpbase.basenetwork.consumer.BaseApiResultConsumer;
import com.hw.mvpbase.basenetwork.exception.ApiException;
import com.hw.mvpbase.basenetwork.utils.RetryWithDelay;
import com.hw.mvpbase.entity.BaseResponse;
import com.hw.mvpdemo.api.DemoApi;
import com.hw.mvpdemo.business.login.LoginContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hw on 2018/7/18.
 */

public class DemoPresenter extends DemoContract.Presenter {
    private Context mContext;

    private Disposable mDisposable;

    public DemoPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void attachView(@NonNull DemoContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mContext = null;
    }

    @Override
    public void doSomething() {
        removeDisposable(mDisposable);

        mDisposable = DemoApi.postSomething("something")
                .retryWhen(new RetryWithDelay(3, 0))  //接口重试次数，及延迟时间
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseApiResultConsumer<BaseResponse>() {
                    @Override
                    protected void handleResult(BaseResponse result) throws Exception {
                        super.handleResult(result);
                    }
                }, new BaseApiErrorConsumer<Throwable>(mContext, getView()) {
                    @Override
                    public void handleError(Throwable t, boolean handled) throws Exception {
                        super.handleError(t, handled);
                    }
                });

        addDisposable(mDisposable);
    }
}
