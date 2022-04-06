package util;

/**
 * @author wei
 * @description uppaal中的常量
 * @date 2022-04-06 19:03
 */
public class Uppaal {
    public static final int K = 10;
    public static final int INT16_MAX = 32767;
    public static final int INT16_MIN = -32768;

    private static int f(double x) {
        return (int) Math.round(x * K);
    }
}
