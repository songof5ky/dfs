/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package depthfirstsearch;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;


public class Graph {

    private boolean end_sort, end_dfs, error;

    private int[] numbers;
    private int[] colors;

    private Stack<Integer> stack;
    private Stack<Integer> step_index_dfs;

    private ArrayList<String> message;
    private ArrayList<ArrayList<Integer>> edges;

    public Graph() {
        edges = new ArrayList<>();
        stack = new Stack<>();
        end_dfs = false;
        end_sort = false;
        error = false;
        step_index_dfs = new Stack<>();
        message = new ArrayList<>();
    }

    //шаговый обход в глубину
    private int step_dfs() {
        //System.out.println(step_index_dfs);
        if (step_index_dfs.empty()) {
            if (check_step_dfs()) {
                if (step_dfs() == -1) {
                    return -1;
                }
                return 0;
            }
            end_dfs = true;
            return 1;
        }

        int v = step_index_dfs.pop();
        int i = step_index_dfs.pop();

        message.add("Текущая вершина: " + (v + 1));

        if (colors[v] == 2) {
            message.add("Эта вершина уже использована. Возврат к предыдушей вершине.");
            return step_dfs();
        }

        message.add("Окрасим ее в серый цвет.");
        colors[v] = 1;

        if (!edges.get(v).isEmpty() && i < edges.get(v).size()) {

            System.out.println("Test");

            step_index_dfs.push(i + 1);
            step_index_dfs.push(v);

            step_index_dfs.push(0);
            step_index_dfs.push(edges.get(v).get(i));

            return 1;
        }

        message.add("Из вершины " + (v + 1) + " необследованных путей нет.");
        message.add("Помещаем в стек вершину с номером:" + (v + 1));
        message.add("Возврат к предыдушей вершине.");

        stack.push(v);
        colors[v] = 2;

        return 0;
    }

    private boolean check_step_dfs() {
        for (int i = 0; i < edges.size(); i++) {
            if (colors[i] == 0) {
                step_index_dfs.push(0);
                step_index_dfs.push(i);
                return true;
            }
        }
        return false;
    }

    private void replacement_rule() {

        if (stack.empty()) {
            return;
        }

        for (int i = 0; i < edges.size(); i++) {
            numbers[stack.pop()] = i;
        }
    }

    private void print() {
        for (int i = 0; i < edges.size(); i++) {
            System.out.print((i + 1) + "(" + colors[i] + ")\t|");
            for (int j = 0; j < edges.get(i).size(); j++) {
                System.out.print(" -> " + edges.get(i).get(j));
            }
            System.out.println("-> X");
        }
    }

    public int topological_sort() {

        if (error) {
            error("Cycle!");
            message.add("Алгоритм топологической сортировки не может быть выполнен, так как в графе был обнаружен цикл.");
            return -1;
        }

        if (end_sort) {
            System.out.println("!end_sort");
            return 1;
        }

        if (!end_dfs) {
            System.out.println("!end_dfs");
            return step_dfs();
        }

        message.add("По очереди достаем все вершины из стека и присваиваем им номера в порядке вытаскивания из стека.");

        replacement_rule();

        System.out.println("exit of dfs");

        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.get(i).size(); j++) {
                edges.get(i).set(j, numbers[edges.get(i).get(j)]);
            }
        }

        ArrayList<Integer> temp;

        int itemp;

        do {
            for (int i = 0; i < edges.size(); i++) {
                if (numbers[i] != i) {
                    temp = edges.get(i);
                    edges.set(i, edges.get(numbers[i]));
                    edges.set(numbers[i], temp);
                    itemp = numbers[numbers[i]];
                    numbers[numbers[i]] = numbers[i];
                    numbers[i] = itemp;
                }
            }
        } while (!correct_rule());

        for (int i = 0; i < edges.size(); i++) {
            colors[i] = 0;
        }

        end_sort = true;

        message.add("Алгоритм топологической сортировки завершен. Граф Отсортирован.");

        return 3;
    }

    private boolean correct_rule() {
        for (int i = 0; i < edges.size(); i++) {
            if (numbers[i] != i) {
                return false;
            }
        }
        return true;
    }

    public boolean getError() {
        return error;
    }

    private void error(String er_mes) {
        JFrame errFrame = new JFrame();
        errFrame.setTitle("Error!");
        errFrame.setPreferredSize(new Dimension(200, 100));
        JLabel mess = new JLabel();
        mess.setText(er_mes);
        errFrame.add(mess);
        errFrame.pack();
        errFrame.setVisible(true);
    }

    public boolean setEdges(ArrayList<String> edges_load) {

        if (edges_load == null) {
            error("Error!!! Broken graph!");
            return false;
        }

        for (int i = 0; i < edges_load.size(); i++) {

            StringTokenizer str = new StringTokenizer(edges_load.get(i), " ", false);

            ArrayList<Integer> temp = new ArrayList<>();

            boolean first = true;

            while (str.hasMoreTokens()) {

                int t = Integer.parseInt(str.nextToken());

                if (t > edges_load.size()) {
                    error("Error!!! Broken graph!");
                    return false;
                }

                if (first) {
                    first = false;
                } else {
                    temp.add(t - 1);
                }
            }

            edges.add(temp);
        }

        colors = new int[edges.size()];

        numbers = new int[edges.size()];

        return true;
    }

    public ArrayList<ArrayList<Integer>> getGraph() {
        return edges;
    }

    public ArrayList<String> getEdges() {

        ArrayList<String> str = new ArrayList<>();

        for (int i = 0; i < edges.size(); i++) {

            String temp = "";

            for (int j = 0; j < edges.get(i).size(); j++) {
                temp += String.valueOf(edges.get(i).get(j) + 1);
                temp += " ";
            }

            str.add(temp);
        }

        return str;
    }

    public int[] getColors() {
        return colors;
    }

    public Stack<Integer> getStack() {
        return stack;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void clearMessage() {
        message.clear();
    }
}
