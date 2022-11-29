package com.lixi.shoolcard.pages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lixi.shoolcard.databinding.CardRechardLayoutBinding;
import com.lixi.shoolcard.network.vo.CardRechargeVo;
import com.lixi.shoolcard.utils.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

public class CardRechargeListAdapter extends BaseAdapter {
    private Context context;
    private List<CardRechargeVo> list = new ArrayList<>();

    public CardRechargeListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<CardRechargeVo> list, boolean isMore) {
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
    public CardRechargeVo getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CardRechardLayoutBinding binding  = CardRechardLayoutBinding.inflate(LayoutInflater.from(context));
        CardRechargeVo result = getItem(i);
        binding.amount.setText(result.getAmount());
        binding.userName.setText(result.getUserName());
        binding.gradeName.setText(result.getGradeName());
        binding.className.setText(result.getClassName());
        binding.transTime.setText(result.getTransTime());
        binding.accName.setText(result.getAccname());
        binding.remain.setText(result.getOddfare());
        binding.walletType.setText(ConvertUtil.convertWalletType(result.getWalletType()));
        return binding.getRoot();
    }
}
