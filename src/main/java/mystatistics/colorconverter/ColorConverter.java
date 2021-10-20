package mystatistics.colorconverter;

import java.util.Scanner;

public class ColorConverter {
    public static void main(String[] args) {
        System.out.println("Введите флаг(encode чтобы закодировать цвет в одно число, decode чтобы раскодировать цвет из одного числа в RGBA) и цвет RGBA. \nПример: 255 0 0 255 encode");
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String mode = in.next();
            if(mode.equalsIgnoreCase("encode")) {
                int r = in.nextInt();
                int g = in.nextInt();
                int b = in.nextInt();
                int a = in.nextInt();
                int color = encodeColor(r, g, b, a);
                System.out.println("Закодированный цвет: " + color);
                continue;
            }
            if(mode.equalsIgnoreCase("decode")) {
                int color = in.nextInt();
                int[] rgba = decodeColor(color);
                System.out.println("Раскодированный цвет: R = " + rgba[0] + ", G = " + rgba[1] + ", B = " + rgba[2] + ", A = " + rgba[3]);
                continue;
            }
            System.out.println("Введите encode или decode.");
        }
        in.close();
    }

    private static int encodeColor(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
    }

    private static int[] decodeColor(int color) {
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        return new int[] { r, g, b, a };
    }
}
