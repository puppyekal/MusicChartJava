import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DriveMakeComment {
    public static void main(String[] args) throws IOException {
        new MakeComment();

        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.white);

        CommentUI primary = new CommentUI();
        frame.getContentPane().add(primary);


        frame.pack();
        frame.setVisible(true);
    }
}
