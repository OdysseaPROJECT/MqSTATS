package mystatistics.util;

import java.util.ArrayList;
import java.util.Collection;

public class SortedList<E> extends ArrayList<E> {
    public SortedList() {}

    public SortedList(Collection<? extends E> c) {
        super(c);
    }

    public SortedList(int initialCapacity) {
        super(initialCapacity);
    }

    public boolean add(E e) {
        boolean add = super.add(e);
        sort(null);
        return add;
    }

    public void add(int index, E element) {
        super.add(index, element);
        sort(null);
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean addAll = super.addAll(c);
        sort(null);
        return addAll;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        boolean addAll = super.addAll(index, c);
        sort(null);
        return addAll;
    }

    public E set(int index, E element) {
        E set = super.set(index, element);
        sort(null);
        return set;
    }
}
