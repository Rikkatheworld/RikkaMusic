package com.rikkathewrold.rikkamusic;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.lzx.starrysky.manager.MusicManager;
import com.rikkathewrold.rikkamusic.greendao.db.DaoMaster;
import com.rikkathewrold.rikkamusic.greendao.db.DaoSession;

import org.greenrobot.greendao.database.Database;

public class App extends Application {
    private static final String TAG = "App";
    public static final String DATA_BASE_NAME = "RikkaMusicDao";

    private static App mContext;

    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ToastUtils.init(this);
        MusicManager.initMusicManager(this);
        initDataBase();
    }

    private void initDataBase() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, DATA_BASE_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static App getContext() {
        return mContext;
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
