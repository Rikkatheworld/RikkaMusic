package com.rikkathewrold.rikkamusic.database;

import android.content.Context;

import com.rikkathewrold.rikkamusic.search.bean.SearchHistoryBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class SearchHistoryDaoOp {
    private static final String TAG = "SearchHistoryDaoOp";

    /**
     * 添加数据到数据库
     */
    public static void insertData(Context context, List<SearchHistoryBean> listBean) {
        if (listBean == null || listBean.size() <= 0) {
            return;
        }
        DbManager.getDaoSession(context).getSearchHistoryBeanDao().insertInTx(listBean);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param listBean
     */
    public static void saveData(Context context, List<SearchHistoryBean> listBean) {
        deleteAllData(context);
        DbManager.getDaoSession(context).getSearchHistoryBeanDao().saveInTx(listBean);
    }

    /**
     * 删除全部数据
     *
     * @param context
     */
    public static void deleteAllData(Context context) {
        DbManager.getDaoSession(context).getSearchHistoryBeanDao().deleteAll();
    }

    /**
     * 查询所有数据
     *
     * @param context
     * @return
     */
    public static List<SearchHistoryBean> queryAll(Context context) {
        QueryBuilder<SearchHistoryBean> builder = DbManager.getDaoSession(context).getSearchHistoryBeanDao().queryBuilder();
        return builder.build().list();
    }
}