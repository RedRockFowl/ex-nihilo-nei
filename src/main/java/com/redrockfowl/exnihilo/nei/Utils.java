package com.redrockfowl.exnihilo.nei;

import net.minecraft.util.StatCollector;

public class Utils {

    public static String translate(String key, Object... objects) {
        String format = StatCollector.translateToLocal(ExNihiloNEI.MODID + "." + key);
        return String.format(format, objects);
    }

    /**
     * Replaces the last occurrence of the {@code regex} in {@code text} with
     * {@code replacement}.
     *
     * This method uses a complex regex. The opening {@code (?s)} turns on
     * {@code DOTALL} mode. Then the provided {@code regex} is matched. Then a
     * negative look ahead is opened with {@code (?!}. Then anything is
     * matched, reluctantly, and the provided {@code regex} is matched again.
     * @param text        the text to replace in
     * @param regex       the regex to match
     * @param replacement the text to replace the last match with
     * @return            the {@code text} with the last instance of {@code regex} replaced by {@code replacement}
     */
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

    public static String removeTrailingZeros(String text) {
        return replaceLast(text, "\\.?0+\\b", "");
    }

}
