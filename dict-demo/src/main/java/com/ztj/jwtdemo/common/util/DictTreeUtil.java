package com.ztj.jwtdemo.common.util;

import com.ztj.jwtdemo.vo.DictTree;

import java.util.ArrayList;
import java.util.List;

public class DictTreeUtil {

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static <T extends DictTree> List<T> bulid(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getPcode())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends DictTree> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getCode().equals(it.getPcode())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<DictTree>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}
