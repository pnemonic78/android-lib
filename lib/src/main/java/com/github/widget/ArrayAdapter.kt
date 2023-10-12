/*
 * Copyright 2012, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.widget

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.widget.ArrayAdapter.ArrayViewHolder
import timber.log.Timber
import java.util.Collections
import java.util.Locale

/**
 * Array adapter ported from [android.widget.ArrayAdapter] to [RecyclerView].
 *
 * @author Moshe Waisberg
 */
open class ArrayAdapter<T, VH : ArrayViewHolder<T>> @JvmOverloads constructor(
    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    @LayoutRes private val layoutResource: Int,
    @IdRes textViewResourceId: Int = 0,
    objects: List<T?> = emptyList()
) : RecyclerView.Adapter<VH>(), Filterable {
    /**
     * Lock used to modify the content of [.objects]. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see [.getFilter] to make a synchronized copy of
     * the original array of data.
     */
    @JvmField
    protected val lock = Any()

    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    @JvmField
    protected val objects: MutableList<T?> = mutableListOf()

    /**
     * If the inflated resource is not a TextView, `textFieldId` is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    @IdRes
    private var textFieldId = 0

    /**
     * Indicates whether or not [.notifyDataSetChanged] must be called whenever
     * [.objects] is modified.
     */
    private var notifyOnChange = true

    /**
     * A copy of the original objects array, initialized from and then used instead as soon as
     * the filter is used. objects will then only contain the filtered values.
     */
    @JvmField
    protected val originalValues: MutableList<T?> = mutableListOf()
    @JvmField
    protected var objectsFiltered = false

    private var filter: ArrayFilter? = null

    /**
     * Constructor
     *
     * @param resource The resource ID for a layout file containing a TextView to use when
     * instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    constructor(@LayoutRes resource: Int, objects: Array<T>) : this(resource, 0, listOf<T>(*objects))

    /**
     * Constructor
     *
     * @param resource           The resource ID for a layout file containing a layout to use when
     * instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects            The objects to represent in the ListView.
     */
    constructor(@LayoutRes resource: Int, @IdRes textViewResourceId: Int, objects: Array<T>) : this(resource, textViewResourceId, listOf<T>(*objects))

    /**
     * Constructor
     *
     * @param resource The resource ID for a layout file containing a TextView to use when
     * instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    constructor(@LayoutRes resource: Int, objects: List<T>) : this(resource, 0, objects)

    init {
        this.objects.addAll(objects)
        this.textFieldId = textViewResourceId
        setHasStableIds(true)
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    fun add(`object`: T?) {
        var position: Int
        synchronized(lock) {
            if (objectsFiltered) {
                position = originalValues.size
                originalValues.add(`object`)
            } else {
                position = objects.size
                objects.add(`object`)
            }
        }
        if (notifyOnChange) notifyItemInserted(position)
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     * is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     * collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     * or more null elements and this list does not permit null
     * elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     * specified collection prevents it from being added to this list
     */
    fun addAll(collection: Collection<T?>) {
        var position: Int
        val count = collection.size
        synchronized(lock) {
            if (objectsFiltered) {
                position = originalValues.size
                originalValues.addAll(collection)
            } else {
                position = objects.size
                objects.addAll(collection)
            }
        }
        if (notifyOnChange) notifyItemRangeInserted(position, count)
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    fun addAll(vararg items: T?) {
        var position: Int
        val count = items.size
        synchronized(lock) {
            if (objectsFiltered) {
                position = originalValues.size
                Collections.addAll<T?>(originalValues, *items)
            } else {
                position = objects.size
                Collections.addAll<T?>(objects, *items)
            }
        }
        if (notifyOnChange) notifyItemRangeInserted(position, count)
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    fun insert(`object`: T?, index: Int) {
        synchronized(lock) {
            if (objectsFiltered) {
                originalValues.add(index, `object`)
            } else {
                objects.add(index, `object`)
            }
        }
        if (notifyOnChange) notifyItemInserted(index)
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    fun remove(`object`: T?) {
        var position: Int
        synchronized(lock) {
            if (objectsFiltered) {
                position = originalValues.indexOf(`object`)
                originalValues.removeAt(position)
            } else {
                position = objects.indexOf(`object`)
                objects.removeAt(position)
            }
        }
        if (notifyOnChange) notifyItemRemoved(position)
    }

    /**
     * Remove all elements from the list.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        synchronized(lock) {
            if (objectsFiltered) {
                originalValues.clear()
            } else {
                objects.clear()
            }
        }
        if (notifyOnChange) notifyDataSetChanged()
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained in this adapter.
     */
    open fun sort(comparator: Comparator<in T?>) {
        var count: Int
        synchronized(lock) {
            if (objectsFiltered) {
                count = originalValues.size
                Collections.sort(originalValues, comparator)
            } else {
                count = objects.size
                Collections.sort(objects, comparator)
            }
        }
        if (notifyOnChange) notifyItemRangeChanged(0, count)
    }

    /**
     * Control whether methods that change the list ([.add], [.addAll],
     * [.addAll], [.insert], [.remove], [.clear],
     * [.sort]) automatically call [.notifyDataSetChanged].  If set to
     * false, caller must manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged() resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will automatically call [.notifyDataSetChanged]
     */
    fun setNotifyOnChange(notifyOnChange: Boolean) {
        this.notifyOnChange = notifyOnChange
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    fun getItem(position: Int): T? {
        return objects[position]
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    fun getPosition(item: T?): Int {
        return objects.indexOf(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(layoutResource, parent, false)
        return createArrayViewHolder(view, textFieldId)
    }

    protected open fun createArrayViewHolder(view: View, fieldId: Int): VH {
        return ArrayViewHolder<T>(view, fieldId) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getFilter(): Filter {
        var filter = filter
        if (filter == null) {
            filter = createFilter()
            this.filter = filter
        }
        return filter
    }

    protected open fun createFilter(): ArrayFilter {
        return ArrayFilter()
    }

    open class ArrayViewHolder<T>(itemView: View, fieldId: Int) : ViewHolder(itemView) {
        @JvmField
        protected val textView: TextView

        init {
            textView = try {
                (if (fieldId == 0) itemView else itemView.findViewById(fieldId)) as TextView
            } catch (e: ClassCastException) {
                Timber.e(e, "You must supply a resource ID for a TextView")
                throw IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e
                )
            }
        }

        open fun bind(item: T?) {
            if (item is CharSequence) {
                textView.text = item
            } else {
                textView.text = item.toString()
            }
        }
    }

    /**
     * An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.
     */
    protected open inner class ArrayFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()

            if (!objectsFiltered) {
                synchronized(lock) {
                    objectsFiltered = true
                    originalValues.clear()
                    originalValues.addAll(objects)
                }
            }

            if (prefix.isNullOrEmpty()) {
                val list: List<T?>
                synchronized(lock) { list = ArrayList(originalValues) }
                results.values = list
                results.count = list.size
            } else {
                val locale = Locale.getDefault()
                val prefixString = prefix.toString().lowercase(locale)
                val values: List<T?>
                synchronized(lock) { values = ArrayList(originalValues) }
                val count = values.size
                val newValues = ArrayList<T?>()

                for (i in 0 until count) {
                    val value = values[i]
                    val valueText = value.toString().lowercase(locale)

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value)
                    } else {
                        val words = valueText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (word in words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value)
                                break
                            }
                        }
                    }
                }
                results.values = newValues
                results.count = newValues.size
            }
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            synchronized(lock) {
                objects.clear()
                if (results.count > 0) {
                    objects.addAll((results.values as Collection<T?>))
                }
                notifyDataSetChanged()
                notifyOnChange = true
            }
        }
    }

    /**
     * Permanently removes the specified object from the array.
     *
     * @param object The object to delete.
     */
    fun delete(`object`: T?) {
        var position: Int
        synchronized(lock) {
            position = originalValues.indexOf(`object`)
            originalValues.removeAt(position)
            position = objects.indexOf(`object`)
            if (position >= 0) {
                objects.removeAt(position)
            }
        }
        if (notifyOnChange) notifyItemRemoved(position)
    }
}