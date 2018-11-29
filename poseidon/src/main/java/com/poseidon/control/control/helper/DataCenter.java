package com.poseidon.control.control.helper;

import android.os.Bundle;
import android.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by huzhaoxu on 2016/12/30.
 */

public class DataCenter {

    private Map<String, Object> publicDataMap;
    private Map<Class, Map<String, Object>> cardDataMap;

    private static final String PUBLIC_KEY = "public_data_map";
    private static final String CARD_KEY = "card_data_map";


    public DataCenter() {
        publicDataMap = new HashMap<>();
        cardDataMap = new HashMap<>();
    }

    public void storePublicData(String key, Object data) {
        publicDataMap.put(key, data);
    }

    public void storeCardData(String key, Class clazz, Object data) {
        if (cardDataMap.get(clazz) == null) {
            cardDataMap.put(clazz, new HashMap<String, Object>());
        }
        cardDataMap.get(clazz).put(key, data);
    }

    public <T> T getPublicData(String key, Class<T> clazz) {
        Object data = publicDataMap.get(key);
        if(data == null){
            return null;
        }
        if (clazz.isInstance(data)) {
            return (T) data;
        } else {
            throw new ClassCastException("data is not class:" + clazz.getSimpleName());
        }
    }


    public <T> T getCardData(String key, Class<T> clazz, Class cardClass) {
        if(cardDataMap.get(cardClass) == null){
            cardDataMap.put(cardClass , new HashMap<String, Object>());
        }
        Object data = cardDataMap.get(cardClass).get(key);
        if(data == null){
            return null;
        }
        if (clazz.isInstance(data)) {
            return (T) data;
        } else {
            throw new ClassCastException("data is not class:" + clazz.getSimpleName());
        }
    }

    public void onSaveInstance(Bundle bundle) {
        Set<String> unfitPublicSet = new HashSet<>();
        for (Map.Entry<String, Object> entry : publicDataMap.entrySet()) {
            if (!(entry.getValue() instanceof Serializable)) {
                unfitPublicSet.add(entry.getKey());
            }
        }
        for (String key : unfitPublicSet) {
            publicDataMap.remove(key);
        }
        Set<Pair<Class, String>> unfitPrivateSet = new HashSet<>();
        for (Map.Entry<Class, Map<String, Object>> entryLevel1 : cardDataMap.entrySet()) {
            for (Map.Entry<String, Object> entryLevel2 : entryLevel1.getValue().entrySet()) {
                if (!(entryLevel2.getValue() instanceof Serializable)) {
                    unfitPrivateSet.add(new Pair<Class, String>(entryLevel1.getKey(), entryLevel2.getKey()));
                }
            }
        }
        for (Pair<Class, String> pair : unfitPrivateSet) {
            cardDataMap.get(pair.first).remove(pair.second);
        }
        bundle.putSerializable(PUBLIC_KEY, (Serializable) publicDataMap);
        bundle.putSerializable(CARD_KEY, (Serializable) cardDataMap);
    }

    public void onRestoreInstance(Bundle bundle) {
        try {
            Map<String, Object> m1 = (Map<String, Object>) bundle.getSerializable(PUBLIC_KEY);
            Map<Class, Map<String, Object>> m2 = (Map<Class, Map<String, Object>>) bundle.getSerializable(CARD_KEY);
            publicDataMap.clear();
            cardDataMap.clear();
            publicDataMap.putAll(m1);
            cardDataMap.putAll(m2);
        } catch (ClassCastException e) {
            //just catch
        }

    }


}
