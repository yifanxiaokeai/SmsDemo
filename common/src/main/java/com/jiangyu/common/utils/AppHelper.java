package com.jiangyu.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>Title: 应用帮助类         </p>
 * <p>Description: 包括
 * <p> 1. 获取版本名称          </p>
 * <p> 2. 获取版本号               </p>
 * <p> 3. 获取手机IMSI号    </p>
 * <p> 4. 获取手机IMEI号    </p>
 * <p> 5. 获取系统图类型     </p>
 * <p> 6. 获取手机屏幕宽高</p>
 * <p> 7. 获取手机屏幕密度比例</p>
 * <p> 8. 获取手机文字密度比例</p>
 * <p> 9. 获取手机顶部状态栏高度</p>
 * <p> 10. 获取手机IP地址</p>
 * <p> 11. 获取手机Mac地址</p>
 * </p>
 */

public final class AppHelper {

    /**
     * 获取应用版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).versionName;
        } catch (NameNotFoundException e) {
            Log.e(e.getMessage(), e);
            // e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    1);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            Log.e(e.getMessage(), e);
            // e.printStackTrace();
        }
        return versionCode;
    }

    public static String getPackageName(Context context) {
        String packageName = null;
        try {
            packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).packageName;
        } catch (NameNotFoundException e) {
            Log.e(e.getMessage(), e);
            // e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 判断是否当前应用在前端可视
     *
     * @param pkgName
     * @param context
     * @return
     */
    public static boolean isApplicationShow(String pkgName, Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);

        if (activityManager == null)
            return false;
        // get running application processes
        List<RunningAppProcessInfo> processList = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(pkgName)) {
                boolean isBackground = process.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && process.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState)
                    return true;
                else
                    return false;
            }
        }

        return false;
    }

    static boolean isSpecialSystem = false;

    /**
     * 判断程序是否在前台
     *
     * @return true 在前台; false 在后台
     */
    public static boolean isAppOnForeground(String pkgName, Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        if (!isSpecialSystem) {
            boolean isspecial = true;
            String packageName = pkgName;

            List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null)
                return false;
            for (RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(packageName)) {
                    if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            || appProcess.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                        return true;
                    }
                    if (keyguardManager.inKeyguardRestrictedInputMode())
                        return true;
                }
                if (isspecial) {
                    if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        isspecial = false;
                    }
                }
            }
            if (isspecial) {
                isSpecialSystem = true;
                return !isApplicationBroughtToBackgroundByTask(activityManager, pkgName);
            }
            return false;
        } else {
            return !isApplicationBroughtToBackgroundByTask(activityManager, pkgName);
        }
    }

    /**
     * 判断当前应用程序是否处于后台，通过getRunningTasks的方式
     *
     * @return true 在后台; false 在前台
     */
    public static boolean isApplicationBroughtToBackgroundByTask(ActivityManager activityManager,
                                                                 String pkgName) {

        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(pkgName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 变黑变
     */
    public static void changeToBlackAndWhite(ImageView view, float f) {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(f); // 设置饱和度:0为纯黑白，饱和度为0；1为饱和度为100，即原图；
        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
        view.setColorFilter(grayColorFilter);
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    public static String getMobileIMSI(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr.getSubscriberId();
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telManage = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telManage.getDeviceId();
    }

    /**
     * 获取手机运营商
     *
     * @param context
     * @return
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperatorName();
    }

    /**
     * 获取手机号码
     *
     * @param context
     * @return
     */
    public static String getSimMobile(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 返回系统类型，android系统为1
     *
     * @return
     */
    public static String getOSType() {
        return "1"; // android系统为1
    }

    /**
     * 获取新的OSType
     *
     * @return
     */
    public static String getOSTypeNew() {
        return "android";
    }

    /**
     * 操作系统版本
     *
     * @return
     */
    public static String getOSRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 手机厂商
     *
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取手机屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取手机屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取虚拟功能键高度
     *
     * @return
     */
    public static int getVirtualBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * 获取屏幕密度比例
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取文字密度比例
     *
     * @param context
     * @return
     */
    public static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static PackageInfo getPackageInfo(Activity activity, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = activity.getPackageManager().getPackageInfo(packageName, 0);

        } catch (NameNotFoundException e) {
            packageInfo = null;
            // e.printStackTrace();
            Log.e(e.getMessage(), e);
        }
        return packageInfo;
    }

    /**
     * 获取手机顶部状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        }
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("GetIpAddress Exception" + e);
        }
        return null;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + (i >> 24 & 0xFF);
    }

    /**
     * 获取手机MAC地址，通过wifi
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取手机MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            Log.e("获取MAC地址异常：" + ex);
        }
        return macSerial;

    }

    public static String getWifiMacAddress(Context context) {
        String macAddress = null;
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取唯一码，通过对mac地址进行加密串
     *
     * @return
     */
    public static String getSerialCode() {
        String mac = getMacAddress();
        if (!StringUtil.isEmpty(mac)) {
            return EncryptUtil.encryptMd5(mac);
        }

        return null;
    }

    /**
     * 获取唯一码
     * 1、默认imsi
     * 2、当imsi为空时，imei
     * 3、当imei为空时，获取mac地址
     */
    public static String getSerialCode2(Context context) {
        String serial = getMobileIMSI(context);
        if (StringUtil.isEmpty(serial)) {
            serial = getIMEI(context);
        }

        if (StringUtil.isEmpty(serial)) {
            String macAddress = AppHelper.getMacAddress();
            if (!StringUtil.isEmpty(macAddress)) {
                serial = macAddress.replaceAll(":", "");
            }
        }

        return serial;
    }
}
