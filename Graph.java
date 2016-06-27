/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package depthfirstsearch;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Viktor
 */
public class Graph {
    public Graph()
    {
        edges=new ArrayList<ArrayList<Integer>> ();
        stack=new Stack<Integer> ();
        end_dfs=false;
        end_sort=false;
        error=false;
        step_index_dfs=new Stack<Integer>();
        message=new ArrayList<String>();
    }
    //шаговый обход в глубину
    private int step_dfs()
    {
        //System.out.println(step_index_dfs);
        if (step_index_dfs.empty())
        {
            if (check_step_dfs())
            {
                if (step_dfs()==-1)
                {
                    return -1;
                }
                return 0;
            }
            end_dfs=true;
            return 1;
        }
        int v=step_index_dfs.pop();
        int i=step_index_dfs.pop();
        if (i==0)
        {
            //System.out.println("Current: "+(v+1));
            message.add("Текущая вершина: "+(v+1));
            if (colors[v]==1)
            {
                //ERROR
                error("Cycle!");
                message.add("Обнаружен цикл в графе. Такой граф не может быть отсортирован.");
                error=true;
                return -1;
            }
            if (colors[v]==2)
            {
                message.add("Эта вершина уже использована. Возврат к предыдушей вершине.");
                return step_dfs();
            }
            message.add("Окрасим ее в серый цвет.");
            colors[v]=1;
            
        }
        
        if (!edges.get(v).isEmpty() && i<edges.get(v).size())
        {
            System.out.println("Test");
            step_index_dfs.push(0);
            step_index_dfs.push(edges.get(v).get(i));
            step_index_dfs.push(i+1);
            step_index_dfs.push(v);
            
            return 2;
        }
        message.add("Из вершины "+(v+1)+" необследованных путей нет.");
        message.add("Помещаем в стек вершину с номером:"+(v+1));
        message.add("Возврат к предыдушей вершине.");
        //System.out.println("push");
        stack.push(v);
        colors[v]=2;
        return 0;
    }

    private boolean check_step_dfs()
    {
        for (int i=0; i<edges.size(); i++)
        {
            if (colors[i]==0)
            {
                step_index_dfs.push(0);
                step_index_dfs.push(i);
                return true;
            }
        }
        return false;
    }
    private void replacement_rule()
    {
        if (stack.empty())
        {
            return;
        }
        for (int i=0; i<edges.size(); i++)
        {
            numbers[stack.pop()]=i;
        }
    }
    private void print()
    {
        for (int i=0; i<edges.size(); i++)
        {
            System.out.print((i+1)+"("+colors[i]+")\t|");
            for (int j=0; j<edges.get(i).size(); j++)
            {
                System.out.print("->"+edges.get(i).get(j));
            }
            System.out.println("->X");
        }
    }

   


