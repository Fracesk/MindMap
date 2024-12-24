package com.example.mindmap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.shape.Line;

import java.io.Serializable;
import java.util.ArrayList;
// store the tree, A tree structure

public class TreeNode extends TextField implements Serializable {
    /*
    the leftSonNodes and RightSonNodes of the root
    you can look through all the sonNodes by this.getChildren()
    */
    public static double LMaxLinkLen;
    public static double RMaxLinkLen;
    public static double LBlockLen = Draw.RecH;
    public static double RBlockLen = Draw.RecH;
    private static ArrayList<TreeNode> Lchildren = new ArrayList<>(); // store the left son of the root, only on the first layout
    private static ArrayList<TreeNode> Rchildren = new ArrayList<>(); // store the right son
    //I am the Dividing line
    /*
    as a Node, it should have its private properties
     */
    private ArrayList<TreeNode> children; // store the son
    private double blockLen; // the length of the block
    private double maxLinkLen; // the max len of sonChain
    private TreeNode father;
    private boolean isRoot;
    private LineSerial line; // every node has a line, but the line of root unUsing
    private double textLen; // the width of text
    private int type; // The Node to left or Right , type = -1 to left, type = 1 to right
    private TreeItemSerial item;
    private int sonSize;
    private String txt; // store the txt, because when you save, it will be disappear
    public TreeNode(String s) {
        super(s);
        setTxt(s);
        this.setPrefHeight(Draw.RecH);
        this.setPrefWidth(Math.max(s.length()*15, 50));
        this.setRoot(false);
        this.textLen = Math.max( Draw.RecW , this.getPrefWidth() );
        blockLen = Draw.RecH;
        type = 1;
        line = new LineSerial();
        children = new ArrayList<>();
    }
    /* INIT */
    public void initNode(TreeNode root) {
        // setting the style
        this.setAlignment(Pos.CENTER);
        // setting init height and width, opening a file need it
        this.setPrefHeight(Draw.RecH);
        this.setPrefWidth(this.textLen);
        this.setText(this.txt);

        this.setStyle("-fx-control-inner-background:#FFB3E6");
        this.setEditable(false);
        // When the mouse clicked(Add listener to listen mouse clicked)
        this.setOnMouseClicked(mouseEvent -> {
            //if CurNode has been a Node, we should unedittable it first
            if(Draw.CurNode != null) {
                Draw.CurNode.setEditable(false);
                //unselected style
                Draw.CurNode.setStyle("-fx-control-inner-background:#FFB3E6");
            }
            Draw.CurNode = this;
            //set the look of Node extends textfield like be selected
            Draw.CurNode.setStyle("-fx-control-inner-background:#FFB3E6;"+"-fx-border-color: #FF1493;"+"-fx-border-radius: 2px;");
            //if
            if(mouseEvent.getClickCount() == 2) {
                this.setEditable(true);
            }
        });

        // when the text change
        this.textProperty().addListener((observableValue, s, t1) -> {
            txt = TreeNode.this.getText();
            item.setValue(TreeNode.this.getText()); // why use a TreeNode
            TreeNode.this.setPrefWidth(Math.max(TreeNode.this.getText().length()*15, 50)); // get the width of the text and update the NodeWidth
            textLen = TreeNode.this.getPrefWidth(); // get the node's width
            Draw.update(root);
        });



        // When the mouse entered
        this.setOnMouseEntered(mouseEvent -> {
            this.setCursor(Cursor.DEFAULT); // the cursor's style
            this.setStyle("-fx-control-inner-background:#FFB3E6;"+"-fx-border-color:#00BFFF;"+"-fx-border-radius: 2px;");
        });
        // When the mouse exit
        this.setOnMouseExited(mouseEvent -> {
            if(Draw.CurNode!=this) {
                this.setStyle("-fx-control-inner-background:#FFB3E6;");
            }else {
                this.setStyle("-fx-control-inner-background:#FFB3E6;"+"-fx-border-color:#FF1493;"+"-fx-border-radius: 2px;");
            }
        });

        item = new TreeItemSerial(getTxt());
        item.setExpanded(true);
    }




    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public LineSerial getLine() {
        return line;
    }
    public double getTextLen() {
        return textLen;
    }
    public void setTextLen(double textLen) {
        textLen = textLen;
    }

    public static ArrayList<TreeNode> getLchildren() {
        return Lchildren;
    }
    public static void setLchildren(ArrayList<TreeNode> lchildren) {
        Lchildren = lchildren;
    }

    public static ArrayList<TreeNode> getRchildren() {
        return Rchildren;
    }
    public static void setRchildren(ArrayList<TreeNode> rchildren) {
        Rchildren = rchildren;
    }

    public ArrayList<TreeNode> getChildrens() { // no override
        return children;
    }
    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public boolean isRoot() {
        return isRoot;
    }
    public void setRoot(boolean root) {
        isRoot = root;
    }

    public void setItem(TreeItemSerial item) {
        this.item = item;
    }
    public TreeItemSerial getItem() {
        return item;
    }


    public TreeNode getFather() {
        return father;
    }
    public void setFather(TreeNode father) {
        this.father = father;
    }
   // adjust the locate

    public static double getLMaxLinkLen() {
        return LMaxLinkLen;
    }
    public static void setLMaxLinkLen(int LMaxLinkLen) {
        TreeNode.LMaxLinkLen = LMaxLinkLen;
    }

    public static double getRMaxLinkLen() {
        return RMaxLinkLen;
    }
    public static void setRMaxLinkLen(int RMaxLinkLen) {
        TreeNode.RMaxLinkLen = RMaxLinkLen;
    }

    public double getMaxLinkLen() {
        return maxLinkLen;
    }
    public void setMaxLinkLen(double maxLinkLen) {
        this.maxLinkLen = maxLinkLen;
    }

    public double getBlockLen() {
        return blockLen;
    }
    public void setBlockLen(double blockLen) {
        this.blockLen = blockLen;
    }

    public int getSonSize() {
        return sonSize;
    }
    public void setSonSize(int sonSize) {
        this.sonSize = sonSize;
    }

    public String getTxt() {
        return txt;
    }
    public void setTxt(String txt) {
        this.txt = txt;
    }


}
