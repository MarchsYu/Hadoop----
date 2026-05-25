package hotel.common;

/**
 * 课程设计中常用的 CSV 字段清洗工具。
 */
public final class CsvCleaner {

    private CsvCleaner() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * 将类似“1000+消费”“900消费”“约1200”等文本解析为整数。
     * 解析失败时返回 -1。
     */
    public static int parseSalesCount(String raw) {
        if (isBlank(raw)) {
            return -1;
        }
        StringBuilder digits = new StringBuilder();
        char[] chars = raw.trim().toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                digits.append(c);
            }
        }
        if (digits.length() == 0) {
            return -1;
        }
        try {
            return Integer.parseInt(digits.toString());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * 解析正整数，失败返回 -1。
     */
    public static int parsePositiveInt(String raw) {
        if (isBlank(raw)) {
            return -1;
        }
        try {
            int value = Integer.parseInt(raw.trim());
            return value > 0 ? value : -1;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
