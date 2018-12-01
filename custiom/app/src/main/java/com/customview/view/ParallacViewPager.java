package com.customview.view;

/**
 * des:
 * author: Yang Weisi
 * date 2018/12/1 15:28
 *
 * 1.效果分析
 *   知乎，酷狗，一般用作引导页(视差)，其实就是根据当前滚动的位置去设置位移
 *   布局确定：最外层(ViewPager)+Fragment
 *   思路确定：
 *
 *   2.代码与框架的区别
 *     2.1 思路确定
 *         2.1.1 代码：ViewPager+Fragment 怎么去改变位置，findViewById找到所有View,
 *               可以监听ViewPager的滚动，然后循环所有View去改变位置
 *         2.1.2 框架：布局自定义属性，自己去解析，自己去实现监听滚动，去改变View位置
 *         可扩展，可阅读性...
 *     2.2 实现确定：
 *         2.2.1 先把布局和Fragment创建好
 *
 * 3.View拦截解析属性
 *
 */
public class ParallacViewPager {
}
