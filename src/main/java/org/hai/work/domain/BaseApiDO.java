package org.hai.work.domain;

/**
 * @author mr.ahai
 *
 * 测试的目标DO
 */
public class BaseApiDO {
    public int a() {
        System.out.println("1");
        return 0;
    }

    public int b(int a) {
        return a;
    }

    public int c(int a, int b) {
        return a + b;
    }

    public int d() {
        throw new RuntimeException("1");
    }

    public int e(int a) {
        throw new RuntimeException("1");
    }

    public void f(int a) {
        throw new RuntimeException("1");
    }
}
