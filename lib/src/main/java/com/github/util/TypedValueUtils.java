package com.github.util;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class TypedValueUtils {
    private TypedValueUtils() {
    }

    /**
     * @return The resource ID value in the {@code context} specified by {@code attr}. If it does
     * not exist, {@code fallbackAttr}.
     */
    public static int getAttr(@NonNull Context context, int attr, int fallbackAttr) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attr, value, true);
        if (value.resourceId != 0) {
            return attr;
        }
        return fallbackAttr;
    }
}
