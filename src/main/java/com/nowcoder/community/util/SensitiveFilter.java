package com.nowcoder.community.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

/**
 * @author wxx
 * @version 1.0
 * 过滤敏感词
 */
@Slf4j
@Component
public class SensitiveFilter {
    //替换敏感词的内容
    private static final String REPLACEMENT="***";

    private TrieNode rootNode=new TrieNode();
    //@PostConstruct注解的方法将会在依赖注入完成后被自动调用。
    public SensitiveFilter(){}
    @PostConstruct
    public void init(){
        //（）中的流会在结束后自动被关闭，不再需要finally中手动关闭
        try (
                InputStream is =
                        this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                ){
            String word="";
            while ((word=br.readLine())!=null){
                rootNode.addKeyWord(word,rootNode);
            }
        } catch (IOException e) {
            log.error("加载前缀树失败"+e.getMessage());
        }
    }

    /**
     * 过滤字符串中的敏感词
     * @param text  为要过滤的字符串
     * @return   过滤后的
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                //如果该节点被标记为敏感词尾部
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    //判断是否为特殊字符
    private boolean isSymbol(Character c){
        //0x2E80 ~ 0x9FF 是东亚文字范围
        //如果不是字母或数字 也不是中文字符
        return (!CharUtils.isAsciiAlphanumeric(c)) && (c< 0x2E80 || c> 0x9FFF);
    }
    //前缀树
    private class TrieNode{
        //判断该字符是否为敏感词结尾
        private boolean isKeyWordEnd=false;
        //子节点
        private Map<Character,TrieNode> map=new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
        //将敏感词添加到树中
        public void addKeyWord(String word,TrieNode node){
            //遍历单词中每个字
            for (int i=0;i<word.length();i++){
                char c=word.charAt(i);
                TrieNode subnode=getSubNode(c);
                if(subnode==null) {
                    //如果子节点没有该字符，创建
                    TrieNode newNode = new TrieNode();
                    if(i==word.length()-1) newNode.setKeyWordEnd(true);
                    node.map.put(c, newNode);
                    //跳转到该节点
                    node=newNode;
                }else {
                    //如果子节点有该字符
                    //跳转到该节点
                    node = subnode;
                }
            }
        }
        //根据字符获取子节点
        public TrieNode getSubNode(char c){
            return map.getOrDefault(c, null);
        }
    }
}
