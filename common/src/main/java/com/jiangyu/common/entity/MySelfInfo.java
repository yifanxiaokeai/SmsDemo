package com.jiangyu.common.entity;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jiangyu.common.utils.AESUtil;
import com.jiangyu.common.utils.JsonUtil;
import com.jiangyu.common.utils.StringUtil;


public class MySelfInfo {

    private static final String USER_INFO = "user_info";
    private static final String USER_ENTITY = "user_entity";
    private static MySelfInfo ourInstance = new MySelfInfo();

    public static MySelfInfo getInstance() {
        return ourInstance;
    }

    private UserEntity user;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void writeUserToCache(Context context) {
        SharedPreferences settings = context.getSharedPreferences(USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        String strConfig = new Gson().toJson(user);
        String encrypt = AESUtil.encrypt(strConfig);
        editor.putString(USER_ENTITY, encrypt);
        editor.commit();
    }

    public void getUserCache(Context context) {
        SharedPreferences sharedata = context.getSharedPreferences(USER_INFO, 0);
        String recipesStr = sharedata.getString(USER_ENTITY, "");
        String decrypt = "";
        try {
            decrypt = AESUtil.decrypt(recipesStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isEmpty(decrypt)) {
            user = null;
        } else {
            UserEntity user1 = JsonUtil.toObject(decrypt,
                    UserEntity.class);
            if (user1 == null) {
                user = null;
            } else {
                user = user1;
            }
        }
    }

    public void clearCache(Context context) {
        SharedPreferences settings = context.getSharedPreferences(USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

}
