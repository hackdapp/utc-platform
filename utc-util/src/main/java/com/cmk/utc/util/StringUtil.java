package com.cmk.utc.util;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Title StringUtils.java 字符串工具类
 * @author zhangliang
 * @Description:
 * @version $Rev$
 * @LastModify: $Id$
 */
public class StringUtil {
    /** 日志记录 */

    /**
     * 将字符串str中的str1替换为str2
     * 
     * @param str str
     * @param str1 str1
     * @param str2 str2
     * @return 替换后的结果
     */
    public static String replace(String str, String str1, String str2) {
        return org.apache.commons.lang3.StringUtils.replace(str, str1, str2);
    }

    /**
     * 匹配前缀字符是否相同
     * 
     * @param str 源字符串
     * @param prefix 目标字符串
     * @return 前缀字符相同与否
     */
    public static boolean startWithIgnoreCase(String str, String prefix) {
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.substring(0, prefix.length()).equalsIgnoreCase(prefix);
    }

    /**
     * 对应解开客户端进行简单加密的字符串，进一步提高系统的安全性 原理：对应客户端加密的字符串进行拆解，转为Unicode对应的数字，对每一个数字进行恢复的反向调整。
     * 
     * @param src 原加密字符串
     * @return String 解密后的字符串
     */
    public static String unEscapeStr(String src) {
        String ret = "";

        if (src == null) {
            return ret;
        }

        for (int i = src.length() - 1; i >= 0; i-- ) {
            int iCh = src.substring(i, i + 1).hashCode();

            if (iCh == 15) {
                iCh = 10;
            } else if (iCh == 16) {
                iCh = 13;
            } else if (iCh == 17) {
                iCh = 32;
            } else if (iCh == 18) {
                iCh = 9;
            } else {
                iCh = iCh - 5;
            }

            ret += (char)iCh;
        }
        return ret;
    }

    /**
     * 加密字符串，进一步提高系统的安全性
     * 
     * @param src 未加密字符串
     * @return String 加密后的字符串
     */
    public static String escapeStr(String src) {
        String ret = "";

        if (src == null) {
            return ret;
        }

        for (int i = src.length() - 1; i >= 0; i-- ) {
            int iCh = src.substring(i, i + 1).hashCode();

            if (iCh == 10) {
                iCh = 15;
            } else if (iCh == 13) {
                iCh = 16;
            } else if (iCh == 32) {
                iCh = 17;
            } else if (iCh == 9) {
                iCh = 18;
            } else {
                iCh = iCh + 5;
            }

            ret += (char)iCh;
        }
        return ret;
    }

    /**
     * 加密方法，
     * 
     * @param str 加密对象
     * @param encoding 字符集
     * @return 加密后的对象
     */
    public static String encodeStr(String str, String encoding) {
        try {
            return URLEncoder.encode(str, encoding);
        } catch (Exception e) {
            return str;
        }
    }

    public static String[] chineseDigits = new String[] {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒",
        "捌", "玖"};

    /**
     * 把金额转换为汉字表示的数量，小数点后四舍五入保留两位
     * 
     * @param amount
     * @return
     */
    public static String amountToChinese(double amount) {

        if (amount > 99999999999999.99 || amount < -99999999999999.99)
            throw new IllegalArgumentException(
                "参数值超出允许范围 (-99999999999999.99 ～ 99999999999999.99)！");

        boolean negative = false;
        if (amount < 0) {
            negative = true;
            amount = amount * (-1);
        }

        long temp = Math.round(amount * 100);
        int numFen = (int)(temp % 10); // 分
        temp = temp / 10;
        int numJiao = (int)(temp % 10); // 角
        temp = temp / 10;
        // temp 目前是金额的整数部分

        int[] parts = new int[20]; // 其中的元素是把原来金额整数部分分割为值在 0~9999 之间的数的各个部分
        int numParts = 0; // 记录把原来金额整数部分分割为了几个部分（每部分都在 0~9999 之间）
        for (int i = 0;; i++ ) {
            if (temp == 0)
                break;
            int part = (int)(temp % 10000);
            parts[i] = part;
            numParts++ ;
            temp = temp / 10000;
        }

        boolean beforeWanIsZero = true; // 标志“万”下面一级是不是 0

        String chineseStr = "";
        for (int i = 0; i < numParts; i++ ) {

            String partChinese = partTranslate(parts[i]);
            if (i % 2 == 0) {
                if ("".equals(partChinese))
                    beforeWanIsZero = true;
                else
                    beforeWanIsZero = false;
            }

            if (i != 0) {
                if (i % 2 == 0)
                    chineseStr = "亿" + chineseStr;
                else {
                    if ("".equals(partChinese) && !beforeWanIsZero) // 如果“万”对应的
                        // part 为
                        // 0，而“万”下面一级不为
                        // 0，则不加“万”，而加“零”
                        chineseStr = "零" + chineseStr;
                    else {
                        if (parts[i - 1] < 1000 && parts[i - 1] > 0) // 如果"万"的部分不为
                            // 0,
                            // 而"万"前面的部分小于
                            // 1000 大于
                            // 0，
                            // 则万后面应该跟“零”
                            chineseStr = "零" + chineseStr;
                        chineseStr = "万" + chineseStr;
                    }
                }
            }
            chineseStr = partChinese + chineseStr;
        }

        if ("".equals(chineseStr)) // 整数部分为 0, 则表达为"零元"
            chineseStr = chineseDigits[0];
        else if (negative) // 整数部分不为 0, 并且原金额为负数
            chineseStr = "负" + chineseStr;

        chineseStr = chineseStr + "元";

        if (numFen == 0 && numJiao == 0) {
            chineseStr = chineseStr + "整";
        } else if (numFen == 0) { // 0 分，角数不为 0
            chineseStr = chineseStr + chineseDigits[numJiao] + "角";
        } else { // “分”数不为 0
            if (numJiao == 0)
                chineseStr = chineseStr + "零" + chineseDigits[numFen] + "分";
            else
                chineseStr = chineseStr + chineseDigits[numJiao] + "角" + chineseDigits[numFen]
                             + "分";
        }

        return chineseStr;

    }

    /**
     * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""
     * 
     * @param amountPart
     * @return
     */
    private static String partTranslate(int amountPart) {

        if (amountPart < 0 || amountPart > 10000) {
            throw new IllegalArgumentException("参数必须是大于等于 0，小于 10000 的整数！");
        }

        String[] units = new String[] {"", "拾", "佰", "仟"};

        int temp = amountPart;

        String amountStr = new Integer(amountPart).toString();
        int amountStrLength = amountStr.length();
        boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
        String chineseStr = "";

        for (int i = 0; i < amountStrLength; i++ ) {
            if (temp == 0) // 高位已无数据
                break;
            int digit = temp % 10;
            if (digit == 0) { // 取到的数字为 0
                if (!lastIsZero) // 前一个数字不是 0，则在当前汉字串前加“零”字;
                    chineseStr = "零" + chineseStr;
                lastIsZero = true;
            } else { // 取到的数字不是 0
                chineseStr = chineseDigits[digit] + units[i] + chineseStr;
                lastIsZero = false;
            }
            temp = temp / 10;
        }
        return chineseStr;
    }

    public static boolean contains(String s, String text, String delimiter) {
        if ((s == null) || (text == null) || (delimiter == null)) {
            return false;
        }

        if (!s.endsWith(delimiter)) {
            s += delimiter;
        }

        int pos = s.indexOf(delimiter + text + delimiter);

        if (pos == -1) {
            if (s.startsWith(text + delimiter)) {
                return true;
            }

            return false;
        }

        return true;
    }

    public static int count(String s, String text) {
        if ((s == null) || (text == null)) {
            return 0;
        }

        int count = 0;

        int pos = s.indexOf(text);

        while (pos != -1) {
            pos = s.indexOf(text, pos + text.length());
            count++ ;
        }

        return count;
    }

    public static String merge(String array[], String delimiter) {
        if (array == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < array.length; i++ ) {
            sb.append(array[i].trim());

            if ((i + 1) != array.length) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }

    public static String read(ClassLoader classLoader, String name) throws IOException {

        return read(classLoader.getResourceAsStream(name));
    }

    public static String read(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuffer sb = new StringBuffer();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }

        br.close();

        return sb.toString().trim();
    }

    public static String remove(String s, String remove, String delimiter) {
        if ((s == null) || (remove == null) || (delimiter == null)) {
            return null;
        }

        if (!CommonUtil.isNullOrEmpty(s) && !s.endsWith(delimiter)) {
            s += delimiter;
        }

        while (contains(s, remove, delimiter)) {
            int pos = s.indexOf(delimiter + remove + delimiter);

            if (pos == -1) {
                if (s.startsWith(remove + delimiter)) {
                    s = s.substring(remove.length() + delimiter.length(), s.length());
                }
            } else {
                s = s.substring(0, pos)
                    + s.substring(pos + remove.length() + delimiter.length(), s.length());
            }
        }

        return s;
    }

    public static String shorten(String s) {
        return shorten(s, 20);
    }

    public static String shorten(String s, int length) {
        return shorten(s, length, "..");
    }

    public static String shorten(String s, String suffix) {
        return shorten(s, 20, suffix);
    }

    public static String shorten(String s, int length, String suffix) {
        if (s == null || suffix == null) {
            return null;
        }

        if (s.length() > length) {
            s = s.substring(0, length) + suffix;
        }

        return s;
    }

    public static String[] split(String s, String delimiter) {
        if (s == null || delimiter == null) {
            return new String[0];
        }

        if (!s.endsWith(delimiter)) {
            s += delimiter;
        }

        s = s.trim();

        if (s.equals(delimiter)) {
            return new String[0];
        }

        List nodeValues = new ArrayList();

        if (delimiter.equals("\n") || delimiter.equals("\r")) {
            try {
                BufferedReader br = new BufferedReader(new StringReader(s));

                String line = null;

                while ((line = br.readLine()) != null) {
                    nodeValues.add(line);
                }

                br.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            int offset = 0;
            int pos = s.indexOf(delimiter, offset);

            while (pos != -1) {
                nodeValues.add(s.substring(offset, pos));

                offset = pos + delimiter.length();
                pos = s.indexOf(delimiter, offset);
            }
        }

        return (String[])nodeValues.toArray(new String[0]);
    }

    public static boolean[] split(String s, String delimiter, boolean x) {
        String[] array = split(s, delimiter);
        boolean[] newArray = new boolean[array.length];

        for (int i = 0; i < array.length; i++ ) {
            boolean value = x;

            try {
                value = Boolean.valueOf(array[i]).booleanValue();
            } catch (Exception e) {}

            newArray[i] = value;
        }

        return newArray;
    }

    public static double[] split(String s, String delimiter, double x) {
        String[] array = split(s, delimiter);
        double[] newArray = new double[array.length];

        for (int i = 0; i < array.length; i++ ) {
            double value = x;

            try {
                value = Double.parseDouble(array[i]);
            } catch (Exception e) {}

            newArray[i] = value;
        }

        return newArray;
    }

    public static float[] split(String s, String delimiter, float x) {
        String[] array = split(s, delimiter);
        float[] newArray = new float[array.length];

        for (int i = 0; i < array.length; i++ ) {
            float value = x;

            try {
                value = Float.parseFloat(array[i]);
            } catch (Exception e) {}

            newArray[i] = value;
        }

        return newArray;
    }

    public static long[] split(String s, String delimiter, long x) {
        String[] array = split(s, delimiter);
        long[] newArray = new long[array.length];

        for (int i = 0; i < array.length; i++ ) {
            long value = x;

            try {
                value = Long.parseLong(array[i]);
            } catch (Exception e) {}

            newArray[i] = value;
        }

        return newArray;
    }

    public static short[] split(String s, String delimiter, short x) {
        String[] array = split(s, delimiter);
        short[] newArray = new short[array.length];

        for (int i = 0; i < array.length; i++ ) {
            short value = x;

            try {
                value = Short.parseShort(array[i]);
            } catch (Exception e) {}

            newArray[i] = value;
        }

        return newArray;
    }

    public static final String stackTrace(Throwable t) {
        String s = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintWriter(baos, true));
            s = baos.toString();
        } catch (Exception e) {}

        return s;
    }

    public static boolean startsWith(String s, char begin) {
        return startsWith(s, (new Character(begin)).toString());
    }

    public static boolean startsWith(String s, String begin) {
        if ((s == null) || (begin == null)) {
            return false;
        }

        if (begin.length() > s.length()) {
            return false;
        }

        String temp = s.substring(0, begin.length());

        if (temp.equalsIgnoreCase(begin)) {
            return true;
        } else {
            return false;
        }
    }

    public static String wrap(String text) {
        return wrap(text, 80);
    }

    public static String wrap(String text, int width) {
        if (text == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new StringReader(text));

            String s = "";

            while ((s = br.readLine()) != null) {
                if (s.length() == 0) {
                    sb.append("\n");
                } else {
                    while (true) {
                        int pos = s.lastIndexOf(' ', width);

                        if ((pos == -1) && (s.length() > width)) {
                            sb.append(s.substring(0, width));
                            sb.append("\n");

                            s = s.substring(width, s.length()).trim();
                        } else if ((pos != -1) && (s.length() > width)) {
                            sb.append(s.substring(0, pos));
                            sb.append("\n");

                            s = s.substring(pos, s.length()).trim();
                        } else {
                            sb.append(s);
                            sb.append("\n");

                            break;
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return sb.toString();
    }

    public static String getPassword(int length, String key) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++ ) {
            sb.append(key.charAt((int)(Math.random() * key.length())));
        }

        return sb.toString();
    }

    public static String getPassword(int length) {
        String key = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return getPassword(length, key);
    }

    /**
     * Encode a string using algorithm specified in web.xml and return the resulting encrypted
     * password. If exception, the plain credentials string is returned
     * 
     * @param password Password or other credentials to use in authenticating this username
     * @param algorithm Algorithm used to do the digest
     * @return encypted password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            System.err.print("Exception: " + e);

            return password;
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] encodedPassword = md.digest();

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < encodedPassword.length; i++ ) {
            if (((int)encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }

            buf.append(Long.toString((int)encodedPassword[i] & 0xff, 16));
        }

        return buf.toString();
    }

    public static String[][] sort(String[][] list) {
        int len = list.length;
        int min, comp;
        for (int i = 0; i < len; i++ ) {
            min = i;
            for (int j = i + 1; j < len; j++ ) {
                comp = compareArray(list[min], list[j]);
                if (comp == 1)
                    min = j;
            }
            swap(list, i, min);
        }
        return list;
    }

    private static int compareArray(String a[], String b[]) {
        if (a[0].compareTo(b[0]) > 0)
            return 1;
        else if (a[0].compareTo(b[0]) < 0)
            return -1;
        return 0;
    }

    private static void swap(String[][] list, int a, int b) {
        String[] alist = list[a].clone();
        for (int i = 0; i < list[b].length; i++ ) {
            list[a][i] = list[b][i];
        }
        for (int i = 0; i < alist.length; i++ ) {
            list[b][i] = alist[i];
        }
    }

    public static String firstCharUpper(String str) {
        if (!CommonUtil.isNullOrEmpty(str)) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return null;
    }

    public static String firstCharLower(String str) {
        if (!CommonUtil.isNullOrEmpty(str)) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
        return null;
    }

    public static String getDomain(String url) {
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher m = p.matcher(url);
        if (m.find()) {
            return m.group();
        }
        return null;
    }
}
