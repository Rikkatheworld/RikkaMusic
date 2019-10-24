package com.rikkathewrold.rikkamusic.base;


public class BasePresenter<V extends BaseView, M extends BaseModel> {

    protected V mView;
    protected M mModel;


    public void unSubscribe(){
        if(mView != null){
            mView = null;
        }
    }
}
