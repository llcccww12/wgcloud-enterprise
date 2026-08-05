/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import org.springframework.ui.Model;

public class PageUtil {
    public static void initPageNumber(PageInfo pageInfo, Model model) {
        ArrayList<String> pageNumbers = new ArrayList<String>();
        if (pageInfo.getPages() >= 10 && pageInfo.getPageNum() <= 5) {
            for (int i = 0; i < 10; ++i) {
                pageNumbers.add(1 + i + "");
            }
        } else {
            int i;
            for (i = 5; i > 0; --i) {
                if (pageInfo.getPageNum() - i <= 0) continue;
                pageNumbers.add(pageInfo.getPageNum() - i + "");
            }
            for (i = 0; i < 5; ++i) {
                if (pageInfo.getPageNum() + i > pageInfo.getPages()) continue;
                pageNumbers.add(pageInfo.getPageNum() + i + "");
            }
        }
        model.addAttribute("pageNumbers", pageNumbers);
    }
}

