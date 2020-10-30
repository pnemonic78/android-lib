package com.github.util;

import androidx.annotation.Nullable;

/**
 * String utilities.
 *
 * @author Moshe Waisberg
 */
public class StringUtils {

    private StringUtils() {
    }

    @Nullable
    public static String toString(CharSequence s) {
        return (s != null) ? s.toString() : null;
    }
}
