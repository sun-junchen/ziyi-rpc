package com.ziyi.ziyirpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.ziyi.ziyirpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SPI加载器 (支持键值对映射)
 */
@Slf4j
public class SpiLoader {
    /**
     * 存储已加载的类: 接口名 => (key => 实现类)
     */
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存(避免重复 new) , 类路径 => 对象实例,单例模式
     */

    public static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";
    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};
    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型
     */
    public static void loadAll() {
        log.info("加载所有SPI");
//        for (Class<?> aClass : LOAD_CLASS_LIST) {
//            load(aClass);
//        }
        LOAD_CLASS_LIST.forEach(SpiLoader::load);
    }

    /**
     * 获取某个接口的实例
     *
     * @param tClass
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader的 %s 不存在 key=%s的类型", tClassName, key));
        }
//        获取到要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
//        从实例缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s类实例化失败", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }


    /**
     * 加载某个类型
     *
     * @param loadClass
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> loadClass) {
        log.info("加载类型为 {} 的 SPI", loadClass.getName());
//      扫描路径, 用户自定义的SPI 优先级高于系统 SPI
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        Arrays.stream(SCAN_DIRS).forEach(
                scanDir -> {
                    List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
//                    读取每个资源文件
                    resources.forEach(resource -> {
                        try {
                            InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                String[] strArray = line.split("=");
                                if (strArray.length > 1) {
                                    String key = strArray[0];
                                    String className = strArray[1];
                                    keyClassMap.put(key, Class.forName(className));
                                }
                            }

                        } catch (IOException | ClassNotFoundException e) {
                            log.info("SPI resource load error", e);
                        }
                    });
                }
        );
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }


}
