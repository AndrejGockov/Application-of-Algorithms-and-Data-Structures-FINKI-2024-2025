//        Во следната задача треба да изградите неподредено (општо) дрво со N јазли, за кое ќе треба да одговорите на Q прашања од видот „колку лисја има поддрвото на избран јазол“.
//        Секој јазол ќе има уникатен индекс од 1 до N. Коренот на дрвото секогаш ќе има индекс 1. Сите деца ќе имаат индекси поголеми од индексите на родителите.
//
//        Влезот ќе содржи N+Q редови од видот
//
//        root 1 - Треба да го поставите коренот на дрвото да биде јазелот со индекс 1
//        add parent_index child_index - Треба да додадете дете јазел со индекс child_index на јазелот со индекс parent_index
//        ask node_index - Треба да одговорите колку листови има во поддрвото на јазелот со индекс node_index
//
//        Пример и структура на влезот:
//        Влез:
//
//        11 5
//        root 1
//        add 1 2
//        add 2 3
//        ask 1
//        add 1 4
//        add 2 5
//        add 3 6
//        ask 2
//        add 3 7
//        ask 1
//        add 1 8
//        add 4 9
//        add 2 10
//        add 4 11
//        ask 2
//        ask 1
//
//        Излез:
//        1
//        2
//        4
//        4
//        7
//
//        Објаснување:
//        Прикажано е изгледот на дрвото при секое од 5те прашања во влезот
//        прашање 1
//        прашање 1: ask 1: Во поддрвото на јазелот 1 има 1 лист.
//        прашање 2: ask 2: Во поддрвото на јазелот 2 има 2 листови.
//        прашање 3: ask 1: Во поддрвото на јазелот 1 има 4 листови.
//        прашање 4: ask 2: Во поддрвото на јазелот 2 има 4 листови
//        прашање 5: ask 1: Во поддрвото на јазелот 1 има 7 листови

import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Tree interface
interface Tree<E> {
    // Accessors
    public Tree.Node<E> root();

    public Tree.Node<E> parent(Tree.Node<E> node);

    public int childCount(Tree.Node<E> node);

    // Transformers
    public void makeRoot(E elem);

    public Tree.Node<E> addChild(Tree.Node<E> node, E elem);

    public void remove(Tree.Node<E> node);

    // Iterators
    public Iterator<E> children(Tree.Node<E> node);

    public Iterable<Tree.Node<E>> childrenNodes(Tree.Node<E> node);

    // Inner interface for tree nodes
    public interface Node<E> {
        public E getElement();

        public void setElement(E elem);
    }
}

// SLLTree class
class SLLTree<E> implements Tree<E> {

    // Node implementation
    static class SLLNode<P> implements Node<P> {
        SLLNode<P> parent, sibling, firstChild;
        P element;

        public SLLNode(P o) {
            element = o;
            parent = sibling = firstChild = null;
        }

        public P getElement() {
            return element;
        }

        public void setElement(P o) {
            element = o;
        }
    }

    protected SLLNode<E> root;

    public SLLTree() {
        root = null;
    }

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public Node<E> parent(Node<E> node) {
        return ((SLLNode<E>) node).parent;
    }

    @Override
    public int childCount(Node<E> node) {
        SLLNode<E> tmp = ((SLLNode<E>) node).firstChild;
        int num = 0;
        while (tmp != null) {
            tmp = tmp.sibling;
            num++;
        }
        return num;
    }

    @Override
    public void makeRoot(E elem) {
        root = new SLLNode<>(elem);
    }

    @Override
    public Node<E> addChild(Node<E> node, E elem) {
        SLLNode<E> tmp = new SLLNode<>(elem);
        SLLNode<E> curr = (SLLNode<E>) node;
        tmp.sibling = curr.firstChild;
        curr.firstChild = tmp;
        tmp.parent = curr;
        return tmp;
    }

    @Override
    public void remove(Node<E> node) {
        SLLNode<E> curr = (SLLNode<E>) node;
        if (curr.parent != null) {
            if (curr.parent.firstChild == curr) {
                curr.parent.firstChild = curr.sibling;
            } else {
                SLLNode<E> tmp = curr.parent.firstChild;
                while (tmp.sibling != curr) {
                    tmp = tmp.sibling;
                }
                tmp.sibling = curr.sibling;
            }
        } else {
            root = null;
        }
    }

    @Override
    public Iterator<E> children(Node<E> node) {
        return new SLLTreeIterator(((SLLNode<E>) node).firstChild);
    }

    @Override
    public Iterable<Node<E>> childrenNodes(Node<E> node) {
        return new NodeIterable(((SLLNode<E>) node).firstChild);
    }

    public Node<E> findNode(E value, Node<E> currentNode) {
        if (currentNode == null) {
            return null;
        }
        if (currentNode.getElement().equals(value)) {
            return currentNode;
        }
        SLLNode<E> child = ((SLLNode<E>) currentNode).firstChild;
        while (child != null) {
            Node<E> result = findNode(value, child);
            if (result != null) {
                return result;
            }
            child = child.sibling;
        }
        return null;
    }

    class SLLTreeIterator implements Iterator<E> {
        SLLNode<E> current;

        public SLLTreeIterator(SLLNode<E> node) {
            current = node;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            SLLNode<E> tmp = current;
            current = current.sibling;
            return tmp.getElement();
        }
    }

    class NodeIterable implements Iterable<Node<E>> {
        Node<E> start;

        public NodeIterable(Node<E> start) {
            this.start = start;
        }

        @Override
        public Iterator<Node<E>> iterator() {
            return new NodeIterator(start);
        }
    }

    class NodeIterator implements Iterator<Node<E>> {
        Node<E> current;

        public NodeIterator(Node<E> node) {
            current = node;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Node<E> next() {
            Node<E> tmp = current;
            current = ((SLLNode<E>) current).sibling;
            return tmp;
        }
    }
}

public class Main {
    public static <E> int countLeaves(SLLTree<E> tree, SLLTree.SLLNode<E> node) {
        if (node == null) {
            return 0;
        }
        if (node.firstChild == null) {
            return 1;
        }

        int count = 0;
        SLLTree.SLLNode<E> child = node.firstChild;
        while (child != null) {
            count += countLeaves(tree, child);
            child = child.sibling;
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int q = input.nextInt();
        input.nextLine();

        SLLTree<Integer> tree = new SLLTree<Integer>();

        for(int i = 0; i < n + q; i++){
            String line = input.nextLine().trim();
            String[] cuts = line.split(" ");


            if(cuts[0].equals("root")) {
                tree.makeRoot(Integer.parseInt(cuts[1]));
            }

            if(cuts[0].equals("add")){
                SLLTree.SLLNode<Integer> parentNode =
                        (SLLTree.SLLNode<Integer>)tree.findNode(Integer.parseInt(cuts[1]), tree.root());
                tree.addChild(parentNode, Integer.parseInt(cuts[2]));
            }

            if(cuts[0].equals("ask")){
                SLLTree.SLLNode<Integer> queryNode =
                        (SLLTree.SLLNode<Integer>) tree.findNode(Integer.parseInt(cuts[1]), tree.root());
                System.out.println(countLeaves(tree, queryNode));
            }
        }
    }
}