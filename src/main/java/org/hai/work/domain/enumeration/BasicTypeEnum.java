package org.hai.work.domain.enumeration;

public enum BasicTypeEnum {
    hai_int("int", 1),
    hai_string("String", "a"),
    hai_short("short", 1),
    hai_boolean("boolean", true),
    hai_byte("byte", 1),
    hai_char("char", 'a'),
    hai_float("float", 0.1f),
    hai_double("double", 1.1d),
    hai_long("long", 1L);

    private String key;
    private Object value;

    BasicTypeEnum(String key, Object value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
