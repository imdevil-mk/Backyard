package com.imdevil.dynamic_proxy;

import java.util.List;
import java.util.Locale;

public class HelloImp implements HelloInterface{


    String s = "FUCK";

    String b = s.toLowerCase(Locale.ROOT);

    @Override
    public List<List<String>> hello(List<List<Integer>> a) {
        System.out.println("i`m HelloImp" + " " + s + " " + b);
        return null;
    }
}
