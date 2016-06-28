/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package depthfirstsearch;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author iliaaa
 */
public class GraphJFrame extends javax.swing.JFrame {

    /**
     * Creates new form GraphJFrame
     */
    public GraphJFrame() {
        initComponents();
    }

    private void createPanel(int size)
    {
        jPanel1.setPreferredSize(new Dimension(size,size));
        jPanel1.updateUI();
        jScrollPane1.updateUI();
        jPanel2.setPreferredSize(new Dimension(jPanel1.getWidth(),(gr.getEdges().size()+2)*20));
        jPanel2.updateUI();
        jScrollPane2.updateUI();
    }

    private void print()
    {
        if (!lock)
        {
            return ;
        }
        printGraph();
        printStack();
    }
    private void printStack()
    {
        int width=15;
        int height=jPanel2.getHeight()-20;//145;
        int step=0;
        setRectangle(jPanel2);
        Graphics2D st=(Graphics2D) jPanel2.getGraphics();
        Font font = new Font("Tahoma", Font.BOLD|Font.ITALIC, 15);
        st.setFont(font);
        for(int i=0; i<gr.getStack().size(); i++)
        {
            st.drawLine(width, height-step, width+50, height-step);//flor
            st.drawLine(width, height-step, width, height-step-20);//left
            st.drawLine(width+50, height-step, width+50, height-step-20);//right
            st.drawString(String.valueOf(gr.getStack().get(i)+1), 2*width, height-step);
            step+=20;
        }
        st.dispose();
    }
    private int printGraph()
    {
        ArrayList<ArrayList<Integer>> edges;
        edges=gr.getGraph();
        int n=edges.size();
        int r=15;
        int tab=20;
        //============изменяемая часть
        //закрасим участок рисования
        setRectangle(jPanel1);
        //рисуем вершины графа
        int R=(int)(r*n/Math.PI+2*r);
        if (!lock)
        {
            return 4*tab+2*r+2*R;
        }
        double alpha=2*Math.PI/n;
        int centr=tab+r+R;
        double[] vect=new double[2];
        vect[0]=0;
        vect[1]=R;
        for (int i=0; i<n; i++)
        {
            setCircle((int)(centr+vect[0]),(int)(centr-vect[1]),r,i);
            vect=rotationVector(vect,alpha);
        }
        //рисуем связи
        for (int i=0; i<n; i++)
        {
            int x0=(int)(centr+vect[0]);
            int y0=(int)(centr-vect[1]);
            for (int j=0; j<edges.get(i).size(); j++)
            {
                int edg=edges.get(i).get(j)-i;
                vect=rotationVector(vect,alpha*edg);
                setArrow(x0,y0,(int)(centr+vect[0]),(int)(centr-vect[1]),r);
                vect=rotationVector(vect,-alpha*edg);
            }
            vect=rotationVector(vect,alpha);
        }
        return 4*tab+2*r+2*R;
    }

    private void setArrow(int x1, int y1, int x2, int y2, int r)
    {
        x1+=r;
        y1+=r;
        x2+=r;
        y2+=r;
        double[] vect=new double[2];
        vect[0]=r;
        vect[1]=0;
        double alpha=Math.acos((x1-x2)/Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
        vect=rotationVector(vect,alpha);
        if (y1-y2<0)
        {
            vect[1]=-vect[1];
        }
        x1-=vect[0];
        y1-=vect[1];
        vect=rotationVector(vect,Math.PI);
        x2-=vect[0];
        y2-=vect[1];
        int rad=10;
        Graphics2D gfx=(Graphics2D) jPanel1.getGraphics();
        gfx.drawLine(x1, y1, x2, y2);
        vect[0]=rad;
        vect[1]=0;
        vect=rotationVector(vect,alpha);
        if (y1-y2>0)
        {
            vect[1]=-vect[1];
        }
        vect=rotationVector(vect,Math.PI/6);
        gfx.drawLine((int)(x2+vect[0]), (int)(y2-vect[1]), x2, y2);
        vect=rotationVector(vect,-Math.PI/3);
        gfx.drawLine((int)(x2+vect[0]), (int)(y2-vect[1]), x2, y2);
        gfx.dispose();
    }

    private void setRectangle(JPanel panel)
    {
        Graphics2D gfx=(Graphics2D) panel.getGraphics();
        gfx.setColor(panel.getBackground());
        gfx.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        gfx.dispose();
    }

    private double[] rotationVector(double[] vect, double alpha)
    {
        double temp=vect[0];
        vect[0]=temp*Math.cos(alpha)-vect[1]*Math.sin(alpha);
        vect[1]=temp*Math.sin(alpha)+vect[1]*Math.cos(alpha);
        return vect;
    }

    private void setCircle(int x, int y, int r, int number)
    {
        int FontSize=12;
        Graphics2D gfx=(Graphics2D) jPanel1.getGraphics();
        Graphics2D g=(Graphics2D) jPanel1.getGraphics();
        Graphics2D col=(Graphics2D) jPanel1.getGraphics();
        int color=gr.getColors()[number];
        col.setColor(setColors(color));
        col.fillOval(x, y, 2*r, 2*r);
        g.setColor(SelectColor(setColors(color)));
        Font font = new Font("Tahoma", Font.BOLD|Font.ITALIC, FontSize);
        gfx.drawOval(x, y, 2*r, 2*r);
        g.setFont(font);
        g.drawString(String.valueOf(number+1), x+r-FontSize/2, y+r+FontSize/2);
        gfx.dispose();
        g.dispose();
        col.dispose();
    }
    public Color SelectColor (Color cl){
        return ((0.3*cl.getAlpha()+0.59*cl.getGreen()+0.11*cl.getBlue())>((0.3*255+0.59*255+0.11*255)/2))
                ? new Color(0,0,0) : new Color(255,255,255);
    }

    public Color setColors(int i){
        return i==0 ? new Color(255,255,255) :
                i==1 ? new Color(187,187,187) :
                i==2 ? new Color(0,0,0) : null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
        });

        jScrollPane1.setOpaque(false);
        jScrollPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScrollPane1ComponentResized(evt);
            }
        });

        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 364, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setInheritsPopupMenu(true);
        jButton1.setLabel("sort: next_step");
        jButton1.setVisible(false);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(110, 170));
        jScrollPane2.setVisible(false);

        jPanel2.setPreferredSize(new java.awt.Dimension(110, 170));
        jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jPanel2ComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel2ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel2);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setToolTipText("");
        jScrollPane4.setAutoscrolls(true);

        jEditorPane1.setEditable(false);
        jScrollPane4.setViewportView(jEditorPane1);

        jMenu1.setText("File");

        jMenuItem1.setText("Открыть граф");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Сохранить граф");
        jMenuItem2.setEnabled(false);
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator1);

        jMenuItem3.setText("Выход");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
        // TODO add your handling code here:
        print();

    }//GEN-LAST:event_jPanel1ComponentResized

    private void jPanel1ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentMoved
        // TODO add your handling code here:
        print();

    }//GEN-LAST:event_jPanel1ComponentMoved

    private void jScrollPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane1ComponentResized
        // TODO add your handling code here:
        print();
    }//GEN-LAST:event_jScrollPane1ComponentResized

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            // TODO add your handling code here:
            chooseFile();
        } catch (IOException ex) {
            Logger.getLogger(GraphJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        print();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            // TODO add your handling code here:
            chooseDirectory();
        } catch (IOException ex) {
            Logger.getLogger(GraphJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        print();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        if (gr.topological_sort()==3)
        {
            jButton1.setVisible(false);
            jScrollPane2.setVisible(false);
        }
        if (gr.getError())
        {
            jButton1.setVisible(false);
        }
        print();
        for (int i=0; i<gr.getMessage().size(); i++)
        {
            jEditorPane1.setText(jEditorPane1.getText()+gr.getMessage().get(i)+"\n");
        }
        gr.clearMessage();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jPanel2ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentResized
        // TODO add your handling code here:
        print();
    }//GEN-LAST:event_jPanel2ComponentResized

    private void jPanel2ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentMoved
        // TODO add your handling code here:
        print();
    }//GEN-LAST:event_jPanel2ComponentMoved

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        // TODO add your handling code here:
        print();
    }//GEN-LAST:event_formWindowDeiconified

    public void chooseFile() throws IOException{
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            gr=new Graph();
            jMenuItem1.setEnabled(false);
            jMenuItem2.setEnabled(true);
            jButton1.setVisible(true);
            jScrollPane2.setVisible(true);
            gr.setEdges(Work.ReadFile(file.getAbsolutePath()));
            createPanel(printGraph());
            lock=true;
        }
    }

    public void chooseDirectory() throws IOException{
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showSaveDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            Work.WriteFile(gr.getEdges(), file.getAbsolutePath());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GraphJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphJFrame().setVisible(true);
            }
        });
    }


    private Graph gr;
    private boolean lock;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    // End of variables declaration//GEN-END:variables
}