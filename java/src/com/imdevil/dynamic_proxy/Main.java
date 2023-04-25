package com.imdevil.dynamic_proxy;

public class Main {
    public static void main(String[] args) {
        HelloImp helloImp = new HelloImp();
        HelloInterface proxy = LogProxy.getInstance(helloImp);
        proxy.hello(null);
    }
}
