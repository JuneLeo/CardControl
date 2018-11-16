package com.poseidon.control.control;

import android.util.Pair;

import java.util.List;

/**
 * Created by spf on 2018/11/15.
 */
class EventMessage {
    Pair<String, Object> pair;
    List<Pair<String, Object>> pairList;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(! (o instanceof EventMessage)){
            return false;
        }
        EventMessage bean = (EventMessage)o;
        if(pair == bean.pair ){
        }else if(pair != null && !pair.equals(bean.pair)){
            return false;
        }
        if(pairList == bean.pairList){

        }else if(pairList != null && !pairList.equals(bean.pairList)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(pair != null){
            return pair.toString();
        }else{
            return pairList.toString();
        }
    }
}
