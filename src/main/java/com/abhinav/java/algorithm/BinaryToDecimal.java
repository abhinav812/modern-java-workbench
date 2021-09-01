package com.abhinav.java.algorithm;

public class BinaryToDecimal {
    public Integer binaryToDecimal(String binaryStr) {
        int conversion = 1;
        int result = 0;
        int length = binaryStr.length();
        for (int i = 1; i <= length; i++) {
            if (binaryStr.charAt(length - i) == '1') {
                result += conversion;
            }
            conversion *= 2;
        }
        return result;
    }

    public Integer binaryToDecimal2(String binaryStr) {
        int result = 0;
        int length = binaryStr.length();
        for (int i = 1; i <= length; i++) {
            if (binaryStr.charAt(length - i) == '1') {
                result += Math.pow(2, i - 1);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        BinaryToDecimal binaryToDecimal = new BinaryToDecimal();
        System.out.println(Integer.parseInt("10110", 2));
        System.out.println(binaryToDecimal.binaryToDecimal("10110"));
        System.out.println(binaryToDecimal.binaryToDecimal2("10110"));
    }
}
