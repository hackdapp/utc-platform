package com.cmk.utc.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class StringUtils {
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

    public static String replace(String str, String str1, String str2) {
        return org.apache.commons.lang3.StringUtils.replace(str, str1, str2);
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

    public static String sortComlexKey(String[][] keyValue) {
        StringUtils.sort(keyValue);
        StringBuffer keyBuffer = new StringBuffer();
        int i = 0;
        for (String[] field : keyValue) {
            if (i++ != 0)
                keyBuffer.append("-");
            keyBuffer.append(field[1]);
        }
        return keyBuffer.toString();
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

    public static boolean startWithIgnoreCase(String str, String prefix) {
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.substring(0, prefix.length()).equalsIgnoreCase(prefix);
    }

    public static String firstCharUpper(String str) {
        if (!CommonUtil.isNullOrEmpty(str)) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return null;
    }
}
