package com.example.operlog.util;

/**
 * @program: learn
 * @description:
 * @author: Mr.liu
 * @create: 2020-11-14 00:23
 **/
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * ip查询处理
 *
 * @author videomonster
 */
@Slf4j
public final class IpUtils {

    private static final String UNKNOWN_ADDRESS = "未知位置";

    /**
     * 根据IP获取地址
     *
     * @return 国家|区域|省份|城市|ISP
     */
    public static String getAddress(String ip) {
        return getAddress(ip, DbSearcher.BTREE_ALGORITHM);
    }

    /**
     * 根据IP获取地址
     *
     * @param ip
     * @param algorithm 查询算法
     * @return 国家|区域|省份|城市|ISP
     * @see DbSearcher
     * DbSearcher.BTREE_ALGORITHM; //B-tree
     * DbSearcher.BINARY_ALGORITHM //Binary
     * DbSearcher.MEMORY_ALGORITYM //Memory
     */
    public static String getAddress(String ip, int algorithm) {
        DbSearcher searcher=null;
        try {
            String name = "ip2region.db";
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
            //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
            //再把文件流写入暂存文件，方便读取
            ClassPathResource resource = new ClassPathResource("ip2region/ip2region.db");
            if(!file.exists()){
                FileUtils.copyInputStreamToFile(resource.getInputStream(), file);
            }
            searcher = new DbSearcher(new DbConfig(), file.getPath());
            DataBlock dataBlock;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    dataBlock = searcher.btreeSearch(ip);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    dataBlock = searcher.binarySearch(ip);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    dataBlock = searcher.memorySearch(ip);
                    break;
                default:
                    log.error("未传入正确的查询算法");
                    return UNKNOWN_ADDRESS;
            }
            String address = dataBlock.getRegion().replace("0|","");
            char symbol = '|';
            if(address.charAt(address.length()-1) == symbol){
                address = address.substring(0,address.length() - 1);
            }
            return address.equals("内网IP|内网IP")?"内网IP":address;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(searcher!=null){
                try {
                    searcher.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(IpUtils.getAddress("223.73.198.16"));
        System.out.println(IpUtils.getAddress("128.190.125.42"));
        System.out.println(IpUtils.getAddress("206.77.131.86"));
        System.out.println(IpUtils.getAddress("116.37.161.86"));
        System.out.println(IpUtils.getAddress("136.27.231.86"));
        System.out.println(IpUtils.getAddress("127.0.0.1"));
        System.out.println(IpUtils.getAddress("112.17.236.511"));
    }
}

