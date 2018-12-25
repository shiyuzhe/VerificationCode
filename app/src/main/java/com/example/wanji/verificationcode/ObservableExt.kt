package com.example.wanji.verificationcode

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *   by  :   syz
 *   Time: 2018/11/12 13:30
 *   Description:
 */



fun <T : Any> Observable<T>.subscribeByThread(onErrStub: (Throwable) -> Unit = {}, onNextStub: (T) -> Unit = {}
): Disposable = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(onErrStub, {}, onNextStub)