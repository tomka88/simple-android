package org.simple.clinic.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import org.simple.clinic.R
import org.simple.clinic.home.overdue.OverdueScreenKey
import org.simple.clinic.home.patients.PatientsTabScreenKey
import org.simple.clinic.home.report.ReportsScreenKey

class HomePagerAdapter(private val context: Context) : RecyclerView.Adapter<HomePagerAdapter.TabViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    return TabViewHolder(view)
  }

  override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
  }

  fun getPageTitle(position: Int): CharSequence = context.resources.getString(HomeTab.values()[position].title)

  override fun getItemViewType(position: Int): Int {
    return when (position) {
      0 -> HomeTab.PATIENTS.key
      1 -> HomeTab.OVERDUE.key
      2 -> HomeTab.REPORTS.key
      else -> throw IllegalArgumentException()
    }
  }

  override fun getItemCount() = HomeTab.values().size

  inner class TabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

enum class HomeTab(@LayoutRes val key: Int, @StringRes val title: Int) {

  PATIENTS(PatientsTabScreenKey().layoutRes(), R.string.tab_patient),

  OVERDUE(OverdueScreenKey().layoutRes(), R.string.tab_overdue),

  REPORTS(ReportsScreenKey().layoutRes(), R.string.tab_progress)
}
