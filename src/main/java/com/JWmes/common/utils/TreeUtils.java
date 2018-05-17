package com.JWmes.common.utils;


import com.JWmes.project.system.menu.entity.MenuEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 权限数据处理
 * 
 * @author zhong
 */
public class TreeUtils
{

    /**
     * 根据父节点的ID获取所有子节点
     * 
     * @param list 分类表
     * @return String
     */
    public static List<MenuEntity> getChildPerms(List<MenuEntity> list, int parentId)
    {
        List<MenuEntity> returnList = new ArrayList<MenuEntity>();
        for (Iterator<MenuEntity> iterator = list.iterator(); iterator.hasNext();)
        {
            MenuEntity t = (MenuEntity) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * 
     * @param list
     * @param
     */
    private static void recursionFn(List<MenuEntity> list, MenuEntity t)
    {
        // 得到子节点列表
        List<MenuEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MenuEntity tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<MenuEntity> it = childList.iterator();
                while (it.hasNext())
                {
                    MenuEntity n = (MenuEntity) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<MenuEntity> getChildList(List<MenuEntity> list, MenuEntity t)
    {

        List<MenuEntity> tlist = new ArrayList<MenuEntity>();
        Iterator<MenuEntity> it = list.iterator();
        while (it.hasNext())
        {
            MenuEntity n = (MenuEntity) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    List<MenuEntity> returnList = new ArrayList<MenuEntity>();

    /**
     * 根据父节点的ID获取所有子节点
     * 
     * @param list 分类表
     * @param typeId 传入的父节点ID
     * @param prefix 子节点前缀
     */
    public List<MenuEntity> getChildPerms(List<MenuEntity> list, int typeId, String prefix)
    {
        if (list == null)
        {
            return null;
        }
        for (Iterator<MenuEntity> iterator = list.iterator(); iterator.hasNext();)
        {
            MenuEntity node = (MenuEntity) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.getParentId() == typeId)
            {
                recursionFn(list, node, prefix);
            }
            // 二、遍历所有的父节点下的所有子节点
            /*
             * if (node.getParentId()==0) { recursionFn(list, node); }
             */
        }
        return returnList;
    }

    private void recursionFn(List<MenuEntity> list, MenuEntity node, String p)
    {
        // 得到子节点列表
        List<MenuEntity> childList = getChildList(list, node);
        if (hasChild(list, node))
        {
            // 判断是否有子节点
            returnList.add(node);
            Iterator<MenuEntity> it = childList.iterator();
            while (it.hasNext())
            {
                MenuEntity n = (MenuEntity) it.next();
                n.setMenuName(p + n.getMenuName());
                recursionFn(list, n, p + p);
            }
        }
        else
        {
            returnList.add(node);
        }
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<MenuEntity> list, MenuEntity t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
