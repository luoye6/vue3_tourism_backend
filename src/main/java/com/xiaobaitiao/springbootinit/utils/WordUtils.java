package com.xiaobaitiao.springbootinit.utils;

import cn.hutool.dfa.WordTree;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 内容工具类
 */
public class WordUtils {
    private static final WordTree WORD_TREE;

    static {
        WORD_TREE = new WordTree();
        try {
            // 使用 ClassLoader 加载资源文件,否则会导致项目部署上线后，打成 Jar 包无法获取，导致使用工具类失败
            ClassPathResource resource = new ClassPathResource("forbiddenWords.txt");
            InputStream inputStream = resource.getInputStream();
            List<String> blackList = loadBlackListFromStream(inputStream);
            WORD_TREE.addWords(blackList);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "读取违禁词文件出错");
        }
    }

    /**
     * 从输入流中加载违禁词列表
     *
     * @param inputStream 输入流
     * @return 违禁词列表
     */
    private static List<String> loadBlackListFromStream(InputStream inputStream) {
        List<String> blackList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                blackList.add(line.trim()); // 去掉首尾空格
            }
        } catch (Exception e) {
            System.err.println("读取违禁词文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
        return blackList;
    }

    /**
     * 检测文本中是否包含违禁词
     *
     * @param content 输入文本
     * @return 是否包含违禁词
     */
    public static boolean containsForbiddenWords(String content) {
        return !WORD_TREE.matchAll(content).isEmpty();
    }

    /**
     * 提取文本中的违禁词列表
     *
     * @param content 输入文本
     * @return 检测到的违禁词列表
     */
    public static List<String> extractForbiddenWords(String content) {
        return WORD_TREE.matchAll(content);
    }
}