package com.lixi.shoolcard.pages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.lixi.shoolcard.databinding.FitlerItemLayoutBinding;
import com.lixi.shoolcard.network.vo.RoomVo;

import java.util.ArrayList;
import java.util.List;

public class RechardFilterAdaper extends BaseAdapter implements Filterable {
    //搜索过滤后的数据
    private List<RoomVo> keys = new ArrayList<>();
    //所有数据
    private List<RoomVo> okeys = new ArrayList<>();

    private Context context;

    public List<RoomVo> getKeys() {
        return keys;
    }

    public List<RoomVo> getOkeys() {
        return okeys;
    }

    public void reSetKeys(List<RoomVo> keys) {
        this.keys = keys;
        notifyDataSetChanged();
    }

    public void setKeys(List<RoomVo> keys) {
        this.keys = keys;
        this.okeys = keys;
        notifyDataSetChanged();
    }

    public RechardFilterAdaper(List<RoomVo> keys, Context context) {
        this.keys = keys;
        this.okeys = keys;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(keys!=null){
            return keys.size();
        }
        return 0;
    }

    @Override
    public RoomVo getItem(int i) {
        return keys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FitlerItemLayoutBinding fitlerItemLayoutBinding = FitlerItemLayoutBinding.inflate(LayoutInflater.from(context));
        fitlerItemLayoutBinding.name.setText(getItem(i).getRoomName());
        View view1 = fitlerItemLayoutBinding.getRoot();
        view1.setTag(getItem(i));
        return view1;
    }

    private ArrayFilter arrayFilter;

    @Override
    public Filter getFilter() {
        if(arrayFilter == null){
            arrayFilter = new ArrayFilter(RechardFilterAdaper.this);
        }
        return arrayFilter;
    }
}
