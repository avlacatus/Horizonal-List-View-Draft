/*
 * Copyright (c) 2001 - 2012 Sileria, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.sileria.android.adapter;

import android.content.Context;

import java.util.*;

import android.widget.*;
import android.view.*;

/**
 * An abstract base adapter that uses a {@linkplain List} to renderer
 * the list items. Conveniently you will only have to override
 * {@linkplain BaseAdapter#getView(int, View, ViewGroup)}.
 * <p/>
 *
 * Example of an inner class that subclasses {@code AbstractListAdapter} for a {@code GridView}:
 *
 * <blockquote><pre>
 *
 *       public ButtonAdaptor (Context ctx) {
 *           super( ctx, gridIconList );
 *       }
 *
 *       public View getView (int index, View view, ViewGroup parent) {
 *           TextView btn;
 *           AssetType type = get( index );
 *           if (view == null)
 *               btn = UIScheme.createButton( ctx, type );
 *           else {
 *               UIScheme.setIcon( btn = (TextView)view, type );
 *               btn.setText( Resource.getString( type ) );
 *           }
 *
 *           return btn;
 *       }
 *   }
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date Apr 5, 2010
 *
 * @param <T> type of the row item
 */
public abstract class AbstractListAdapter<T> extends BaseAdapter implements List<T> {

    protected final Context ctx;
    protected List<T> data;

    protected T selection;

    /**
     * Constructor
     */
    protected AbstractListAdapter (Context ctx, List<T> data) {
        this.ctx = ctx;
        this.data = data == null ? Collections.<T>emptyList() : data;
    }

    /**
     * Set the new list data and triggers dataset change event.
     * @param data list data
     */
    public void setData (List<T> data) {
        this.data = data == null ? Collections.<T>emptyList() : data;
        notifyDataSetChanged();
    }

    /**
     * Set the new list data and triggers dataset change event.
     * @return list data
     */
    public List<T> getData () {
        return data;
    }

    /**
     * Get result count.
     */
    public int getCount () {
        return data.size();
    }

    /**
     * Get the row item.
     */
    public T getItem (int position) {
        return data.get( position );
    }

    /**
     * Get row id.
     */
    public long getItemId (int position) {
        return position;
    }

	/**
	 * Returns the number of elements in this list.  If this list contains
	 * more than <tt>Integer.MAX_VALUE</tt> elements, returns
	 * <tt>Integer.MAX_VALUE</tt>.
	 *
	 * @return the number of elements in this list.
	 */
	public int size () {
		return data.size();
	}

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 *
	 * @return <tt>true</tt> if this list contains no elements.
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 *
	 * Returns <tt>true</tt> if this list contains the specified element.
	 * More formally, returns <tt>true</tt> if and only if this list contains
	 * at least one element <tt>t</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;t==null&nbsp;:&nbsp;o.equals(t))</tt>.
	 *
	 * @param o element whose presence in this list is to be tested.
	 * @return <tt>true</tt> if this list contains the specified element.
	 * @throws ClassCastException if the type of the specified element
	 * 	       is incompatible with this list (optional).
	 * @throws NullPointerException if the specified element is null and this
	 *         list does not support null elements (optional).
	 */
	public boolean contains (Object o) {
		return data.contains( o );
	}

	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 *
	 * @return an iterator over the elements in this list in proper sequence.
	 */
	public Iterator<T> iterator() {
		return data.iterator();
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence.  Obeys the general contract of the
	 * <tt>Collection.toArray</tt> method.
	 *
	 * @return an array containing all of the elements in this list in proper
	 *	       sequence.
	 */
	public Object[] toArray() {
		return data.toArray();
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
		return data.toArray( a );
	}


	// Modification Operations

	/**
	 * Appends the specified element to the end of this list (optional
	 * operation). <p>
	 *
	 * Lists that support this operation may place limitations on what
	 * elements may be added to this list.  In particular, some
	 * lists will refuse to add null elements, and others will impose
	 * restrictions on the type of elements that may be added.  List
	 * classes should clearly specify in their documentation any restrictions
	 * on what elements may be added.
	 *
	 * @param o element to be appended to this list.
	 */
	@SuppressWarnings( "unchecked" )
	public void addElement (Object o) {
		add( (T)o );
	}

	/**
	 * Appends the specified element to the end of this list (optional
	 * operation). <p>
	 *
	 * Lists that support this operation may place limitations on what
	 * elements may be added to this list.  In particular, some
	 * lists will refuse to add null elements, and others will impose
	 * restrictions on the type of elements that may be added.  List
	 * classes should clearly specify in their documentation any restrictions
	 * on what elements may be added.
	 *
	 * @param o element to be appended to this list.
	 * @return <tt>true</tt> (as per the general contract of the
	 *            <tt>Collection.add</tt> method).
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
	public boolean add (T o) {
		boolean added = data.add( o );
		if (added)
			notifyDataSetChanged();
		return added;
	}

	/**
	 * Inserts the specified element at the specified position in this list
	 * (optional operation).  Shifts the element currently at that position
	 * (if any) and any subsequent elements to the right (adds one to their
	 * indices).
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
	public void add(int index, T element) {
		data.add(index, element);
		notifyDataSetChanged();
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
	public T remove (int index) {
		T t = data.remove( index );
		if (index >= 0)
			notifyDataSetChanged();
		return t;
	}

	/**
	 * Removes the first occurrence in this list of the specified element
	 * (optional operation).  If this list does not contain the element, it is
	 * unchanged.  More formally, removes the element with the lowest index i
	 * such that <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if
	 * such an element exists).
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
		int index = indexOf( o );
		return index >= 0 && remove( index ) != null;

	}

	/**
	 * Removes the first occurrence in this list of the specified element
	 * (optional operation).  If this list does not contain the element, it is
	 * unchanged.  More formally, removes the element with the lowest index i
	 * such that <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if
	 * such an element exists).
	 *
	 * @param obj element to be removed from this list, if present.
	 */
	public void removeElement (Object obj) {
		remove( obj );
	}

	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p/>
	 * @param anObject selection object.
	 */
	public void setSelection (T anObject) {
		if ((selection != null && !selection.equals( anObject )) ||
				selection == null && anObject != null)
		{
			selection = anObject;
			notifyDataSetChanged();
		}
	}

	/**
	 * Set the value of the selected item. The selected item may be null.
	 * @param index selection index.
	 */
	public void setSelection (int index) {
		setSelection( data.get( index ));
	}

	/**
	 * Get the selected item.
	 * @return selected item of type <T> or <code>null</code> if nothing selected.
	 */
	public T getSelection () {
		return selection;
	}

	// Bulk Modification Operations

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
	 * @throws NullPointerException if the specified collection contains one
	 *         or more null elements and this list does not support null
	 *         elements (optional). or if the specified collection is
	 *         <tt>null</tt>.
	 * @see #contains(Object)
	 */
	public boolean containsAll (Collection<?> c) {
		return data.containsAll( c );
	}

	/**
	 * Appends all of the elements in the specified collection to the end of
	 * this list, in the order that they are returned by the specified
	 * collection's iterator (optional operation).  The behavior of this
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
	 */
	public boolean addAll (Collection<? extends T> c) {
		boolean rv = data.addAll( c );
		notifyDataSetChanged();
		return rv;
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
	public boolean addAll (int index, Collection<? extends T> c) {
		boolean rv = data.addAll( index, c );
		notifyDataSetChanged();
		return rv;
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
	 * @throws NullPointerException if this list contains one or more
	 *            null elements and the specified collection does not support
	 *            null elements (optional). or if the specified collection is <tt>null</tt>.
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	public boolean removeAll (Collection<?> c) {
		boolean rv = data.removeAll( c );
		notifyDataSetChanged();
		return rv;
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
	 * @throws NullPointerException if this list contains one or more
	 *            null elements and the specified collection does not support
	 *            null elements (optional). or if the specified collection is <tt>null</tt>.
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	public boolean retainAll(Collection<?> c) {
		boolean rv = data.retainAll( c );
		notifyDataSetChanged();
		return rv;
	}

	/**
	 * Removes all of the elements from this list (optional operation).  This
	 * list will be empty after this call returns (unless it throws an
	 * exception).
	 *
	 * @throws UnsupportedOperationException if the <tt>clear</tt> method is
	 * 		  not supported by this list.
	 */
	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of element to return.
	 * @return the element at the specified position in this list.
	 * @throws IndexOutOfBoundsException if the index is out of range (index
	 *                                   &lt; 0 || index &gt;= size()).
	 */
	public T get (int index) {
		return data.get( index );
	}

	/**
	 * Sets the component at the specified <code>index</code> of this
	 * list to be the specified object. The previous component at that
	 * position is discarded.
	 * <p/>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is invalid.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>set(int,Object)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      index   the specified index
	 * @param      obj     what the component is to be set to
	 */
	public T set (int index, T obj) {
		T t = data.set(index, obj);
		notifyDataSetChanged();
		return t;
	}

	// Search Operations

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
	public int indexOf (Object o) {
		return data.indexOf( o );
	}

	/**
	 * Returns the index in this list of the last occurrence of the specified
	 * element, or -1 if this list does not contain this element.
	 * More formally, returns the highest index <tt>i</tt> such that
	 * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for.
	 * @return the index in this list of the last occurrence of the specified
	 * 	       element, or -1 if this list does not contain this element.
	 * @throws ClassCastException if the type of the specified element
	 * 	       is incompatible with this list (optional).
	 * @throws NullPointerException if the specified element is null and this
	 *         list does not support null elements (optional).
	 */
	public int lastIndexOf(Object o) {
		return data.lastIndexOf( o );
	}


	// List Iterators

	/**
	 * Returns a list iterator of the elements in this list (in proper
	 * sequence).
	 *
	 * @return a list iterator of the elements in this list (in proper
	 * 	       sequence).
	 */
	public ListIterator<T> listIterator () {
		return data.listIterator();
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
	public ListIterator<T> listIterator (int index) {
		return data.listIterator( index );
	}

	// View

	/**
	 * Returns a view of the portion of this list between the specified
	 * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.  (If
	 * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
	 * empty.)  The returned list is backed by this list, so non-structural
	 * changes in the returned list are reflected in this list, and vice-versa.
	 * The returned list supports all of the optional list operations supported
	 * by this list.<p>
	 *
	 * This method eliminates the need for explicit range operations (of
	 * the sort that commonly exist for arrays).   Any operation that expects
	 * a list can be used as a range operation by passing a subList view
	 * instead of a whole list.  For example, the following idiom
	 * removes a range of elements from a list:
	 * <pre>
	 *	    list.subList(from, to).clear();
	 * </pre>
	 * Similar idioms may be constructed for <tt>indexOf</tt> and
	 * <tt>lastIndexOf</tt>, and all of the algorithms in the
	 * <tt>Collections</tt> class can be applied to a subList.<p>
	 *
	 * The semantics of the list returned by this method become undefined if
	 * the backing list (i.t., this list) is <i>structurally modified</i> in
	 * any way other than via the returned list.  (Structural modifications are
	 * those that change the size of this list, or otherwise perturb it in such
	 * a fashion that iterations in progress may yield incorrect results.)
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList.
	 * @param toIndex high endpoint (exclusive) of the subList.
	 * @return a view of the specified range within this list.
	 *
	 * @throws IndexOutOfBoundsException for an illegal endpoint index value
	 *     (fromIndex &lt; 0 || toIndex &gt; size || fromIndex &gt; toIndex).
	 */
	public List<T> subList(int fromIndex, int toIndex) {
		return data.subList(fromIndex, toIndex);
	}

	
}

