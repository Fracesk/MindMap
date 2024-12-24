package com.example.mindmap;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;

public class FileManger implements Serializable{
    public void saveFile(TreeNode t,File file){
        WriteObject(t, file);
    }
    public TreeNode openFile(File file){
        return readObject(file);
    }
    private void WriteObject(TreeNode t, File file){
        FileOutputStream out;
        try{
            out =new FileOutputStream(file);
            ObjectOutputStream objout=new ObjectOutputStream(out);
            objout.writeObject(TreeNode.LMaxLinkLen);
            objout.writeObject(TreeNode.RMaxLinkLen);
            objout.writeObject(TreeNode.LBlockLen);
            objout.writeObject(TreeNode.RBlockLen);
            objout.writeObject(TreeNode.getLchildren());
            objout.writeObject(TreeNode.getRchildren());
            objout.writeObject(t);
            objout.flush();
            objout.close();
            System.out.println("write success");
        } catch (IOException e) {
            System.out.println("write failed");
            e.printStackTrace();
        }
    }
    private TreeNode readObject(File file){
        TreeNode tmp=null;
        FileInputStream in;
        try {
            in=new FileInputStream(file);
            ObjectInputStream objin=new ObjectInputStream(in);
            TreeNode.LMaxLinkLen= (double) objin.readObject();
            TreeNode.RMaxLinkLen= (double) objin.readObject();
            TreeNode.LBlockLen= (double) objin.readObject();
            TreeNode.RBlockLen= (double) objin.readObject();
            TreeNode.setLchildren((ArrayList<TreeNode>) objin.readObject());
            TreeNode.setRchildren((ArrayList<TreeNode>) objin.readObject());
            tmp = (TreeNode)objin.readObject();
            objin.close();
            //System.out.println("read success");
        } catch (IOException e){
            //System.out.println("read failed");
            e.printStackTrace();
            return  null;
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            return  null;
        }
        return tmp;
    }

    public void export(AnchorPane A, File file){
        WritableImage image=A.snapshot(new SnapshotParameters(),null); // 为节点截图，null即sys会帮我们自动创建一个接受图片的WriableImage对象
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",file);
            System.out.println("保存成功");
        }catch (IOException ex){
            System.out.println("保存失败"+ex.getMessage());
        }
    }

}
