package controller;

import java.text.DecimalFormat;

public class FormatUtil {
    // Hàm format tiền tệ
    public static String formatCurrency(int amount) {
        return new DecimalFormat("#,###").format(amount);
    }

    // Nếu muốn thêm ký hiệu tiền
    public static String formatCurrencyWithSymbol(int amount) {
        return new DecimalFormat("#,###").format(amount) + " ₫";
    }
}
