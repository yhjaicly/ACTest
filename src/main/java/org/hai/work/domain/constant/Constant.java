package org.hai.work.domain.constant;

import java.util.Arrays;
import java.util.List;

public class Constant {

    /**
     * 需要生成单元测试的DO文件地址
     */
    public final static String MAIN_FILE_PATH = "/Users/mr.ahai/IdeaProjects/ACTest/src/main/java/org/hai/work/domain/BaseApiDO.java";

    /**
     * 生成后存放TEST文件地址
     */
    public final static String TEST_FILE_PATH = "/Users/mr.ahai/IdeaProjects/ACTest/src/test/java/org/hai/work/domain/";

    /**
     * 基本类型（String 可以直接创建，故放在这里不要抬杠）
     */
    public final static List<String> BASIC_TYPE_LIST = Arrays.asList("int", "java.lang.String", "short", "boolean", "byte", "char", "float", "double", "long");

    /**
     * 包装类型
     */
    public final static List<String> BASIC_CLASS_TYPE_LIST = Arrays.asList("Integer", "Short", "Boolean", "Byte", "Character", "Float", "Double", "Long");

    public final static List<String> COLLECTION_LIST = Arrays.asList("List","Set","Map");

    /**
     * 基本类型的默认值
     */
    public final static int INT_DEFAULT_VALUE = 0;
    public final static String STRING_DEFAULT_VALUE = "a";
    public final static short SHORT_DEFAULT_VALUE = 1;
    public final static boolean BOOLEAN_DEFAULT_VALUE = true;
    public final static byte BYTE_DEFAULT_VALUE = 1;
    public final static char CHAR_DEFAULT_VALUE = 'a';
    public final static float FLOAT_DEFAULT_VALUE = 0.1f;
    public final static double DOUBLE_DEFAULT_VALUE = 1.1d;
    public final static long LONG_DEFAULT_VALUE = 1L;

    public final static String LANG_STRING = "java.lang.String";
    public final static String STRING = "String";
    public final static String CHAR = "char";

}
