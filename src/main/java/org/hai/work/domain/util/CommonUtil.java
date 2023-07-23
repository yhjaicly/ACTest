package org.hai.work.domain.util;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.apache.commons.lang3.StringUtils;
import org.hai.work.domain.constant.Constant;
import org.hai.work.domain.enumeration.BasicTypeEnum;

import java.io.*;

public class CommonUtil {

    /**
     * 若为set/get方法，则返回字段名称
     * 例：setAge -> age
     *
     * @param methodName
     * @return
     */
    public static String methodName(String methodName) {

        if (methodName.startsWith("set") || methodName.startsWith("get")) {
            String substring = methodName.substring(3);
            // 首字母转小写
            return StringUtils.uncapitalize(substring);
        }
        return methodName;
    }

    /**
     * 生成单元测试的前缀
     *
     * @param methodName
     * @param index
     * @return
     */
    public static StringBuffer prefixStr(String methodName, int index) {
        StringBuffer sb = new StringBuffer();
        sb.append("\t@Test\n\tpublic void ").append(methodName).append(index).append("() {\n\t\t");
        return sb;
    }

    /**
     * 生成单元测试的后缀
     *
     * @return
     */
    public static StringBuffer suffixStr() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n").append("\t").append("}").append("\n");
        return sb;
    }

    /**
     * 获取基础信息的默认值
     *
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        if (Constant.LANG_STRING.equals(key)) {
            key = Constant.STRING;
        }
        BasicTypeEnum[] basicTypeEnums = BasicTypeEnum.values();
        for (BasicTypeEnum basicTypeEnum : basicTypeEnums) {
            if (key.equals(basicTypeEnum.getKey())) {
                return basicTypeEnum.getValue();
            }
        }
        return null;
    }

    /**
     * 返回由集合创建的方式，例：
     * new ArrayList<>();
     * new Hash<>();
     * new HashSet<>();
     *
     * @param type List,Set,Map
     * @return
     */
    public static void getNewClass(String type, StringBuffer testStr) {
        if ("List".equals(type)) {
            testStr.append(" = new ArrayList<>();\n\t\t");
        }
        if ("Set".equals(type)) {
            testStr.append(" = new HashSet<>();\n\t\t");
        }
        if ("Map".equals(type)) {
            testStr.append(" = new Hash<>();\n\t\t");
        }
    }

    /**
     * 创建测试文件
     *
     * @param testStr
     * @param clazz
     * @throws IOException
     */
    public static void createTestFile(StringBuffer testStr, Class<?> clazz) throws IOException {
        // 创建一个文件，并写到文件里面
        File file = new File(Constant.TEST_FILE_PATH + clazz.getSimpleName() + "Test.java");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        // 添加类的头
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package org.hai.work.domain;\nimport org.testng.annotations.Test;\nimport org.testng.Assert;\n").append("public class ").append(clazz.getSimpleName()).append("Test {\n\n");
        stringBuffer.append(testStr).append(createObj(clazz)).append("\n}");
        bufferedWriter.write(stringBuffer.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
        fileWriter.close();
    }

    /**
     * 获取类的对象名称（TestDO testDO）
     *
     * @param clazz
     * @return
     */
    public static String getClazzName(Class<?> clazz) {
        String[] classNameStr = StringUtils.splitByWholeSeparator(clazz.getName(), ".");
        return classNameStr[classNameStr.length - 1];
    }

    /**
     * 获取错误码
     * @param throwMsg
     * @return
     */
    public static String errorCode(String throwMsg){
        String errorMsg = throwMsg.split("\\(")[1];
        errorMsg = errorMsg.split("\\)")[0];
        if (errorMsg.contains(",")){
            return errorMsg.split(",")[0];
        }
        return errorMsg.replace("\"","");
    }

    /**
     * 判断方法中是否存在异常关键字
     * @param fileName
     * @param filePath
     * @param methodName
     * @return
     * @throws FileNotFoundException
     */
    public static boolean isHaveThrow(String fileName, String filePath, String methodName) throws FileNotFoundException {
        File file = new File(filePath);
        CompilationUnit cu = StaticJavaParser.parse(file);
        MethodDeclaration method = cu.getClassByName(fileName).get().getMethodsByName(methodName).get(0);
        String text = method.getBody().get().toString();
        return text.contains("throw");
    }

    public static String createObj(Class<?> clazzName){
        StringBuffer sb = new StringBuffer();
        sb.append("\tprivate ").append(getClazzName(clazzName)).append(" ")
                .append(StringUtils.uncapitalize(getClazzName(clazzName))).append(" {")
                .append("\n\t\treturn new ").append(StringUtils.uncapitalize(getClazzName(clazzName))).append("();").append("\n\t}");
        return sb.toString();
    }

}
