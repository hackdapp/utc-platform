package com.cmk.utc.util;


public final class Constant {
    /** 通用是否: 是 */
    public static final int INT_YES = 1;
    /** 通用是否: 否*/
    public static final int INT_NO = 2;
    /** 通用是否: 是*/
    public static final String STR_YES = "1";
    /** 通用是否: 否*/
    public static final String STR_NO = "2";

    /** 编码字段分隔符号 */
    public static final String CODE_SEPARATOR = "`";

    public static enum ModuleType {
        /** 系统模块 */
        SY("SY");
        /** value */
        private final String value;

        /**
         * Constructor
         * @param value value
         */
        ModuleType(String value) {
            this.value = value;
        }

        /**
         * Method toString.
         * @return String
         */
        @Override
        public String toString() {
            return this.value;
        }
    };
    
    
    public static final String PERMISSION_WHERE = "$PERMISSION_WHERE$";
	public static final String PERMISSION_BUTTON_WHERE = "$PERMISSION_BUTTON_WHERE$";
}
