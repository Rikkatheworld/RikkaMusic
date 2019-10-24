package com.rikkathewrold.rikkamusic.database;

import android.content.Context;

import com.rikkathewrold.rikkamusic.main.bean.DRGreenDaoBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 将日推写入到GreenDao中、从GreenDao中获取日推
 */
public class DailyRecommendDaoOp {

    /**
     * 添加数据到数据库
     */
    public static void insertData(Context context, List<DRGreenDaoBean> listBean) {
        if (listBean == null || listBean.size() <= 0) {
            return;
        }
        DbManager.getDaoSession(context).getDRGreenDaoBeanDao().insertInTx(listBean);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param listBean
     */
    public static void saveData(Context context, List<DRGreenDaoBean> listBean) {
        DbManager.getDaoSession(context).getDRGreenDaoBeanDao().saveInTx(listBean);
    }

    /**
     * 删除全部数据
     *
     * @param context
     */
    public static void deleteAllData(Context context) {
        DbManager.getDaoSession(context).getDRGreenDaoBeanDao().deleteAll();
    }

    /**
     * 查询所有数据
     *
     * @param context
     * @return
     */
    public static List<DRGreenDaoBean> queryAll(Context context) {
        QueryBuilder<DRGreenDaoBean> builder = DbManager.getDaoSession(context).getDRGreenDaoBeanDao().queryBuilder();

        return builder.build().list();
    }
}
