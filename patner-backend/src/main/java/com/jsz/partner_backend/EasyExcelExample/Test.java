package com.jsz.partner_backend.EasyExcelExample;

import lombok.Data;

import java.util.Objects;

public class Test {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        System.out.println(Objects.equals(a.getA(),b.getB()));
        System.out.println(a.getA() == b.getB());
    }
}

@Data
class A {
    Long a = 156L;
}
@Data
class B{
    Long b = 156L;
}

