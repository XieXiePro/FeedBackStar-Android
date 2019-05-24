package pro.xp.com.feedbackstar;

//import com.alibaba.android.arouter.launcher.ARouter;

import io.dcloud.application.DCloudApplication;
import uni.dcloud.io.uniplugin_richalert.utils.LogUtils;

public class AndroidApp extends DCloudApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initArouter();
    }


    private void initArouter() {
//        if (LogUtils.isApkDebugable(this)) {
//            // 打印日志
//            ARouter.openLog();
//            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//            ARouter.openDebug();
//        }
//        ARouter.init(this);
    }
}
