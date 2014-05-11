package com.redrockfowl.exnihilo.nei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Partition<T> implements Iterable<Pair<Collection<T>, Collection<T>>> {

    private final ArrayList<T> original;
    private final int n;
    private final int k;

    public Partition(Collection<T> original, int k) {
        this.original = new ArrayList<T>(original);
        this.n = original.size();
        this.k = k;
    }

    @Override
    public Iterator<Pair<Collection<T>, Collection<T>>> iterator() {
        return new PartitionIterator();
    }

    private class PartitionIterator implements Iterator<Pair<Collection<T>, Collection<T>>> {

        private int[] indices;
        private boolean done;

        public PartitionIterator() {
            indices = new int[k];
            for (int i = 0; i < k; i++) {
                indices[i] = i;
            }
            done = false;
        }

        @Override
        public boolean hasNext() {
            return !done;
        }

        @Override
        public Pair<Collection<T>, Collection<T>> next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Collection<T> fst = new ArrayList<T>(k);
            Collection<T> snd = new ArrayList<T>(n-k);

            for (int i = 0; i < k; i++) {
                fst.add(original.get(indices[i]));
            }

            for (int i = 0; i < n; i++) {
                if (!contains(indices, i)) {
                    snd.add(original.get(i));
                }
            }

            done = true;
            for (int i = k - 1; i >= 0; i--) {
                if (indices[i] < n - k + i) {
                    indices[i] += 1;
                    for (int j = i + 1; j < k; j++) {
                        indices[j] = indices[i] - i + j;
                    }
                    done = false;
                    break;
                }
            }

            return new Pair<Collection<T>, Collection<T>>(fst, snd);

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static boolean contains(int[] arr, int t) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == t) { return true; }
        }
        return false;
    }

}
