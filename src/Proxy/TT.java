package Proxy;

public class TT {
    public static void main(String[] args) {
        MyProxy myProxy = new MyProxy();
        TestIn test =  myProxy.create(TestIn.class);
        test.setA("a");
        System.out.println(test.getA());


    }
}
