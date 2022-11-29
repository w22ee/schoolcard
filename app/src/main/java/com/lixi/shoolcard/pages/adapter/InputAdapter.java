package com.lixi.shoolcard.pages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lixi.shoolcard.R;
import com.lixi.shoolcard.databinding.DownListItemLayoutBinding;
import com.lixi.shoolcard.databinding.RechardBillLayoutBinding;
import com.lixi.shoolcard.network.vo.RechargeVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 楼栋，楼层下拉框adapter
 */
public class InputAdapter extends BaseAdapter {
    private Context context;
    private List<String> list = new ArrayList<>();

    public InputAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DownListItemLayoutBinding binding  = DownListItemLayoutBinding.inflate(LayoutInflater.from(context));
        if(view == null){
            binding  = DownListItemLayoutBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        }else {
            binding = (DownListItemLayoutBinding) view.getTag();
        }
        binding.downName.setText(getItem(i));
        return view;
    }
}
