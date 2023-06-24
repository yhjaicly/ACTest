package org.hai.work.core;

import org.apache.commons.lang3.StringUtils;
import org.hai.work.domain.BaseApiDO;
import org.hai.work.domain.util.CommonUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hai.work.domain.constant.Constant.*;

public class ACTest {

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
                //  添加中间内容，先构建入参
                List<String> realParamList = new CopyOnWriteArrayList<>();
                for (int i = 0; i < parametersSize; i++) {
                    // 基本类型的话，直接创建默认值
                    if (BASIC_TYPE_LIST.contains(parameterTypes[i].getTypeName())) {
                        // String 特别处理
                        if (LANG_STRING.equals(parameterTypes[i].getTypeName())) {
                            testStr.append("String ").append(parameters[i].getName());
                            testStr.append(" = \"");
                            testStr.append(CommonUtil.getValue(parameterTypes[i].getTypeName())).append("\";\n\t\t");
                        }
                        // char 特别处理
                        else if (CHAR.equals(parameterTypes[i].getTypeName())) {
                            testStr.append(parameterTypes[i].getTypeName()).append(" ").append(parameters[i].getName());
                            testStr.append(" = '");
                            testStr.append(CommonUtil.getValue(parameterTypes[i].getTypeName())).append("';\n\t\t");
                        } else {
                            testStr.append(parameterTypes[i].getTypeName()).append(" ").append(parameters[i].getName());
                            testStr.append(" = ");
                            testStr.append(CommonUtil.getValue(parameterTypes[i].getTypeName())).append(";\n\t\t");
                        }
                        // 其他所有类
                    } else {
                        // 类的话，直接创建一个空对象，并对类中的属性进行默认值赋值(要不只建一个空对象算了), 只考虑类，别的就算了，不能创建对象的类暂不考虑
                        // 参数的类型
                        String[] objType = StringUtils.splitByWholeSeparator(parameterTypes[i].getTypeName(), ".");
                        String objTypeName = objType[objType.length - 1];
                        // 如果是集合特殊处理
                        if (COLLECTION_LIST.contains(objTypeName)) {
                            testStr.append(objTypeName).append(" ").append(parameters[i].getName());
                            CommonUtil.getNewClass(objTypeName, testStr);
                        } else {
                            testStr.append(objTypeName).append(" ").append(parameters[i].getName());
                            testStr.append(" = new ").append(objTypeName).append("();\n\t\t");
                        }
                    }

                    realParamList.add(parameters[i].getName());
                }
                realMethodParamMap.put(methodName, realParamList);

                // 有返回值
                if (!returnType.getSimpleName().equals("void")) {
                    // 入参构建完毕，开始构建要执行的测试方法，并把参数塞进去
                    String[] classNameStr = StringUtils.splitByWholeSeparator(clazz.getName(), ".");
                    String className = classNameStr[classNameStr.length - 1];
                    // 先按所有的测试类都有无参数构造器
                    testStr.append(className).append(" ").append(StringUtils.uncapitalize(className))
                            .append(" = new ").append(className).append("();\n\t\t");
                    // 调用方法的返回对象
                    testStr.append(returnType.getSimpleName()).append(" ").append("test").append(StringUtils.capitalize(returnType.getSimpleName()));
                    testStr.append(" = ").append(StringUtils.uncapitalize(className)).append(".").append(methodName)
                            .append("(");
                    for (Parameter parameter : parameters) {
                        testStr.append(parameter.getName()).append(", ");
                    }
                    // 删除最后一个,
                    testStr.deleteCharAt(testStr.length() - 1);
                    testStr.deleteCharAt(testStr.length() - 1);
                    testStr.append(");\n\t\t");
                    testStr.append("Assert.assertTrue(true);\n\t\t");
                } else {
                    // 无返回值 多半是要抛出异常
                    // void 返回值，一般是抛出异常，捕获一下
                    // 入参构建完毕，开始构建要执行的测试方法，并把参数塞进去
                    String[] classNameStr = StringUtils.splitByWholeSeparator(clazz.getName(), ".");
                    String className = classNameStr[classNameStr.length - 1];
                    // 先按所有的测试类都有无参数构造器
                    testStr.append(className).append(" ").append(StringUtils.uncapitalize(className))
                            .append(" = new ").append(className).append("();\n\t\t");
                    // 判断是否需要抛出异常信息
                    boolean isExection = CommonUtil.isHaveThrow(fileName, filePath, methodName);
                    if (isExection) {
                        testStr.append("try {\n\t\t\t");
                        testStr.append(StringUtils.uncapitalize(className)).append(".").append(methodName).append("(");
                        for (String realParam : realParamList) {
                            testStr.append(realParam).append(", ");
                        }
                        testStr.delete(testStr.length() - 2, testStr.length()).append(");\n\t\t\t");
                        testStr.append("Assert.fail();\n\t\t} catch (Exception e) {\n\t\t\tAssert.assertEquals(e.getMessage(), \"ERROR_CODE\");\n\t\t}");

                    } else {
                        // 不抛出异常，大多单元测试是用来赋值
                        testStr.append(StringUtils.uncapitalize(className)).append(".").append(methodName).append("(");
                        for (String realParam : realParamList) {
                            testStr.append(realParam).append(", ");
                        }
                        testStr.delete(testStr.length() - 2, testStr.length()).append(");\n\t\t");
                        testStr.append("Assert.assertThrows(null); // 无返回值，无抛出异常");
                    }

                }

            } // Boolean 还需要特殊处理 （用得少，暂时就先不写了） TODO
            else {
                // 无入参数的话，一般是为了创建一个对象，或是通过DO注入对象（充血模型）进行处理
                // 1.创建一个对象，有返回值
                if (!returnType.getSimpleName().equals("void")) {

                    // 入参构建完毕，开始构建要执行的测试方法，并把参数塞进去
                    String[] classNameStr = StringUtils.splitByWholeSeparator(clazz.getName(), ".");
                    String className = classNameStr[classNameStr.length - 1];
                    // 先按所有的测试类都有无参数构造器
                    testStr.append(className).append(" ").append(StringUtils.uncapitalize(className))
                            .append(" = new ").append(className).append("();\n\t\t");
                    testStr.append("Assert.assertNotNull(")
                            .append(StringUtils.uncapitalize(className)).append(".").append(methodName).append("());\n\t\t");
                }
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
