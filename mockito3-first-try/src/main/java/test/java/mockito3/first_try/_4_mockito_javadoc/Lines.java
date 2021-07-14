package test.java.mockito3.first_try._4_mockito_javadoc;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Lines implements List<Line> {

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @Override
  public Iterator<Line> iterator() {
    return null;
  }

  @Override
  public Object[] toArray() {
    return new Object[0];
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return null;
  }

  @Override
  public boolean add(Line line) {
    return false;
  }

  @Override
  public boolean remove(Object o) {
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends Line> c) {
    return false;
  }

  @Override
  public boolean addAll(int index, Collection<? extends Line> c) {
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  @Override
  public void clear() {

  }

  @Override
  public boolean equals(Object o) {
    return false;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public Line get(int index) {
    return null;
  }

  @Override
  public Line set(int index, Line element) {
    return null;
  }

  @Override
  public void add(int index, Line element) {

  }

  @Override
  public Line remove(int index) {
    return null;
  }

  @Override
  public int indexOf(Object o) {
    return 0;
  }

  @Override
  public int lastIndexOf(Object o) {
    return 0;
  }

  @Override
  public ListIterator<Line> listIterator() {
    return null;
  }

  @Override
  public ListIterator<Line> listIterator(int index) {
    return null;
  }

  @Override
  public List<Line> subList(int fromIndex, int toIndex) {
    return null;
  }
}
