package io.github.busy_spin.fix_showcase;

import java.lang.reflect.Array;

public class TailingLog {
    private String[] container;
    private int first = -1;
    private int last = -1;


    public TailingLog(int size) {
        container = new String[size];
    }

    public String[] tail(int size) {
        if (first == -1) {
            return new String[0];
        }
        if (first == last) {
            return new String[]{container[first]};
        }
        if (first < last) {
            int length = last - first + 1;
            String[] dest = new String[length];
            System.arraycopy(container, first, dest, 0, length);
            return dest;
        } else {
            //int firstPartLength =
            return null;
        }
    }
}
