/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package depthfirstsearch;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class GraphJFrame extends JFrame {

    private Graph gr;

    private boolean lock;

    private JButton jButton1;
    private JEditorPane jEditorPane1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;

    /**
     * Creates new form GraphJFrame
     */
    public GraphJFrame() {
        initializeComponents();
    }

    private void createPanel(int size) {
        jPanel1.setPreferredSize(new Dimension(size, size));
        jPanel1.updateUI();
        jScrollPane1.updateUI();
        jPanel2.setPreferredSize(new Dimension(jPanel1.getWidth(), (gr.getEdges().size() + 2) * 20));
        jPanel2.updateUI();
        jScrollPane2.updateUI();
    }

    private void print() {
        if (!lock) {
            return;
        }
        printGraph();
        printStack();
    }

    private void printStack() {
        int width = 15;
        int height = jPanel2.getHeight() - 20;//145;
        int step = 0;
        setRectangle(jPanel2);
        Graphics2D st = (Graphics2D) jPanel2.getGraphics();
        Font font = new Font("Tahoma", Font.BOLD | Font.ITALIC, 15);
        st.setFont(font);
        for (int i = 0; i < gr.getStack().size(); i++) {
            st.drawLine(width, height - step, width + 50, height - step);//flor
            st.drawLine(width, height - step, width, height - step - 20);//left
            st.drawLine(width + 50, height - step, width + 50, height - step - 20);//right
            st.drawString(String.valueOf(gr.getStack().get(i) + 1), 2 * width, height - step);
            step += 20;
        }
        st.dispose();
    }

    private int printGraph() {
        ArrayList<ArrayList<Integer>> edges;
        edges = gr.getGraph();
        int n = edges.size();
        int r = 15;
        int tab = 20;
        //============изменяемая часть
        //закрасим участок рисования
        setRectangle(jPanel1);
        //рисуем вершины графа
        int R = (int) (r * n / Math.PI + 2 * r);
        if (!lock) {
            return 4 * tab + 2 * r + 2 * R;
        }
        double alpha = 2 * Math.PI / n;
        int centr = tab + r + R;
        double[] vect = new double[2];
        vect[0] = 0;
        vect[1] = R;
        for (int i = 0; i < n; i++) {
            setCircle((int) (centr + vect[0]), (int) (centr - vect[1]), r, i);
            vect = rotationVector(vect, alpha);
        }
        //рисуем связи
        for (int i = 0; i < n; i++) {
            int x0 = (int) (centr + vect[0]);
            int y0 = (int) (centr - vect[1]);
            for (int j = 0; j < edges.get(i).size(); j++) {
                int edg = edges.get(i).get(j) - i;
                vect = rotationVector(vect, alpha * edg);
                setArrow(x0, y0, (int) (centr + vect[0]), (int) (centr - vect[1]), r);
                vect = rotationVector(vect, -alpha * edg);
            }
            vect = rotationVector(vect, alpha);
        }
        return 4 * tab + 2 * r + 2 * R;
    }

    private void setArrow(int x1, int y1, int x2, int y2, int r) {
        x1 += r;
        y1 += r;
        x2 += r;
        y2 += r;
        double[] vect = new double[2];
        vect[0] = r;
        vect[1] = 0;
        double alpha = Math.acos((x1 - x2) / Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
        vect = rotationVector(vect, alpha);
        if (y1 - y2 < 0) {
            vect[1] = -vect[1];
        }
        x1 -= vect[0];
        y1 -= vect[1];
        vect = rotationVector(vect, Math.PI);
        x2 -= vect[0];
        y2 -= vect[1];
        int rad = 10;
        Graphics2D gfx = (Graphics2D) jPanel1.getGraphics();
        gfx.drawLine(x1, y1, x2, y2);
        vect[0] = rad;
        vect[1] = 0;
        vect = rotationVector(vect, alpha);
        if (y1 - y2 > 0) {
            vect[1] = -vect[1];
        }
        vect = rotationVector(vect, Math.PI / 6);
        gfx.drawLine((int) (x2 + vect[0]), (int) (y2 - vect[1]), x2, y2);
        vect = rotationVector(vect, -Math.PI / 3);
        gfx.drawLine((int) (x2 + vect[0]), (int) (y2 - vect[1]), x2, y2);
        gfx.dispose();
    }

    private void setRectangle(JPanel panel) {
        Graphics2D gfx = (Graphics2D) panel.getGraphics();
        gfx.setColor(panel.getBackground());
        gfx.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        gfx.dispose();
    }

    private double[] rotationVector(double[] vect, double alpha) {
        double temp = vect[0];
        vect[0] = temp * Math.cos(alpha) - vect[1] * Math.sin(alpha);
        vect[1] = temp * Math.sin(alpha) + vect[1] * Math.cos(alpha);
        return vect;
    }

    private void setCircle(int x, int y, int r, int number) {
        int FontSize = 12;
        Graphics2D gfx = (Graphics2D) jPanel1.getGraphics();
        Graphics2D g = (Graphics2D) jPanel1.getGraphics();
        Graphics2D col = (Graphics2D) jPanel1.getGraphics();
        int color = gr.getColors()[number];
        col.setColor(setColors(color));
        col.fillOval(x, y, 2 * r, 2 * r);
        g.setColor(selectColor(setColors(color)));
        Font font = new Font("Tahoma", Font.BOLD | Font.ITALIC, FontSize);
        gfx.drawOval(x, y, 2 * r, 2 * r);
        g.setFont(font);
        g.drawString(String.valueOf(number + 1), x + r - FontSize / 2, y + r + FontSize / 2);
        gfx.dispose();
        g.dispose();
        col.dispose();
    }

    public Color selectColor(Color cl) {
        return ((0.3 * cl.getAlpha() + 0.59 * cl.getGreen() + 0.11 * cl.getBlue()) > ((0.3 * 255 + 0.59 * 255 + 0.11 * 255) / 2))
                ? new Color(0, 0, 0) : new Color(255, 255, 255);
    }

    public Color setColors(int i) {
        return i == 0 ? new Color(255, 255, 255) :
                i == 1 ? new Color(187, 187, 187) :
                        i == 2 ? new Color(0, 0, 0) : null;
    }

    @SuppressWarnings("unchecked")
    private void initializeComponents() {

        jScrollPane1 = new JScrollPane();
        jPanel1 = new JPanel();
        jButton1 = new JButton();
        jScrollPane2 = new JScrollPane();
        jPanel2 = new JPanel();

        JScrollPane jScrollPane4 = new JScrollPane();

        jEditorPane1 = new JEditorPane();

        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu jMenu1 = new JMenu();

        jMenuItem1 = new JMenuItem();
        jMenuItem2 = new JMenuItem();

        JPopupMenu.Separator jSeparator1 = new JPopupMenu.Separator();
        JMenuItem jMenuItem3 = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowDeiconified(WindowEvent evt) {
                print();
            }
        });

        jScrollPane1
                .setOpaque(false);
        jScrollPane1
                .addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        print();
                    }
                });

        jPanel1.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent evt) {
                print();
            }

            public void componentResized(ComponentEvent evt) {
                print();
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);

        jPanel1.setLayout(jPanel1Layout);

        jPanel1Layout
                .setHorizontalGroup(
                        jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGap(0, 472, Short.MAX_VALUE));

        jPanel1Layout
                .setVerticalGroup(
                        jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGap(0, 364, Short.MAX_VALUE));

        jScrollPane1
                .setViewportView(jPanel1);

        jButton1.setHorizontalTextPosition(SwingConstants.CENTER);

        jButton1.setInheritsPopupMenu(true);

        jButton1.setText("sort: next_step");

        jButton1.setVisible(false);

        jButton1.addActionListener(e -> {

            if (gr.topological_sort() == 3) {
                jButton1.setVisible(false);
                jScrollPane2.setVisible(false);
            }

            if (gr.getError()) {
                jButton1.setVisible(false);
            }

            print();

            for (int i = 0; i < gr.getMessage().size(); i++) {
                jEditorPane1.setText(jEditorPane1.getText() + gr.getMessage().get(i) + "\n");
            }

            gr.clearMessage();
        });

        jScrollPane2
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2
                .setPreferredSize(new Dimension(110, 170));
        jScrollPane2
                .setVisible(false);

        jPanel2.setPreferredSize(new Dimension(110, 170));

        jPanel2.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent evt) {
                print();
            }

            public void componentResized(ComponentEvent evt) {
                print();
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);

        jPanel2.setLayout(jPanel2Layout);

        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 110, Short.MAX_VALUE));

        jPanel2Layout.setVerticalGroup(
                jPanel2Layout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE));

        jScrollPane2.setViewportView(jPanel2);

        jScrollPane4
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4
                .setToolTipText("");
        jScrollPane4
                .setAutoscrolls(true);

        jEditorPane1
                .setEditable(false);
        jScrollPane4
                .setViewportView(jEditorPane1);

        jMenu1.setText("File");

        jMenuItem1
                .setText("Открыть граф");
        jMenuItem1
                .addActionListener(evt -> {
                    try {
                        chooseFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    print();
                });

        jMenu1.add(jMenuItem1);

        jMenuItem2
                .setText("Сохранить граф");
        jMenuItem2
                .setEnabled(false);
        jMenuItem2
                .addActionListener(evt -> {
                    try {
                        chooseDirectory();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    print();
                });

        jMenu1.add(jMenuItem2);

        jMenu1.add(jSeparator1);

        jMenuItem3
                .setText("Выход");
        jMenuItem3
                .addActionListener(evt -> dispose());

        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());

        getContentPane()
                .setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout
                                .createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addGroup(layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout
                                                .createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout
                                                .createSequentialGroup()
                                                .addPreferredGap(LayoutStyle
                                                        .ComponentPlacement.RELATED)
                                                .addComponent(jButton1)))
                                .addContainerGap())
                        .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE));

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout
                                .createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout
                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout
                                                .createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

        pack();
        setResizable(false);
    }

    public void chooseFile() throws IOException {

        JFileChooser fileOpen = new JFileChooser();

        if (fileOpen.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            File file = fileOpen.getSelectedFile();

            gr = new Graph();

            jMenuItem1.setEnabled(false);
            jMenuItem2.setEnabled(true);

            jButton1.setVisible(true);

            jScrollPane2.setVisible(true);

            gr.setEdges(Work.readFile(file.getAbsolutePath()));

            createPanel(printGraph());

            lock = true;
        }
    }

    public void chooseDirectory() throws IOException {

        JFileChooser fileOpen = new JFileChooser();

        if (fileOpen.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            File file = fileOpen.getSelectedFile();

            Work.writeFile(gr.getEdges(), file.getAbsolutePath());
        }
    }

    public static void main(String args[]) {
        // Закидываем задачу в очередь AWT-Event-Queue на отрисовку окна
        EventQueue
                .invokeLater(() -> new GraphJFrame().setVisible(true));
    }
}
