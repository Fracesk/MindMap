package com.example.mindmap;

import javafx.scene.shape.Line;

import java.io.Serial;
import java.io.Serializable;

//能被序列化的直线，不用改写方法
public class LineSerial extends Line implements Serializable {
    public LineSerial() {
        super();
    }
}
