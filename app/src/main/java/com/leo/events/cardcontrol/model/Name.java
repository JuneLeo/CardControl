package com.leo.events.cardcontrol.model;

import android.content.Context;

import com.card.control.BaseCard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Name {
    public String group;
    public String name;


    public static List<BaseCard> transfrom(Context context) {
        List<Name> data = getData(context);
        if (data == null) {
            return null;
        }
        HashMap<String, List<NameCard>> map = new LinkedHashMap<>();
        for (Name name : data) {
            List<NameCard> names = map.get(name.group);
            if (names == null) {
                names = new ArrayList<>();
                map.put(name.group, names);
            }
            NameCard nameCard = new NameCard();
            nameCard.name = name.name;
            names.add(nameCard);
        }
        List<BaseCard> baseCards = new ArrayList<>();
        for (Map.Entry<String, List<NameCard>> entry : map.entrySet()) {
            GroupCard groupCard = new GroupCard();
            groupCard.group = entry.getKey();
            baseCards.add(groupCard);
            baseCards.addAll(entry.getValue());
        }

        return baseCards;
    }


    public static List<Name> getData(Context context) {
        String read = read(context);
        if (read == null) {
            return null;
        }
        return new Gson().fromJson(read, new TypeToken<List<Name>>() {
        }.getType());
    }


    public static String read(Context context) {
        try {
            InputStream open = context.getAssets().open("name.json");
            InputStreamReader inputStreamReader = new InputStreamReader(open);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
