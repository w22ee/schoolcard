package com.lixi.shoolcard.pages.adapter;

import android.util.Log;
import android.widget.Filter;

import com.lixi.shoolcard.network.vo.RoomVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入房间搜索的filter
 */
public class ArrayFilter extends Filter  {

    private RechardFilterAdaper adaper;

    public ArrayFilter(RechardFilterAdaper adaper) {
        this.adaper = adaper;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        Log.d("AutoCompleteTest",
                String.format("prefix = %s", charSequence));
        ArrayList<RoomVo> newData = new ArrayList<RoomVo>();
        if(charSequence == null){
            return  new FilterResults();
        }
        for(RoomVo i : adaper.getOkeys()){
            if(i.getRoomName().contains(charSequence)){
                newData.add(i);
//                if(newData.size() >= 10){
//                    break;
//                }
            }
        }
        FilterResults results = new FilterResults();
        results.count  = newData.size();
        results.values = newData;
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        try {
            System.out.println("charSequence" + charSequence);
            System.out.println("filterResults" + filterResults.values.toString());
        }catch (Exception e){}
        if(filterResults.count>0){
            adaper.reSetKeys((List<RoomVo>) filterResults.values);
            adaper.notifyDataSetChanged();
        }else {
            adaper.reSetKeys(new ArrayList<>());
            adaper.notifyDataSetChanged();
        }
    }
}
