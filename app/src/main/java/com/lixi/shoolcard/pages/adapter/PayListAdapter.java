package com.lixi.shoolcard.pages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lixi.shoolcard.R;
import com.lixi.shoolcard.databinding.PaymentLayoutBinding;
import com.lixi.shoolcard.network.vo.CardPayVo;
import com.lixi.shoolcard.utils.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

public class PayListAdapter extends BaseAdapter {
    private Context context;
    private List<CardPayVo> list = new ArrayList<>();

    public PayListAdapter(Context context) {
        this.context = context;
    }

    public List<CardPayVo> getList() {
        return list;
    }

    public void setList(List<CardPayVo> list,boolean isMore) {
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
    public CardPayVo getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PaymentLayoutBinding binding  = PaymentLayoutBinding.inflate(LayoutInflater.from(context));
        CardPayVo result = getItem(i);
        binding.userName.setText(result.getUserName());
        binding.gradeName.setText(result.getGradeName());
        binding.className.setText(result.getClassName());
        binding.amount.setText(result.getAmount());
        binding.transTime.setText(result.getTransTime());
        binding.remain.setText(result.getRemain());
        //binding.consumeType.setText(ConvertUtil.convertConsumeType());
        binding.walletType.setText(ConvertUtil.convertWalletType(result.getWalletType()));
        binding.payTerminal.setText(result.getOnsumeMchName());
        if (i % 2 == 0){
            binding.tableBg.setBackgroundColor(context.getResources().getColor(R.color.table_item));
        }else {
            binding.tableBg.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return binding.getRoot();
    }

}
