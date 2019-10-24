package com.rikkathewrold.rikkamusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rikkathewrold.rikkamusic.greendao.db.DaoMaster;
import com.rikkathewrold.rikkamusic.greendao.db.DaoSession;

import static com.rikkathewrold.rikkamusic.App.DATA_BASE_NAME;

/**
 * greendao 工具类
 */
public class DbManager {
    // 是否加密
    public static final boolean ENCRYPTED = true;
    private Context mContext;
    private static DbManager mDbManager;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    public DbManager(Context context) {
        mContext = context;
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DATA_BASE_NAME);
        getDaoMaster(context);
        getDaoSession(context);
    }

    public static DbManager getInstance(Context context) {
        if (mDbManager == null) {
            synchronized (DbManager.class) {
                if (mDbManager == null) {
                    mDbManager = new DbManager(context);
                }
            }
        }
        return mDbManager;
    }

    /**
     * 获取可读数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getReadableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
        }
        return mDevOpenHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getWritableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
        }
        return mDevOpenHelper.getWritableDatabase();
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (mDaoMaster == null) {
            synchronized (DbManager.class) {
                if (mDaoMaster == null) {
                    mDaoMaster = new DaoMaster(getWritableDatabase(context));
                }
            }
        }
        return mDaoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (mDaoSession == null) {
            synchronized (DbManager.class) {
                mDaoSession = getDaoMaster(context).newSession();
            }
        }
        return mDaoSession;
    }
}
