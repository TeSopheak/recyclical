/**
 * Designed and developed by Aidan Follestad (@afollestad)
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
@file:Suppress("UNCHECKED_CAST")

package com.afollestad.recyclical.internal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.afollestad.recyclical.ItemDefinition
import com.afollestad.recyclical.bindViewHolder
import com.afollestad.recyclical.createViewHolder
import com.afollestad.recyclical.datasource.DataSource
import com.afollestad.recyclical.handle.RecyclicalHandle
import com.afollestad.recyclical.handle.getDataSource

/** @author Aidan Follestad (@afollestad) */
internal open class DefinitionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var handle: RecyclicalHandle? = null
  private var dataSource: DataSource? = null

  /** Attaches the adapter to a handle which provides a data source, etc. */
  open fun attach(handle: RecyclicalHandle) {
    this.handle = handle.also {
      this.dataSource = it.getDataSource()
    }
  }

  /** Clears references to avoid memory leaks. */
  open fun detach() {
    this.dataSource = null
    this.handle = null
  }

  override fun getItemViewType(position: Int): Int {
    return dataSource?.get(position)?.getItemType() ?: throw IllegalStateException(
        "No data source available."
    )
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(viewType, parent, false)
    return viewType.getItemDefinition()
        .createViewHolder(view)
  }

  override fun getItemCount() = dataSource?.size() ?: 0

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    val item = dataSource!![position]
    val viewType = item.getItemType()
    viewType.getItemDefinition()
        .bindViewHolder(holder, item, position)
  }

  private fun Any.getItemType(): Int {
    val itemClassName = this::class.java.name
    return handle?.getViewTypeForClass(itemClassName) ?: throw IllegalStateException(
        "Not attached!"
    )
  }

  private fun Int.getItemDefinition(): ItemDefinition<*> {
    return handle?.getDefinitionForType(this) ?: throw IllegalStateException(
        "Not attached!"
    )
  }
}
