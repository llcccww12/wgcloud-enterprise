/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.util.IdGeneratorSnowflake;
import java.util.Random;

public class UUIDUtil {
    private static IdGeneratorSnowflake idGeneratorSnowflake = ApplicationContextHelper.getBean(IdGeneratorSnowflake.class);

    public static String getUUID() {
        return idGeneratorSnowflake.snowflakeId() + "";
    }

    public static String getRandomSix() {
        return "" + new Random().nextInt(999999);
    }
}

