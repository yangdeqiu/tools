package com.ydq.tools.pattern;

import com.sun.codemodel.internal.JClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 范型测试类
 */
public class PatternDemo {
    private final static Logger log = LoggerFactory.getLogger(PatternDemo.class);

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 1.第一个范型例子
        List<String> sal = new ArrayList<>();
        List<Integer> ial = new ArrayList<>();
        if (sal.getClass().equals(ial.getClass()))
            log.info("== {} 与 {}，类型相同 ==", sal.getClass(), ial.getClass());
        // 2.
        //泛型的类型参数只能是类类型（包括自定义类），不能是简单类型
        //传入的实参类型需与泛型的类型参数类型相同，即为Integer.
        Generic1<Integer> genericInteger = new Generic1<>(123456);

        //传入的实参类型需与泛型的类型参数类型相同，即为String.
        Generic1<String> genericString = new Generic1<>("key_vlaue");
        log.info("泛型测试 key is {}", genericInteger.getKey());
        log.info("泛型测试 key is {}", genericString.getKey());

        // 3.范型参数
        Object obj = genericMethod(Class.forName("com.ydq.tools.pattern.Demo"));
        log.info(obj.getClass().toString());

        // 4.可变参数
        printMsg("111",222,"aaaa","2323.4",55.55);

        // 5.范型上界定义-1
        Map<String, ? super Father> map = new HashMap<>();
        map.put("father", new Father("f"));
        map.put("son", new Son("s"));
        log.info("{}", map);

        // 6.范型上界定义-2
        List<? super Father> fl = new ArrayList<>();
        fl.add(new Son(""));

        //泛型可以是看成容器里元素的尺子。
        //往容器里放，元素必须比每把尺子都要小；
        //取出的时候，必须用比最大的尺子还要大的引用来接收：
        Map<? super Object, ? super Object> map1 = new HashMap<>();
        map1.put("1", "2");
        
    }

    public static <T> List<T> makeList(T... args) {
        final ArrayList<T> ts = new ArrayList<>(Arrays.asList(args));
        return ts;
    }

    public static <K, V> Map<K, V> map() {
        return new HashMap<>();
    }

    public static <T> void printMsg( T... args){
        for(T t : args){
            log.info("泛型测试 t is {}", t.getClass());
        }
    }

    public static void showKeyValue1(Generic1<?> obj){
        log.info("泛型测试 key value is {}", obj.getKey());
    }

    /**
     * 泛型方法的基本介绍
     * @param tClass 传入的泛型实参
     * @return T 返回值为T类型
     * 说明：
     *     1）public 与 返回值中间<T>非常重要，可以理解为声明此方法为泛型方法。
     *     2）只有声明了<T>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
     *     3）<T>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
     *     4）与泛型类的定义一样，此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型。
     */
    public static <T> T genericMethod(Class<T> tClass) throws InstantiationException ,
            IllegalAccessException{
        T instance = tClass.newInstance();
        return instance;
    }

}

//此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
//在实例化泛型类时，必须指定T的具体类型
class Generic1<T> {
    //key这个成员变量的类型为T,T的类型由外部指定
    private T key;

    public Generic1(T key) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
        this.key = key;
    }

    public T getKey() { //泛型方法getKey的返回值类型为T，T的类型由外部指定
        return key;
    }
}

class Demo{}
class Father{
    public Father(String name){this.name = name;}
    protected String name;
}
class Son extends Father{
    public Son(String name) {
        super(name);
    }
}

class Generic<T extends Father>{
    private T key;

    public Generic(T key) {
        this.key = key;
    }

    public T getKey(){
        return key;
    }
}