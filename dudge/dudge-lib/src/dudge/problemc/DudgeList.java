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
	
	private ObservableList<E> l= ObservableCollections.observableList(new ArrayList<E>());

	public DudgeList() {
	}

	public void addObservableListListener(ObservableListListener arg0) {
		l.addObservableListListener(arg0);
	}

	public void removeObservableListListener(ObservableListListener arg0) {
		l.removeObservableListListener(arg0);
	}

	public boolean supportsElementPropertyChanged() {
		return l.supportsElementPropertyChanged();
	}

	public int size() {
		return l.size();
	}

	public boolean isEmpty() {
		return l.isEmpty();
	}

	public boolean contains(Object o) {
		return l.contains(o);
	}

	public Iterator<E> iterator() {
		return l.iterator();
	}

	public Object[] toArray() {
		return l.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return l.toArray(a);
	}

	public boolean add(E e) {
		return l.add(e);
	}

	public boolean remove(Object o) {
		return l.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return l.containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c) {
		return l.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return l.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return l.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return l.retainAll(c);
	}

	public void clear() {
		l.clear();
	}

	public E get(int index) {
		return l.get(index);
	}

	public E set(int index, E element) {
		return l.set(index, element);
	}

	public void add(int index, E element) {
		l.add(index, element);
	}

	public E remove(int index) {
		return l.remove(index);
	}

	public int indexOf(Object o) {
		return l.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return l.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return l.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return l.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return l.subList(fromIndex, toIndex);
	}
}
