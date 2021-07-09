package com.ydq.tools.swt.tooltip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;

public class ToolTip {

    boolean isCanTop = true; // 是否要求至顶（jre1.5以上版本方可执行）；

    private int xx_Width, yy_Height;//JToolTip提示框的横坐标和纵坐标；


    public ToolTip(int xx, int yy) {
        this.xx_Width = xx;
        this.yy_Height = yy;
        isCanTop = true;


        try { // 通过调用方法，强制获知是否支持自动窗体置顶；
            JWindow.class.getMethod("setAlwaysOnTop",
                    new Class[]{Boolean.class});
        } catch (Exception e) {
            isCanTop = false;
        }

    }


    class ToolTipModel extends JWindow {
        private static final long serialVersionUID = 1L;

        private JLabel showImage_Label = null;//图片载体；

        private JTextArea showMessage_Texa = null;//文字载体；

        private JPanel inner_Panel = null;//内部JPanel；

        private JPanel external_Panel = null;//外部JPanel；

        public ToolTipModel() {
            initComponents();
        }

        private void initComponents() {
            this.setSize(300, 300);//JToolTip的大小设置（可绝对设置，也可传入参数设置）；
            this.getContentPane().add(getExternal_Panel());
        }

        private JPanel getExternal_Panel() {//返回外部JPanel；
            if (external_Panel == null) {
                external_Panel = new JPanel(new BorderLayout(1, 1));
                external_Panel.setBackground(new Color(255, 255, 225));
                EtchedBorder etchedBorder = (EtchedBorder) BorderFactory
                        .createEtchedBorder();
                external_Panel.setBorder(etchedBorder); // 设定外部面板内容边框为风化效果
                external_Panel.add(getInner_Panel());// 加载内部面板
            }
            return external_Panel;
        }

        private JPanel getInner_Panel() {//返回内部JPanel；
            if (inner_Panel == null) {
                inner_Panel = new JPanel();
                inner_Panel.setLayout(null);
                inner_Panel.setBackground(new Color(255, 255, 225));
                inner_Panel.add(get_IconLabel(), null);
                inner_Panel.add(get_Message(), null);
            }
            return inner_Panel;
        }

        private JTextArea get_Message() {
            if (showMessage_Texa == null) {
                showMessage_Texa = new JTextArea();
                showMessage_Texa.setBackground(new Color(255, 255, 225));
                showMessage_Texa.setMargin(new Insets(4, 4, 4, 4));//设置组件的边框和它的文本之间的空白。
                showMessage_Texa.setLineWrap(true);

                showMessage_Texa.setWrapStyleWord(true);
                showMessage_Texa.setForeground(Color.BLACK);
                showMessage_Texa.setBounds(150, 70, 100, 150);
            }
            return showMessage_Texa;
        }

        private JLabel get_IconLabel() {
            if (showImage_Label == null) {
                showImage_Label = new JLabel();
                showImage_Label.setBounds(10, 10, 140, 225);
            }
            return showImage_Label;
        }

        public void animate() {
            new OperateToolTip(this, isCanTop, xx_Width, yy_Height);
            OperateToolTip.begin();
        }

    }


    public void setToolTip(Icon icon, String msg) {
        ToolTipModel single = new ToolTipModel();
        if (icon != null) {
            single.get_IconLabel().setIcon(icon);
        }
        single.showMessage_Texa.setText(msg);
        single.animate();
    }
}
