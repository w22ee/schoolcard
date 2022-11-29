package com.lixi.shoolcard.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.lixi.shoolcard.R;
import com.lixi.shoolcard.databinding.GridItemLayoutBinding;

public class GridItemView extends LinearLayout {

    private GridItemLayoutBinding binding;

    public GridItemView(Context context) {
        super(context);
    }

    public GridItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public GridItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    public void initView(Context context, @Nullable AttributeSet attrs){

//       binding  =  GridItemLayoutBinding.inflate(LayoutInflater.from(context));
//        GridItemLayoutBinding.bind(binding.getRoot());
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout,GridItemView.this);
        //view.setPadding(20,20,20,20);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.griditem);
        String text = ta.getString(R.styleable.griditem_text);
        int iconUrl = ta.getResourceId(R.styleable.griditem_icon, -1);
        System.out.println("text"+text);
        System.out.println("iconUrl"+iconUrl);
        TextView textView = view.findViewById(R.id.grid_item_text);
        ImageView imageView  = view.findViewById(R.id.grid_item_icon);
        textView.setText(text);
        imageView.setImageResource(iconUrl);
//        binding.gridItemIcon.setImageResource(iconUrl);
//        binding.gridItemText.setText(text);
        ta.recycle();
    }
}
