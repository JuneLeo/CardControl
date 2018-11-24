package com.poseidon.control;

import android.os.Bundle;
import android.util.Pair;

import com.poseidon.control.card.ICard;
import com.poseidon.control.control.DataCenter;
import com.poseidon.control.control.ServiceCenter;
import com.poseidon.control.debug.DebugOption;
import com.poseidon.control.event.Event;
import com.poseidon.control.event.EventManager;
import com.poseidon.control.obsever.Observable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spf on 2018/11/15.
 */
public class CardControl {


    public static final String CREATE_VIEW_KEY = "create_view_key";

    public static final String UPDATE_VIEW_KEY = "update_view_key";

    private DataCenter dataCenter;

    private EventManager eventManager;

    private ServiceCenter serviceCenter;

    private String group;

    private static Map<String, CardControl> cardControlMap = new HashMap<>();

    private CardControl(String group) {
        this.group = group;
        dataCenter = new DataCenter();
        serviceCenter = new ServiceCenter();
        eventManager = EventManager.get();
    }

    public static CardControl create(String group) {
        CardControl cardControl = cardControlMap.get(group);
        if (cardControl == null) {
            cardControl = new CardControl(group);
        }
        return cardControl;
    }

    public static CardControl get(String group) {
        checkGroup(group);
        return cardControlMap.get(group);
    }

    public void notify(String key, Object object) {
        dataCenter.storePublicData(key, object);
        eventManager.add(Event.create(group, key, object));
    }

    public void notifyUI(String key, Object object) {
        dataCenter.storePublicData(key, object);
        eventManager.add(Event.createUI(group, key, object));
    }

    public void notifyUI(String group, List<Pair<String, Object>> dataList) {
        for (Pair<String, Object> pair : dataList) {
            dataCenter.storePublicData(pair.first, pair.second);
            eventManager.add(Event.createUI(group, pair.first, pair.second));
        }
    }

    public void notifyAllView() {
        eventManager.add(Event.createUI(group, UPDATE_VIEW_KEY, null));
    }

    /**
     * 跨页面调用 子线程返回
     *
     * @param group
     * @param key
     * @param object
     */
    public static void notify(String group, String key, Object object) {
        checkGroup(group);
        CardControl cardControl = cardControlMap.get(group);
        if (cardControl != null) {
            cardControl.notify(key, object);
        }

    }

    /**
     * 跨页面调用 UI线程返回
     *
     * @param group
     * @param key
     * @param object
     */
    public static void notifyUI(String group, String key, Object object) {
        checkGroup(group);
        CardControl cardControl = cardControlMap.get(group);
        if (cardControl != null) {
            cardControl.notifyUI(key, object);
        }

    }

    public <T> T getData(String key, Class<T> clazz) {
        return dataCenter.getPublicData(key, clazz);
    }


    public <T> T getCardData(String key, Class<T> clazz, ICard card) {
        return dataCenter.getCardData(key, clazz, card.getClass());
    }


    public <T> Observable<T> getObservable(String key, Class<T> clz) {
        return eventManager.getObservable(group, key);
    }

    /**
     * destory
     */
    public void destroy() {
        //取消订阅
        eventManager.dispose(group);
        cardControlMap.remove(group);
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


    private static void checkGroup(String group) {
        if (DebugOption.isDebug()) {
            CardControl cardControl = cardControlMap.get(group);
            if (cardControl == null)
                throw new RuntimeException("cardcontrol is null by,when group is " + group);
        }
    }
}
