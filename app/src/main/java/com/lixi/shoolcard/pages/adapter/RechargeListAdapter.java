package com.lixi.shoolcard.pages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lixi.shoolcard.R;
import com.lixi.shoolcard.databinding.RechardLayoutBinding;
import com.lixi.shoolcard.network.vo.RechargeVo;

import java.util.ArrayList;
import java.util.List;

public class RechargeListAdapter extends BaseAdapter {
    private Context context;
    private List<RechargeVo> list = new ArrayList<>();

    public RechargeListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<RechargeVo> list, boolean isMore) {
        if (isMore){
            this.list.addAll(list);
        }else {
            this.list = list;
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RechargeVo getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RechardLayoutBinding binding  = RechardLayoutBinding.inflate(LayoutInflater.from(context));
        RechargeVo result = getItem(i);
        binding.buildingName.setText(result.getBuildingName());
        binding.roomName.setText(result.getRoomName());
        //binding.electricBox.setText(result.getElectricBox());
        binding.userName.setText(result.getUserName());
        binding.gradeName.setText(result.getGradeName());
        binding.className.setText(result.getClassName());
        binding.amount.setText(result.getAmount());
        binding.transTime.setText(result.getTransTime());
        //binding.status.setText(result.getStatus()? "已到账" : "未到账");
        if (i % 2 == 0){
            binding.tableBg.setBackgroundColor(context.getResources().getColor(R.color.table_item));
        }else {
            binding.tableBg.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return binding.getRoot();
    }
}
