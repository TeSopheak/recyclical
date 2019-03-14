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
@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.afollestad.recyclical.handle

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.afollestad.recyclical.ItemDefinition
import com.afollestad.recyclical.datasource.DataSource

typealias AdapterBlock = Adapter<*>.() -> Unit

/**
 * Represents a handle to Recyclical as it is setup and manipulating
 * a RecyclerView. Provides utility functions to be used by [DataSource]'s.
 *
 * @author Aidan Follestad (@afollestad)
 */
interface RecyclicalHandle {
  /** Shows the empty view if [show] is true, else hides it. */
  fun showOrHideEmptyView(show: Boolean)

  /** Gets the underlying adapter for the RecyclerView. */
  fun getAdapter(): RecyclerView.Adapter<*>

  /**
   * Executes code in the given [block] on the current adapter,
   * then invalidates whether the empty view is visible or not
   * based on [DataSource.isEmpty].
   */
  fun invalidateList(block: AdapterBlock)

  /** Retrieves a view type for a item class. */
  fun getViewTypeForClass(name: String): Int

  /* Retrieves an [ItemDefinition] for a item class. */
  fun getDefinitionForClass(name: String): ItemDefinition<*>

  /** Retrieves an [ItemDefinition] for a view type. */
  fun getDefinitionForType(type: Int): ItemDefinition<*>
}

/** Gets the current data source, auto casting it to [T]. */
inline fun <reified T : DataSource> RecyclicalHandle.getDataSource(): T {
  return if (this is RealRecyclicalHandle) {
    dataSource as T
  } else {
    throw IllegalStateException("Handle is not a real implementation.")
  }
}
