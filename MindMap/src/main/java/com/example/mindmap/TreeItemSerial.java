package com.example.mindmap;

import javafx.scene.control.TreeItem;

import java.io.Serializable;

public class TreeItemSerial extends TreeItem<String> implements Serializable {
    public TreeItemSerial(String s) {
        super(s);
    }
}
