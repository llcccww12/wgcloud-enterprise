/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.wgcloud.dto.TuopuNodeDto;
import java.util.List;

public class TuopuUtil {
    public static void initList(List<TuopuNodeDto> list) {
        int centerX = 760;
        int centerY = 520;
        int radius = TuopuUtil.getRadius(list.size());
        int count = list.size();
        for (int i = 0; i < list.size(); ++i) {
            int x = centerX + (int)((double)radius * Math.cos(Math.PI * 2 / (double)count * (double)i));
            int y = centerY + (int)((double)radius * Math.sin(Math.PI * 2 / (double)count * (double)i));
            list.get(i).setX(x);
            list.get(i).setY(y);
        }
    }

    public static int getRadius(int count) {
        double disitem;
        double minr;
        double noder;
        if (count == 1) {
            count = 2;
        }
        if ((noder = (minr = 5.0 / Math.sin(0.5 * (disitem = 360.0 / (double)count) * Math.PI / 180.0)) * 1.5 + 15.0 + 80.0) < 260.0) {
            noder = 260.0;
        }
        return Integer.valueOf(String.valueOf(noder).substring(0, String.valueOf(noder).indexOf(".")));
    }
}

