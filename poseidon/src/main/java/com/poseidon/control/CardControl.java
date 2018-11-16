package com.poseidon.control;

import android.os.Bundle;
import android.util.Pair;

import com.poseidon.control.card.CardManager;
import com.poseidon.control.card.ICard;
import com.poseidon.control.control.CardEventBus;
import com.poseidon.control.control.DataCenter;
import com.poseidon.control.control.ServiceCenter;
import com.poseidon.control.obsever.Observable;
import com.poseidon.control.obsever.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spf on 2018/11/15.
 */
public class CardControl {

    public static final String CREATE_VIEW_KEY = "create_view_key";

    public static final String UPDATE_VIEW_KEY = "update_view_key";

    private DataCenter dataCenter;

    private CardEventBus mCardEventBus;

    private ServiceCenter serviceCenter;

    private List<CardManager> cardManagers;

    private ObservableList observableList;

    public CardControl() {
        cardManagers = new ArrayList<>();
        mCardEventBus = new CardEventBus();
        dataCenter = new DataCenter();
        serviceCenter = new ServiceCenter();
        observableList = new ObservableList();
    }

    public void addCardManager(CardManager manager) {
        if (manager != null && !cardManagers.contains(manager)) {
            cardManagers.add(manager);
        }
    }

    /**
     * 更新数据
     *
     * @param key
     * @param object
     */
    public void notify(String key, Object object) {
        dataCenter.storePublicData(key, object);
        for (CardManager manager : cardManagers) {
            manager.addConcernKeys(key);
        }
        mCardEventBus.notify(key, object);
        updateViewAll();
    }

    /**
     * 更新一组数据
     *
     * @param dataList
     */
    public void notify(List<Pair<String, Object>> dataList) {
        for (Pair<String, Object> pair : dataList) {
            dataCenter.storePublicData(pair.first, pair.second);
            for (CardManager manager : cardManagers) {
                manager.addConcernKeys(pair.first);
            }
        }
        mCardEventBus.notify(dataList);
        updateViewAll();
    }


    /**
     * @param key
     * @param object
     * @param card
     */
    public void notifyCard(String key, Object object, ICard card) {
        dataCenter.storeCardData(key, card.getClass(), object);
        updateView(card);
    }

    /**
     * @param dataList
     * @param card
     */
    public void notifyCard(List<Pair<String, Object>> dataList, ICard card) {
        for (Pair<String, Object> pair : dataList) {
            dataCenter.storeCardData(pair.first, card.getClass(), pair.second);
        }
        updateView(card);
    }

    /**
     *
     */
    public void createViewAll() {
        mCardEventBus.notify(CREATE_VIEW_KEY, null);
    }

    /**
     *
     */
    public void updateViewAll() {
        mCardEventBus.notify(UPDATE_VIEW_KEY, null);
    }

    /**
     * @param card
     */
    private void createView(ICard card) {
        mCardEventBus.notify(CREATE_VIEW_KEY, card);
    }

    /**
     * 更新具体card
     *
     * @param card
     */
    private void updateView(ICard card) {
        mCardEventBus.notify(UPDATE_VIEW_KEY, card);
    }

    /**
     * 公共数据缓存
     *
     * @param key   数据key
     * @param clazz 数据类型
     * @param <T>
     * @return
     */
    public <T> T getData(String key, Class<T> clazz) {
        return dataCenter.getPublicData(key, clazz);
    }

    /**
     * Card 模块使用 私有
     *
     * @param key
     * @param clazz
     * @param card
     * @param <T>
     * @return
     */
    public <T> T getCardData(String key, Class<T> clazz, ICard card) {
        return dataCenter.getCardData(key, clazz, card.getClass());
    }

    /**
     * 外部容器使用
     *
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    public <T> Observable<T> getObservable(String key, Class<T> clz) {
        return getObservable(key, clz, null);
    }

    /**
     * card 使用
     *
     * @param key
     * @param clz
     * @param card
     * @param <T>
     * @return
     */
    public <T> Observable<T> getObservable(String key, Class<T> clz, ICard card) {
        if (card != null) {
            for (CardManager manager : cardManagers) {
                manager.addCardConcern(card, key);
            }
        }
        Observable<T> observable = mCardEventBus.getObservable(key);
        observableList.add(observable);
        return observable;
    }

    /**
     * destory
     */
    public void destroy() {
        //取消订阅
        observableList.unsubscribe();
    }


    /**
     * 关联 onCreate
     *
     * @param bundle
     */
    public void performCreate(Bundle bundle) {
        for (CardManager cardManager : cardManagers) {
            cardManager.performCreateCard(bundle);
        }
    }

    /**
     * 关联 onResume
     */
    public void performResume() {
        for (CardManager cardManager : cardManagers) {
            cardManager.performResumeCard();
        }
    }

    /**
     * 关联 onPause
     */
    public void performPause() {
        for (CardManager cardManager : cardManagers) {
            cardManager.performPauseCard();
        }
    }

    /**
     * 关联 onPause
     */
    public void performStop() {
        for (CardManager cardManager : cardManagers) {
            cardManager.performStopCard();
        }
    }

    /**
     * 关联 onDestroy
     */
    public void performDestroy() {
        for (CardManager cardManager : cardManagers) {
            cardManager.performDestroyCard();
        }
    }

    /**
     * 注册服务
     *
     * @param service
     * @param clazz
     * @param <T>
     */
    public <T> void registerService(T service, Class<T> clazz) {
        serviceCenter.registerService(service, clazz);
    }

    /**
     * 解除
     *
     * @param clazz
     */
    public void unRegisterService(Class clazz) {
        serviceCenter.unRegisterService(clazz);
    }

    /**
     * 获取服务
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getService(Class<T> clazz) {
        return serviceCenter.getService(clazz);
    }

    /**
     * 数据保存
     *
     * @param bundle
     */
    public void onSaveInstance(Bundle bundle) {
        dataCenter.onSaveInstance(bundle);
    }

    /**
     * 数据回复
     *
     * @param bundle
     */
    public void onRestoreInstance(Bundle bundle) {
        dataCenter.onRestoreInstance(bundle);
    }
}
