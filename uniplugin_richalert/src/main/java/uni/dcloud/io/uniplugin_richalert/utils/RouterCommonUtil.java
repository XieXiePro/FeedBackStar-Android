package uni.dcloud.io.uniplugin_richalert.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import uni.dcloud.io.uniplugin_richalert.RouterParams;

/**
 * @author xiep
 */
public class RouterCommonUtil {

    private static void toastInterruptInfo(final Activity activity, final Postcard postcard) {
        if (postcard.getTag() instanceof String) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext((String) postcard.getTag());
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    if (!TextUtils.isEmpty(s) && activity != null) {
                        Toast.makeText(activity, (String) postcard.getTag(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public static void startFunctionActivityResult(final Activity activity, int flag, Long resId, Bundle mBundle, int resultCode) {
        String funcionName = getFunctionName(flag, resId, activity);
        ARouter.getInstance().build(funcionName)
                .with(mBundle)
                .navigation(activity, resultCode, new InterruptCallback() {
                    @Override
                    public void onInterrupt(Postcard postcard) {
                        toastInterruptInfo(activity, postcard);
                    }
                });
    }

    /**
     * Activity页面跳转
     */
    public static void startFunctionActivity(final Activity activity, int flag, Long resId, Bundle mBundle) {
        String funcionName = getFunctionName(flag, resId, activity);
        ARouter.getInstance().build(funcionName)
                .with(mBundle)
                .navigation(activity, new InterruptCallback() {
                    @Override
                    public void onInterrupt(Postcard postcard) {
                        toastInterruptInfo(activity, postcard);
                    }
                });
    }

    /**
     * Broadcast页面跳转
     */
    public static void startFunctionActivity(final Context context, int flag, Long resId, Bundle mBundle) {
        String funcionName = getFunctionName(flag, resId, context);
        ARouter.getInstance().build(funcionName)
                .with(mBundle)
                .navigation(context, new InterruptCallback() {
                    @Override
                    public void onInterrupt(Postcard postcard) {

                    }
                });
    }

    @NonNull
    private static String getFunctionName(int flag, Long resId, Context context) {
        String funcionName ="";
        if (flag == -1) {
            funcionName = RouterParams.URL_DATALISTACTIVITY;
        }
        return funcionName;
    }
}