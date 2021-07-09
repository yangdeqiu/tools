package com.ydq.tools.swt.tooltip;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ToolTipFrame {

    private JFrame jFrame = null;  //  @jve:decl-index=0:visual-constraint="347,57"

    private JPanel jContentPane = null;

    private JButton jButton = null;

    private boolean isDraging = false;

    private int xx_Width, yy_Height;


    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(8, 57, 382, 72));
            jButton.setText("CoolBabY,QQ:291904818,只有交流才能进步！");
            jButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseExited(java.awt.event.MouseEvent e) {
                    OperateToolTip.close();
                }

                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if ((jFrame.getLocation().x + jButton.getLocation().x) > (301 + jButton.getLocation().x)) {
                        new ToolTip(jFrame.getLocation().x - 301, jFrame.getLocation().y + jButton.getLocation().y).setToolTip(new ImageIcon("test.gif"), "“CoolBabY，QQ：291904818，只有交流才能进步。博客：http://blog.sina.com.cn/coolbabybing");
                    } else {
                        new ToolTip(jFrame.getLocation().x + (int) jFrame.getWidth(), jFrame.getLocation().y + jButton.getLocation().y).setToolTip(new ImageIcon("test.gif"), "CoolBabY，QQ：291904818，只有交流才能进步。博客：http://blog.sina.com.cn/coolbabybing");
                    }

                }
            });
        }
        return jButton;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ToolTipFrame application = new ToolTipFrame();
            application.getJFrame().setVisible(true);
        });
    }


    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setSize(400, 200);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("JToolTipDemo");
            jFrame.setUndecorated(true);
            moveFrame();
        }
        return jFrame;
    }

    public void moveFrame() {
        jFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                isDraging = true;
                xx_Width = e.getX();
                yy_Height = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                isDraging = false;
            }
        });
        jFrame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isDraging) {
                    int left = jFrame.getLocation().x;
                    int top = jFrame.getLocation().y;
                    jFrame.setLocation(left + e.getX() - xx_Width, top + e.getY()
                            - yy_Height);

                    jFrame.repaint();
                }
            }
        });
    }


    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButton(), null);
        }
        return jContentPane;
    }

}
