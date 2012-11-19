/*
 * Copyright (c) 2003 - 2012 Sileria, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.sileria.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * <code>CheckList</code> is an implementation of {@link java.util.List List} which
 * maintains a map of boolean checks. This class can be used to represent a list or table
 * data which has checkboxes to represent the row selection. It contains methods like
 * <code>isSelected(index)</code> and <code>isSelected(E)</code> to determine whether
 * a row or a list item is checked.
 *
 * @author Ahmed Shakil
 * @version 2.1
 * @date Oct 20, 2007
 *
 * @param <E> Type of objects that the List will contain
 */
public class CheckList<E> extends AbstractList<E>
{
	private final Map<E, Boolean> checks = new HashMap<E, Boolean>();
	private List<E> list;

	/**
	 * Default constructors maintains an internal <code>ArrayList</code>.
	 */
	public CheckList () {
		list = new ArrayList<E>();
	}

	/**
	 * Constructs a <code>CheckList</code> from the specified collection.
	 * Internally an <code>ArrayList</code> is used.
	 * @param c <code>Collection</code>
	 */
	public CheckList (Collection<? extends E> c) {
		list = new ArrayList<E>( c );
	}

	/**
	 * Constructs a <code>CheckList</code> with the specified <code>list</code>
	 * which will be used as the delegate.
	 * @param list <code>List</code>
	 */
	public CheckList (List<E> list) {
		this.list = list;
	}

	/**
	 * Specify an underlying <code>List</code> object.
	 * @param list <code>List</code>
	 */
	public void setListData (List<E> list) {
		if (list == this.list) return;

		this.list = list;
		checks.clear();
	}

	/**
	 * Returns the number of elements in this list.  If this list contains
	 * more than <tt>Integer.MAX_VALUE</tt> elements, returns
	 * <tt>Integer.MAX_VALUE</tt>.
	 *
	 * @return the number of elements in this list.
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 * @return <tt>true</tt> if this list contains no elements.
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if specified <code>obj</code> is selected
	 * @return <tt>true</tt> if specified <code>obj</code> is selected
	 */
	public boolean isSelected (E obj) {
		Boolean b = checks.get( obj );
		return b != null && b;
	}

	/**
	 * Returns <tt>true</tt> if specified row <code>index</code> is selected
	 * @return <tt>true</tt> if specified row <code>index</code> is selected
	 */
	public boolean isSelected (int index) {
		return isSelected( list.get( index ) );
	}

	/**
	 * Returns <tt>true</tt> if all rows are selected; otherwise <code>false</code>.
	 * @return <tt>true</tt> if all rows are selected; otherwise <code>false</code>
	 */
	public boolean isSelectedAll () {
		for(int index=0; index < list.size(); index++) {
			if (!isSelected(index))
				return false;
		}
		return true;
	}

	/**
	 *
	 * Returns <tt>true</tt> if this list contains the specified element.
	 * More formally, returns <tt>true</tt> if and only if this list contains
	 * at least one element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
	 *
	 * @param o element whose presence in this list is to be tested.
	 * @return <tt>true</tt> if this list contains the specified element.
	 * @throws ClassCastException if the type of the specified element
	 * 	       is incompatible with this list (optional).
	 * @throws NullPointerException if the specified element is null and this
	 *         list does not support null elements (optional).
	 */
	public boolean contains (Object o) {
		return list.contains( o );
	}

	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 *
	 * @return an iterator over the elements in this list in proper sequence.
	 */
	public Iterator<E> iterator () {
		return list.iterator();
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence.  Obeys the general contract of the
	 * <tt>Collection.toArray</tt> method.
	 *
	 * @return an array containing all of the elements in this list in proper
	 *	       sequence.
	 * @see Arrays#asList(Object[])
	 */
	public Object[] toArray () {
		return list.toArray();
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence; the runtime type of the returned array is that of the
	 * specified array.  Obeys the general contract of the
	 * <tt>Collection.toArray(Object[])</tt> method.
	 *
	 * @param a the array into which the elements of this list are to
	 *		be stored, if it is big enough; otherwise, a new array of the
	 * 		same runtime type is allocated for this purpose.
	 * @return  an array containing the elements of this list.
	 *
	 * @throws ArrayStoreException if the runtime type of the specified array
	 * 		  is not a supertype of the runtime type of every element in
	 * 		  this list.
	 * @throws NullPointerException if the specified array is <tt>null</tt>.
	 */
	public <T> T[] toArray (T[] a) {
		return list.toArray( a );
	}

	/**
	 * Appends the specified element to the end of this list. <p>
	 *
	 * Lists that support this operation may place limitations on what
	 * elements may be added to this list.  In particular, some
	 * lists will refuse to add null elements, and others will impose
	 * restrictions on the type of elements that may be added.  List
	 * classes should clearly specify in their documentation any restrictions
	 * on what elements may be added.
	 *
	 * @param o element to be appended to this list.
	 * @return <tt>true</tt> if item was added successfully.
	 *
	 * @throws UnsupportedOperationException if the <tt>add</tt> method is not
	 * 		  supported by this list.
	 * @throws ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 * @throws NullPointerException if the specified element is null and this
	 *           list does not support null elements.
	 * @throws IllegalArgumentException if some aspect of this element
	 *            prevents it from being added to this list.
	 */
	public boolean add (E o) {
		return list.add( o );
	}

	/**
	 * Appends the specified element to the end of this list with the
	 * specified selected state.
	 * @param o element to be appended to this list.
	 * @param selected <tt>true</tt> to set the newly inserted item as checked
	 * @return <tt>true</tt> if item was added successfully.
	 */
	public boolean add (E o, boolean selected) {
		checks.put( o, selected?Boolean.TRUE:Boolean.FALSE );
		return list.add( o );
	}

	/**
	 * Get the underlying <code>List</code> which is being used as the delegate.
	 * @return <code>List</code>
	 */
	public List<E> getListData () {
		return list;
	}

	/**
	 * Get an array of selected objects.
	 * @return Returns a non null array object
	 */
	public Object[] getSelections () {
		return getSelections( Object.class );
	}

	/**
	 * Get the number of rows that have the selected check.
	 * @return selection count
	 */
	public int getSelectionCount () {
		int count = 0;
		for (Boolean checked : checks.values())
			if (checked) ++count;
		return count;
	}

	/**
	 * Get selected objects into an array of the specified type.
	 * @param classType class
	 * @return Returns a non null array object
	 */
	public E[] getSelections (Class<?> classType) {
		List<E> list = new ArrayList<E>();
		for (Map.Entry<E, Boolean> entry : checks.entrySet()) {
			if (entry.getValue().equals(Boolean.TRUE))
				list.add(entry.getKey());
		}

		@SuppressWarnings("unchecked")
		E[] array = (E[])Array.newInstance( classType, list.size() );
		return list.toArray( array );
	}

	/**
	 * Set the selected state of specified row <code>item</code>.
	 * @param item Item to select or deselect
	 * @param selected selected state
	 * @return the previous selected state
	 */
	public boolean setSelected (E item, boolean selected) {
		Boolean b = checks.put( item, selected );
		return b != null && b;
	}

	/**
	 * Set the row <code>index</code> to the specifed <code>selected</code> state.
	 * @param index row index
	 * @param selected selected state
	 * @return the previous selected state
	 */
	public boolean setSelected (int index, boolean selected) {
		E obj = list.get( index );
		Boolean b = checks.put( obj, selected );
		return b != null && b;
	}

	/**
	 * Toggle the selection state of sepcirow <code>index</code> betwee
	 * @param item Item to select or deselect
	 * @return the new selected state
	 */
	public boolean setToggle (E item) {
		return !setSelected( item, !isSelected( item ) );
	}

	/**
	 * Toggle the selection state of specified row <code>index</code>.
	 * @param index row index
	 * @return the new selected state
	 */
	public boolean setToggle (int index) {
		return !setSelected( index, !isSelected( index ) );
	}

	/**
	 * Selects or deselects all rows in the list based on the specified <code>selected</code> state.
	 * @param selected selection state
	 */
	public void setSelectedAll (boolean selected) {
		for(int index=0; index < list.size(); index++) {
			setSelected(index, selected);
		}
	}

	/**
	 * Removes the first occurrence in this list of the specified element.
	 * If this list does not contain the element, it is unchanged.
	 * More formally, removes the element with the lowest index i
	 * such that <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>
	 * (if such an element exists).
	 *
	 * @param o element to be removed from this list, if present.
	 * @return <tt>true</tt> if this list contained the specified element.
	 * @throws ClassCastException if the type of the specified element
	 * 	          is incompatible with this list (optional).
	 * @throws NullPointerException if the specified element is null and this
	 *            list does not support null elements (optional).
	 * @throws UnsupportedOperationException if the <tt>remove</tt> method is
	 *		  not supported by this list.
	 */
	public boolean remove (Object o) {
		checks.remove( o );
		return list.remove( o );
	}

	/**
	 *
	 * Returns <tt>true</tt> if this list contains all of the elements of the
	 * specified collection.
	 *
	 * @param  c collection to be checked for containment in this list.
	 * @return <tt>true</tt> if this list contains all of the elements of the
	 * 	       specified collection.
	 * @throws ClassCastException if the types of one or more elements
	 *         in the specified collection are incompatible with this
	 *         list (optional).
	 * @throws NullPointerException if the specified collection is null or
	 *         contains one or more null elements and this list does not
	 *         support null elements (optional).
	 * @see #contains(Object)
	 */
	public boolean containsAll (Collection<?> c) {
		return list.containsAll( c );
	}

	/**
	 * Appends all of the elements in the specified collection to the end of
	 * this list, in the order that they are returned by the specified
	 * collection's iterator.  The behavior of this
	 * operation is unspecified if the specified collection is modified while
	 * the operation is in progress.  (Note that this will occur if the
	 * specified collection is this list, and it's nonempty.)
	 *
	 * @param c collection whose elements are to be added to this list.
	 * @return <tt>true</tt> if this list changed as a result of the call.
	 *
	 * @throws UnsupportedOperationException if the <tt>addAll</tt> method is
	 *         not supported by this list.
	 * @throws ClassCastException if the class of an element in the specified
	 * 	       collection prevents it from being added to this list.
	 * @throws NullPointerException if the specified collection contains one
	 *         or more null elements and this list does not support null
	 *         elements, or if the specified collection is <tt>null</tt>.
	 * @throws IllegalArgumentException if some aspect of an element in the
	 *         specified collection prevents it from being added to this
	 *         list.
	 * @see #add(Object)
	 */
	public boolean addAll (Collection<? extends E> c) {
		return list.addAll( c );
	}

	/**
	 * Inserts all of the elements in the specified collection into this
	 * list at the specified position (optional operation).  Shifts the
	 * element currently at that position (if any) and any subsequent
	 * elements to the right (increases their indices).  The new elements
	 * will appear in this list in the order that they are returned by the
	 * specified collection's iterator.  The behavior of this operation is
	 * unspecified if the specified collection is modified while the
	 * operation is in progress.  (Note that this will occur if the specified
	 * collection is this list, and it's nonempty.)
	 *
	 * @param index index at which to insert first element from the specified
	 *	            collection.
	 * @param c elements to be inserted into this list.
	 * @return <tt>true</tt> if this list changed as a result of the call.
	 *
	 * @throws UnsupportedOperationException if the <tt>addAll</tt> method is
	 *		  not supported by this list.
	 * @throws ClassCastException if the class of one of elements of the
	 * 		  specified collection prevents it from being added to this
	 * 		  list.
	 * @throws NullPointerException if the specified collection contains one
	 *           or more null elements and this list does not support null
	 *           elements, or if the specified collection is <tt>null</tt>.
	 * @throws IllegalArgumentException if some aspect of one of elements of
	 *		  the specified collection prevents it from being added to
	 *		  this list.
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *		  &lt; 0 || index &gt; size()).
	 */
	public boolean addAll (int index, Collection<? extends E> c) {
		return list.addAll( index, c );
	}

	/**
	 * Removes from this list all the elements that are contained in the
	 * specified collection (optional operation).
	 *
	 * @param c collection that defines which elements will be removed from
	 *          this list.
	 * @return <tt>true</tt> if this list changed as a result of the call.
	 *
	 * @throws UnsupportedOperationException if the <tt>removeAll</tt> method
	 * 		  is not supported by this list.
	 * @throws ClassCastException if the types of one or more elements
	 *            in this list are incompatible with the specified
	 *            collection (optional).
	 * @throws NullPointerException if the specified collection is null or
	 *         contains one or more null elements and this list does not
	 *         support null elements (optional).
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	public boolean removeAll (Collection<?> c) {
		boolean success = true;
		for (Object o : c)
			success &= remove(o);
		return success;
	}

	/**
	 * Retains only the elements in this list that are contained in the
	 * specified collection (optional operation).  In other words, removes
	 * from this list all the elements that are not contained in the specified
	 * collection.
	 *
	 * @param c collection that defines which elements this set will retain.
	 *
	 * @return <tt>true</tt> if this list changed as a result of the call.
	 *
	 * @throws UnsupportedOperationException if the <tt>retainAll</tt> method
	 * 		  is not supported by this list.
	 * @throws ClassCastException if the types of one or more elements
	 *            in this list are incompatible with the specified
	 *            collection (optional).
	 * @throws NullPointerException if the specified collection is null or
	 *         contains one or more null elements and this list does not
	 *         support null elements (optional).
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	public boolean retainAll (Collection<?> c) {
		boolean modified = false;
		for( int i=size()-1; i>=0; i-- ) {
			if (!c.contains(get(i))) {
				remove( i );
				modified = true;
			}
		}
		return modified;
	}

	/**
	 * Removes all of the elements from this list.  This list will be empty
	 * after this call returns (unless it throws an exception). The method
	 * will also clear all the selected checks.
	 *
	 * @throws UnsupportedOperationException if the <tt>clear</tt> method is
	 * 		  not supported by this list.
	 */
	public void clear() {
		list.clear();
		checks.clear();
	}

	/**
	 * Clears all the selected checks.
	 */
	public void clearSelection () {
		checks.clear();
	}

	/**
	 * Compares the specified object with this list for equality.  Returns
	 * <tt>true</tt> if and only if the specified object is also a list, both
	 * lists have the same size, and all corresponding pairs of elements in
	 * the two lists are <i>equal</i>.  (Two elements <tt>e1</tt> and
	 * <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ? e2==null :
	 * e1.equals(e2))</tt>.)  In other words, two lists are defined to be
	 * equal if they contain the same elements in the same order.  This
	 * definition ensures that the equals method works properly across
	 * different implementations of the <tt>List</tt> interface.
	 *
	 * @param o the object to be compared for equality with this list.
	 * @return <tt>true</tt> if the specified object is equal to this list.
	 */
	public boolean equals (Object o) {
		return list.equals(o);
	}

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of element to return.
	 * @return the element at the specified position in this list.
	 *
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 * 		  &lt; 0 || index &gt;= size()).
	 */
	public E get (int index) {
		return list.get( index );
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element (optional operation). <p>
	 *
	 * This implementation always throws an
	 * <tt>UnsupportedOperationException</tt>.
	 *
	 * @param index index of element to replace.
	 * @param element element to be stored at the specified position.
	 * @return the element previously at the specified position.
	 *
	 * @throws UnsupportedOperationException if the <tt>set</tt> method is not
	 *		  supported by this List.
	 * @throws ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 * @throws IllegalArgumentException if some aspect of the specified
	 *		  element prevents it from being added to this list.
	 *
	 * @throws IndexOutOfBoundsException if the specified index is out of
	 *            range (<tt>index &lt; 0 || index &gt;= size()</tt>).
	 */
	public E set (int index, E element) {
		return list.set( index, element );
	}

	/**
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted.
	 * @param element element to be inserted.
	 *
	 * @throws UnsupportedOperationException if the <tt>add</tt> method is not
	 *		  supported by this list.
	 * @throws    ClassCastException if the class of the specified element
	 * 		  prevents it from being added to this list.
	 * @throws    NullPointerException if the specified element is null and
	 *            this list does not support null elements.
	 * @throws    IllegalArgumentException if some aspect of the specified
	 *		  element prevents it from being added to this list.
	 * @throws    IndexOutOfBoundsException if the index is out of range
	 *		  (index &lt; 0 || index &gt; size()).
	 */
	public void add (int index, E element) {
		list.add( index, element );
	}

	/**
	 * Inserts the specified element at the specified position in this list
	 * with the sepecified selected index.
	 *
	 * @param index index at which the specified element is to be inserted.
	 * @param element element to be inserted.
	 * @param selected selection state
	 */
	public void add (int index, E element, boolean selected) {
		list.add( index, element );
		checks.put( element, selected?Boolean.TRUE:Boolean.FALSE );
	}

	/**
	 * Removes the element at the specified position in this list (optional
	 * operation).  Shifts any subsequent elements to the left (subtracts one
	 * from their indices).  Returns the element that was removed from the
	 * list.
	 *
	 * @param index the index of the element to removed.
	 * @return the element previously at the specified position.
	 *
	 * @throws UnsupportedOperationException if the <tt>remove</tt> method is
	 *		  not supported by this list.
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *            &lt; 0 || index &gt;= size()).
	 */
	public E remove(int index) {
		E obj = list.remove( index );
		checks.remove( obj );
		return obj;
	}

	/**
	 * Returns the index in this list of the first occurrence of the specified
	 * element, or -1 if this list does not contain this element.
	 * More formally, returns the lowest index <tt>i</tt> such that
	 * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for.
	 * @return the index in this list of the first occurrence of the specified
	 * 	       element, or -1 if this list does not contain this element.
	 * @throws ClassCastException if the type of the specified element
	 * 	       is incompatible with this list (optional).
	 * @throws NullPointerException if the specified element is null and this
	 *         list does not support null elements (optional).
	 */
	public int indexOf(Object o) {
		return list.indexOf( o );
	}

	/**
	 * Returns the index in this list of the last occurence of the specified
	 * element, or -1 if the list does not contain this element.  More
	 * formally, returns the highest index <tt>i</tt> such that <tt>(o==null ?
	 * get(i)==null : o.equals(get(i)))</tt>, or -1 if there is no such
	 * index.<p>
	 *
	 * This implementation first gets a list iterator that points to the end
	 * of the list (with listIterator(size())).  Then, it iterates backwards
	 * over the list until the specified element is found, or the beginning of
	 * the list is reached.
	 *
	 * @param o element to search for.
	 *
	 * @return the index in this list of the last occurence of the specified
	 * 	       element, or -1 if the list does not contain this element.
	 */
	public int lastIndexOf(Object o) {
		return list.lastIndexOf( o );
	}

	/**
	 * Returns a list iterator of the elements in this list (in proper
	 * sequence).
	 *
	 * @return a list iterator of the elements in this list (in proper
	 * 	       sequence).
	 */
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	/**
	 * Returns a list iterator of the elements in this list (in proper
	 * sequence), starting at the specified position in this list.  The
	 * specified index indicates the first element that would be returned by
	 * an initial call to the <tt>next</tt> method.  An initial call to
	 * the <tt>previous</tt> method would return the element with the
	 * specified index minus one.
	 *
	 * @param index index of first element to be returned from the
	 *		    list iterator (by a call to the <tt>next</tt> method).
	 * @return a list iterator of the elements in this list (in proper
	 * 	       sequence), starting at the specified position in this list.
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *         &lt; 0 || index &gt; size()).
	 */
	public ListIterator<E> listIterator(int index) {
		return list.listIterator( index );
	}
}
