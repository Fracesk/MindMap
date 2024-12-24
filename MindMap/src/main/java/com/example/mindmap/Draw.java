package com.example.mindmap;

import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

import static java.lang.Double.max;
import static java.lang.Double.min;
import static java.lang.Math.abs;

// locate the location of the TreeNode and the Line
public abstract class Draw {
    // TreeNode's Height Width and the distance of everyNode
    public static final double RecH = 24;
    public static final double RecW = 54;
    public static final double Block_dis = 24; // 列间距

    public static TreeNode CurNode;


    public static void update_updown_link(TreeNode t) {
        double len = 0; // What
        if (t.isRoot()) { // root
            TreeNode.LMaxLinkLen = 0;
            for (TreeNode tmp : TreeNode.getLchildren()) {
                tmp.setType(-1); // 左右布局时节点方向需要被改变
                update_updown_link(tmp); // recursion
                TreeNode.LMaxLinkLen = Math.max(TreeNode.LMaxLinkLen, tmp.getMaxLinkLen() + (100 - Draw.RecW) + tmp.getTextLen());
                len += tmp.getBlockLen();
            }
            len += (TreeNode.getLchildren().size() - 1) * Block_dis;
            TreeNode.LBlockLen = max(len, RecH);
            // the first layout of root, right
            len = 0;
            TreeNode.RMaxLinkLen = 0;
            for(TreeNode tmp : TreeNode.getRchildren()) {
                tmp.setType(1);
                update_updown_link(tmp);
                TreeNode.RMaxLinkLen = Math.max(TreeNode.RMaxLinkLen, tmp.getMaxLinkLen() + (100 - Draw.RecW) + tmp.getTextLen());
                len += tmp.getBlockLen(); // maybe it's wrong
            }
            len += (TreeNode.getRchildren().size() - 1) * Block_dis;
            TreeNode.RBlockLen = (max(len, RecH));
        }else { // no the root's first layout
            t.setMaxLinkLen(0);
            for (TreeNode tmp : t.getChildrens()) {
                tmp.setType(t.getType());
                update_updown_link(tmp);
                t.setMaxLinkLen(max(t.getMaxLinkLen(),
                        tmp.getMaxLinkLen() + (100 - Draw.RecW) + tmp.getTextLen())); // 取得与最长文字子节点的距离
                len += tmp.getBlockLen();
            }
            len += (t.getChildrens().size() - 1) * Block_dis;
            t.setBlockLen(max(len, RecH));
        }
    }


    public static void update_pos(TreeNode t, int op) {
        if(!t.isRoot() && t.getChildrens().isEmpty()) return; // no the root and it don't have the children
        if(op == 1) t.setBlockLen(TreeNode.RBlockLen);
        if(op == -1) t.setBlockLen(TreeNode.LBlockLen);
        double last = t.getLayoutY() + Draw.RecH / 2 - t.getBlockLen() / 2; // 每次进来都把last，即y坐标设为此节点块的最顶端
        if(op != 0) {
            if(op == -1) {
                for(TreeNode tmp : TreeNode.getLchildren()) {
                    last = setSon(t, last, tmp);
                }
                for(TreeNode tmp : TreeNode.getLchildren()) {
                    update_pos(tmp, 0);
                }
            } else {
                for(TreeNode tmp : TreeNode.getRchildren()) {
                    last = setSon(t, last, tmp);
                }
                for(TreeNode tmp : TreeNode.getRchildren()) {
                    update_pos(tmp, 0);
                }
            }
        } else { // op = 0
            for(TreeNode tmp : t.getChildrens()) {
                last = setSon(t, last, tmp); // 先固定好位置
            }
            for(TreeNode tmp : t.getChildrens()) {
                update_pos(tmp, 0); // 再进行广度遍历
            }
        }
    }

    private static double setSon(TreeNode t, double last, TreeNode tmp) {
        //last为当前节点所处块的最高处
        double pos = last + tmp.getBlockLen() / 2 - Draw.RecH / 2;
        tmp.setLayoutY(pos);
        if(tmp.getType() == 1) { // when type is at right tree
            tmp.setLayoutX(t.getLayoutX() + t.getTextLen() + ( 100 - Draw.RecW ) );
            tmp.getLine().setStartX(t.getLayoutX() + t.getTextLen());
            tmp.getLine().setStartY(t.getLayoutY() + Draw.RecH / 2);
            tmp.getLine().setEndX(tmp.getLayoutX());
            tmp.getLine().setEndY(tmp.getLayoutY() + Draw.RecH / 2);
        }else { // when type is at left tree
            tmp.setLayoutX(t.getLayoutX() + (100 - Draw.RecW) * tmp.getType() - tmp.getTextLen());
            tmp.getLine().setStartX(t.getLayoutX());
            tmp.getLine().setStartY(t.getLayoutY() + Draw.RecH / 2);
            tmp.getLine().setEndX(tmp.getLayoutX() + tmp.getTextLen());
            tmp.getLine().setEndY(tmp.getLayoutY() + Draw.RecH / 2);
        }
        // 该节点的下一个相邻兄弟节点的所处块最顶端
        last = pos + tmp.getBlockLen() / 2 + Block_dis + Draw.RecH / 2;
        return last;
    }

    public static void update(TreeNode root) {
        update_updown_link(root);
        Locate_Root(root);
        update_pos(root, 1);
        update_pos(root, -1);
    }

    public static void Locate_Root ( TreeNode root ){
        double x = root.getLayoutX();
        double y = root.getLayoutY();
        root.setLayoutX( Math.max( x, TreeNode.getLMaxLinkLen() ) );
        root.setLayoutY( Math.max( y,
                Math.max(TreeNode.LBlockLen/2 - Draw.RecH/2,
                        TreeNode.RBlockLen/2 - Draw.RecH/2)  ) );
    }

    public static void reload(TreeNode root, TreeNode t, AnchorPane pane) {
        t.initNode(root);
        t.getFather().getItem().getChildren().add(t.getItem());
        pane.getChildren().add(t);
        pane.getChildren().add(t.getLine());
        for(TreeNode tmp : t.getChildrens()) {
            reload(root, tmp, pane);
        }
    }
    public static void delNode(TreeNode t, AnchorPane A) {
        for(TreeNode tmp : t.getChildrens()) {
            delNode(tmp, A);
        }
        A.getChildren().remove(t);
        A.getChildren().remove(t.getLine());
    }



}
