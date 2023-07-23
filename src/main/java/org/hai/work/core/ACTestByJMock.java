package org.hai.work.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.hai.work.domain.BaseApiDO;
import org.hai.work.domain.util.CommonUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hai.work.domain.constant.Constant.MAIN_FILE_PATH;

public class ACTestByJMock {

    public static void main(String[] args) throws Exception {

        String filePath = MAIN_FILE_PATH;
        String fileName = "BaseApiDO";

        Map<String, List<String>> realMethodParamMap = new HashMap<>();
        List<String> fieldNameList = new ArrayList<>();
        List<Method> methodList = new ArrayList<>();
        StringBuffer testStr = new StringBuffer();

        // 先获取该类
        Class<?> clazz = BaseApiDO.class;
        // 获取所有的方法（含set/get）
        Method[] declaredMethods = clazz.getDeclaredMethods();
        // 排除（set/get）
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            fieldNameList.add(declaredField.getName());
        }

        try {
            // 读取Java源代码文件
            FileInputStream file = new FileInputStream(filePath);
            JavaParser javaParser = new JavaParser();
            // 解析Java源代码文件
            List<MethodDeclaration> methods = javaParser.parse(file).getResult().get().getTypes().get(0).getMethods();
            for (MethodDeclaration method : methods) {
                // 若是set/get方法 则不进行处理
                if (fieldNameList.contains(CommonUtil.methodName(method.getNameAsString()))) {
                    continue;
                }
                System.out.println("方法的名字是" + method.getNameAsString());
                // 获取方法的入参
                method.getParameters().forEach(parameter -> System.out.println("参数类型有" + parameter.toString())
                );

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 存放真正需要测试的方法
        for (Method declaredMethod : declaredMethods) {
            if (!fieldNameList.contains(CommonUtil.methodName(declaredMethod.getName()))) {
                methodList.add(declaredMethod);
            }
        }
        int index = 0;
        // 对方法构建对应的单元测试代码
        for (Method method : methodList) {
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            //String simpleName = exceptionTypes[0].getSimpleName();
            // DO中方法的名称
            String methodName = method.getName();
            // 方法中参数的类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 返回结果的类型
            Class<?> returnType = method.getReturnType();
            // 方法参数有几个
            Parameter[] parameters = method.getParameters();
            int parametersSize = parameters.length;
            if (parameterTypes.length != parameters.length) {
                throw new Exception(methodName + "方法的入参数有误");
            }
            // 单元测试固定前缀
            testStr.append(CommonUtil.prefixStr(methodName, index));
            // 有入参
            if (parametersSize != 0) {
                // 把所有入参数mock一下
                for (int i = 0; i < parameterTypes.length; i++) {
                    System.out.println(parameterTypes[i].getSimpleName());
                }


            } else { // 无入参数


            }
            // 单元测试固定后缀
            testStr.append(CommonUtil.suffixStr());
            index++;

        }
        // 创建测试文件
        System.out.println(testStr);
        CommonUtil.createTestFile(testStr, clazz);
    }
}
