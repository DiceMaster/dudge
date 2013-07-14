/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dudge.problemc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

/**
 *
 * @author Arengor
 */
public class DudgeList<E> implements ObservableList<E> {
	
	private ObservableList<E> l = ObservableCollections.observableList(new ArrayList<E>());

	public DudgeList() {
	}

        @Override
	public void addObservableListListener(ObservableListListener arg0) {
		l.addObservableListListener(arg0);
	}

        @Override
	public void removeObservableListListener(ObservableListListener arg0) {
		l.removeObservableListListener(arg0);
	}

        @Override
	public boolean supportsElementPropertyChanged() {
		return l.supportsElementPropertyChanged();
	}

        @Override
	public int size() {
		return l.size();
	}

        @Override
	public boolean isEmpty() {
		return l.isEmpty();
	}

        @Override
	public boolean contains(Object o) {
		return l.contains(o);
	}

        @Override
	public Iterator<E> iterator() {
		return l.iterator();
	}

        @Override
	public Object[] toArray() {
		return l.toArray();
	}

        @Override
	public <T> T[] toArray(T[] a) {
		return l.toArray(a);
	}

        @Override
	public boolean add(E e) {
		return l.add(e);
	}

        @Override
	public boolean remove(Object o) {
		return l.remove(o);
	}

        @Override
	public boolean containsAll(Collection<?> c) {
		return l.containsAll(c);
	}

        @Override
	public boolean addAll(Collection<? extends E> c) {
		return l.addAll(c);
	}

        @Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return l.addAll(index, c);
	}

        @Override
	public boolean removeAll(Collection<?> c) {
		return l.removeAll(c);
	}

        @Override
	public boolean retainAll(Collection<?> c) {
		return l.retainAll(c);
	}

        @Override
	public void clear() {
		l.clear();
	}

        @Override
	public E get(int index) {
		return l.get(index);
	}

        @Override
	public E set(int index, E element) {
		return l.set(index, element);
	}

        @Override
	public void add(int index, E element) {
		l.add(index, element);
	}

        @Override
	public E remove(int index) {
		return l.remove(index);
	}

        @Override
	public int indexOf(Object o) {
		return l.indexOf(o);
	}

        @Override
	public int lastIndexOf(Object o) {
		return l.lastIndexOf(o);
	}

        @Override
	public ListIterator<E> listIterator() {
		return l.listIterator();
	}

        @Override
	public ListIterator<E> listIterator(int index) {
		return l.listIterator(index);
	}

        @Override
	public List<E> subList(int fromIndex, int toIndex) {
		return l.subList(fromIndex, toIndex);
	}
}
