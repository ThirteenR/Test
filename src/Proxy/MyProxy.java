package Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyProxy {
    public <T> T create(final Class<T> clazz){
         return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
             @Override
             public Object invoke(Object proxy, Method method, Object[] args) {



                 return method.getName();
             }
         });
    }
}
