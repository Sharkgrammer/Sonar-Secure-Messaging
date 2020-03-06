package com.shark.sonar.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shark.sonar.R;
import com.shark.sonar.controller.IconDbControl;
import com.shark.sonar.data.Icon;

import java.util.List;

public class IconPicker {
    private LinearLayout parent;
    private ImageView output;
    private TextView ID;
    private Context context;

    public IconPicker(LinearLayout l, ImageView o, TextView v, Context c) {
        parent = l;
        output = o;
        ID = v;
        context = c;

        populate();
    }

    public void populate() {

        IconDbControl con = new IconDbControl(context);
        List<Icon> list = con.selectAllIcons();
        con.destroy();

        LayoutInflater li = LayoutInflater.from(context);
        ImageView oneImg, twoImg, threeImg;
        LinearLayout oneLay, twoLay, threeLay;

        for (int x = 0; x < list.size(); x += 3) {
            View child = li.inflate(R.layout.item_single_icon, null);

            final Icon one = list.get(x);
            final Icon two = list.get(x + 1);
            final Icon three = list.get(x + 2);

            oneImg = child.findViewById(R.id.iconImage1);
            twoImg = child.findViewById(R.id.iconImage2);
            threeImg = child.findViewById(R.id.iconImage3);
            oneLay = child.findViewById(R.id.iconLay1);
            twoLay = child.findViewById(R.id.iconLay2);
            threeLay = child.findViewById(R.id.iconLay3);

            oneImg.setImageDrawable(one.getIcon());
            twoImg.setImageDrawable(two.getIcon());
            threeImg.setImageDrawable(three.getIcon());

            oneLay.setOnClickListener(view -> updateImageView(one));
            twoLay.setOnClickListener(view -> updateImageView(two));
            threeLay.setOnClickListener(view -> updateImageView(three));

            parent.addView(child);
        }
    }

    private void updateImageView(Icon i) {
        ID.setText(String.valueOf(i.getIcon_ID()));
        output.setImageDrawable(i.getIcon());
    }

}
