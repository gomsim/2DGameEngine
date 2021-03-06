package Logic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An efficient an thread safe imlpementation of a sorted list. It uses the copy
 * on write pattern to guarantee thread safety. By always sorting incoming elements
 * and using a comparator to determine the proper place within the datastructure
 * upon insertion it avoids any swaps during the sorting process.
 * @param <E> Type co be contained in list
 */

public class SortedCopyOnWriteArrayList<E> implements Iterable<E>{

    private static final int MAX_TRIES = 100;
    private final Comparator<E> comparator;
    private final AtomicReference<Object[]> elements = new AtomicReference<>(new Object[0]);

    public SortedCopyOnWriteArrayList(Comparator<E> comparator){
        this.comparator = comparator;
    }

    //TODO: removeAll functions
    //TODO: binary search on add and remove (This data structire is far from the most intensive part of computation, so not prioritised)

    @SuppressWarnings("unchecked")
    public boolean add(E ... toAdd){
        if (toAdd.length == 0)
            return false;
        Object[] oldRef;
        Object[] newRef;
        Arrays.sort(toAdd,comparator.reversed());

        int i = 0;
        int round = 0;
        do{
            oldRef = elements.get();
            newRef = new Object[oldRef.length + toAdd.length];
            if (oldRef.length != 0){
                for (int o = 0, n = 0; n < newRef.length; o++, n++){
                    while (i < toAdd.length && (o >= oldRef.length || comparator.compare((E)oldRef[o],toAdd[i]) < 0)){
                        newRef[n++] = toAdd[i++];
                    }
                    if (o < oldRef.length)
                        newRef[n] = oldRef[o];
                }
            }else{
                for (int a = 0; a < newRef.length; a++)
                    newRef[a] = toAdd[a];
            }
        }while(!elements.compareAndSet(oldRef, newRef) && round++ < MAX_TRIES);

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean remove(E element){
        Object[] oldRef;
        Object[] newRef;
        boolean removed = false;
        int round = 0;

        do{
            oldRef = elements.get();
            newRef = new Object[oldRef.length - 1];
            for (int o = 0, n = 0; n < newRef.length; o++, n++){
                if (!removed && comparator.compare(element,(E)oldRef[o]) == 0 && oldRef[o].equals(element)){
                    o++;
                    removed = true;
                }
                newRef[n] = oldRef[o];
            }
        }while(!elements.compareAndSet(oldRef,newRef) && round++ < MAX_TRIES);

        return removed;
    }


    public int size(){
        return elements.get().length;
    }

    @Override
    public Iterator<E> iterator() {
        return new SortedCopyOnWriteArrayListIterator();
    }

    private class SortedCopyOnWriteArrayListIterator implements Iterator<E>{

        int i;
        Object[] copy;

        private SortedCopyOnWriteArrayListIterator(){
            copy = elements.get();
        }

        public boolean hasNext(){
            return i < copy.length;
        }
        @SuppressWarnings("unchecked")
        public E next(){
            if (!hasNext())
                throw new NoSuchElementException();
            return (E)copy[i++];
        }

        /**
         * Throws exception since removal of elements other than the first is forbidden for queues.
         */
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
}
