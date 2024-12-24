package com.example.mindmap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewControllor implements Initializable {
    private static TreeNode root;

    @FXML
    public StackPane rightshow;
    @FXML
    public Button newNode;
    @FXML
    public AnchorPane paintingPane; // the pane show the mindmap
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public TreeView treeView;
    @FXML
    public Button addSon;
    @FXML
    public Button addBro;
    @FXML
    public Button deleteBut;
    @FXML
    public Button leftLayout;
    @FXML
    public Button rightLayout;
    @FXML
    public Button autoLayout;
    @FXML
    public MenuItem newFile;
    @FXML
    public MenuItem openFile;
    @FXML
    public MenuItem export;
    @FXML
    public MenuItem save;
    // when start to run this program, initialize will be run at the begining
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // show the centreNode
        root = new TreeNode("Title");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        root.setLayoutX(320); // set the location of the root in paintingPane
        root.setLayoutY(250);
        paintingPane.getChildren().add(root);
        root.setRoot(true);
        Draw.CurNode = root;
        root.initNode(root);
        // Need a TreeItem
        treeView.setRoot(root.getItem()); // set the TreeViewRoot as the root of the structure picture
        treeView.requestFocus();

        newFile.setOnAction(actionEvent -> newFile());
        openFile.setOnAction(actionEvent -> openFile());
        export.setOnAction(actionEvent -> export());
        save.setOnAction(actionEvent -> save());
        addSon.setOnAction(actionEvent -> addSon());
        addBro.setOnAction(actionEvent -> addBro());
        deleteBut.setOnAction(actionEvent -> deleteNode());
        leftLayout.setOnAction(actionEvent -> leftLayout());
        rightLayout.setOnAction(actionEvent -> rightLayout());
        autoLayout.setOnAction(actionEvent -> autoLayout());

    }

    @FXML
    private void addSon() {
        if (Draw.CurNode == null) {
            System.out.println("要选中一个节点");
            return;
        }
        TreeNode tmp = new TreeNode("subtitle");
        tmp.initNode(root);
        //if root is selected, then set the type of its son
        if(Draw.CurNode.isRoot()){
            if(TreeNode.getLchildren().size()<TreeNode.getRchildren().size()) { // if the first layout rc > lc, set the son of root toward left
                //SonOfRoot.setType(-1);
                tmp.setType(-1);
                TreeNode.getLchildren().add(tmp); // maybe it can change to the tree
            }else {
                //SonOfRoot.setType(1);
                tmp.setType(1);
                TreeNode.getRchildren().add(tmp);
            }
            tmp.setFather(Draw.CurNode);
            // store the tree_structure
        }
        //if CurNode is other root, then set its son's type follow himself
        else {
            Draw.CurNode.getChildrens().add(tmp);
            tmp.setType(Draw.CurNode.getType());
            tmp.setFather(Draw.CurNode);
        }
        //show in the Pane
        paintingPane.getChildren().add(tmp); // add the node
        paintingPane.getChildren().add(tmp.getLine());// add the line
        //update the treeView
        Draw.CurNode.getItem().getChildren().add(tmp.getItem());
        //Draw.setSon(Draw.CurNode, 1, tmp);// update the location
        Draw.update(root);
    }
    @FXML
    public void addBro() {
        if(Draw.CurNode == null) {
            System.out.println("请选择一个节点");
        }
        if(Draw.CurNode.isRoot()) {
            System.out.println("根无法添加兄弟节点");
        }
        TreeNode tmp = new TreeNode("subTitle");
        tmp.initNode(root);
        // get curNode's parent node, and the way to add the broNode is equal the way add the sonNode
        if (Draw.CurNode.getFather().isRoot()) { // it the parent node is root
            if(Draw.CurNode.getType() == -1) {
                TreeNode.getLchildren().add(tmp);
                tmp.setType(-1);
            } else {
                TreeNode.getRchildren().add(tmp);
                tmp.setType(1);
            }
            tmp.setFather(Draw.CurNode.getFather());
        } else {
            Draw.CurNode.getFather().getChildrens().add(tmp);
            tmp.setFather(Draw.CurNode.getFather());
            tmp.setType(Draw.CurNode.getType());
        }
        paintingPane.getChildren().add(tmp);
        paintingPane.getChildren().add(tmp.getLine());
        Draw.CurNode.getFather().getItem().getChildren().add(tmp.getItem());
        Draw.update(root);
    }
    @FXML
    public void deleteNode() {
        if(Draw.CurNode == null) {
            System.out.println("请选择一个节点");
        }
        if(Draw.CurNode.isRoot()) {
            System.out.println("根节点无法被删除");
        }
        Draw.delNode(Draw.CurNode, paintingPane); // delet the painting, next is delete the treeNode
        if(Draw.CurNode.getFather().isRoot()) {
            if(TreeNode.getLchildren().contains(Draw.CurNode)) {
                TreeNode.getLchildren().remove(Draw.CurNode);
                root.getItem().getChildren().remove(Draw.CurNode.getItem());
            } else {
                TreeNode.getRchildren().remove(Draw.CurNode);
                root.getItem().getChildren().remove(Draw.CurNode.getItem());
            }
        } else {
            Draw.CurNode.getFather().getChildrens().remove(Draw.CurNode);
            Draw.CurNode.getFather().getItem().getChildren().remove(Draw.CurNode.getItem());
        }
        Draw.update(root);
        Draw.CurNode = null;
    }
    @FXML
    public void leftLayout() {
        for(TreeNode tmp : TreeNode.getRchildren()) {
            TreeNode.getLchildren().add(tmp);
        }
        TreeNode.getRchildren().clear();
        Draw.update(root);
    }
    @FXML
    public void rightLayout() {
        for(TreeNode tmp : TreeNode.getLchildren()) {
            TreeNode.getRchildren().add(tmp);
        }
        TreeNode.getLchildren().clear();
        Draw.update(root);
    }

    //the new AutoLayout
    public void autoLayout() {
        TreeNode.getLchildren().sort(new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode o1, TreeNode o2) {
                return (int) (o2.getBlockLen()-o1.getBlockLen());
            }
        });
        while(TreeNode.LBlockLen>TreeNode.RBlockLen && TreeNode.getLchildren().size()>1){
            TreeNode.getRchildren().add(TreeNode.getLchildren().get(TreeNode.getLchildren().size()-1));
            TreeNode.getLchildren().remove(TreeNode.getLchildren().size()-1);
            Draw.update_updown_link(root);
        }

        TreeNode.getRchildren().sort(new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode o1, TreeNode o2) {
                return (int) (o2.getBlockLen()-o1.getBlockLen());
            }
        });
        while(TreeNode.LBlockLen<TreeNode.RBlockLen && TreeNode.getRchildren().size()>1){
            TreeNode.getLchildren().add(TreeNode.getRchildren().get(TreeNode.getRchildren().size()-1));
            TreeNode.getRchildren().remove(TreeNode.getRchildren().size()-1);
            Draw.update_updown_link(root);
        }
        Draw.update(root);

        /*
        if(TreeNode.getRchildren().isEmpty()) {
            for(int i=TreeNode.getLchildren().size()/2;i<TreeNode.getLchildren().size();i++){
                TreeNode.getRchildren().add(TreeNode.getLchildren().get(i));
                TreeNode.getLchildren().remove(i);
            }
            Draw.update(root);
        } else if (TreeNode.getLchildren().isEmpty()) {
            for(int i=TreeNode.getRchildren().size()/2;i<TreeNode.getRchildren().size();i++){
                TreeNode.getLchildren().add(TreeNode.getRchildren().get(i));
                TreeNode.getRchildren().remove(i);
            }
            Draw.update(root);
        }else{
            return;
        }
        */
        //Draw.getDp();
        //Draw.update(root);
    }

    public void newFile() {
        TreeNode.getLchildren().clear();
        TreeNode.getRchildren().clear();
        paintingPane.getChildren().clear();
        root = new TreeNode("Title");
        root.initNode(root);
        treeView.setRoot(root.getItem());
        root.setLayoutX(320); // set the location of the root
        root.setLayoutY(250);
        paintingPane.getChildren().add(root);
        root.setRoot(true);
        Draw.CurNode = root;
    }

    public void openFile() {
        Stage tmpStage = new Stage(); // create a stage to show the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("mindmap file", "*.mp"));
        FileManger fm = new FileManger();
        TreeNode tmp = null;
        tmp = fm.openFile(fileChooser.showOpenDialog(tmpStage));
        if(tmp != null) {
            paintingPane.getChildren().clear();
            root = tmp;
            root.initNode(root);
            root.setLayoutX(320); // set the location of the root
            root.setLayoutY(250);
            paintingPane.getChildren().add(root);
            treeView.setRoot(root.getItem());
            for(TreeNode tmp1 : TreeNode.getRchildren()) {
                Draw.reload(root, tmp1, paintingPane);
            }
            for(TreeNode tmp1 : TreeNode.getLchildren()) {
                Draw.reload(root, tmp1, paintingPane);
            }
            Draw.update(root);
            System.out.println("文件打开成功");
            Draw.CurNode = root;
        }else{
            System.out.println("文件打开失败");
        }
    }

    public void save() {
        FileManger fm = new FileManger();
        Stage tmpStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存");
        fileChooser.setInitialFileName("test");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "mindmap file", "*.mp")); //ObservableList<FileChooser.ExtensionFilter>,
        File file = fileChooser.showSaveDialog(tmpStage);
        if(file==null) return;
        try {
            fm.saveFile(root, file);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export( ) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出");
        fileChooser.setInitialFileName("test");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null) {
            FileManger fm = new FileManger();
            fm.export(paintingPane, file);
            System.out.println("导出成功");
        }
    }

}
