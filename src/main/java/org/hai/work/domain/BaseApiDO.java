package org.hai.work.domain;

import org.hai.work.core.ACTest;

import java.util.List;

/**
 * @author mr.ahai
 *
 * 测试的目标DO
 */
public class BaseApiDO {

    private String a;

    private int b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int aaa(String a, Integer b, ACTest acTest, List<BaseApiDO> v){
        return 0;
    }

    public int kiss(BaseApiDO v){
        return 0;
    }
}
