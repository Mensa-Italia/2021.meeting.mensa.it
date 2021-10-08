package swaix.dev.mensaeventi.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.model.ResponseGetEventDetails
import swaix.dev.mensaeventi.ui.events.EventCalendarSlide
import swaix.dev.mensaeventi.utils.shortDayString


class CalendarFragmentAdapter(fragment: Fragment, val value: ResponseGetEventDetails) : FragmentStateAdapter(fragment) {

    private val days: Map<String, List<EventItemWithDate>> = value.eventActivities
        .sortedBy {
            it.dateFrom
        }
        .groupBy {
            it.dateFrom.shortDayString()
        }


    override fun createFragment(position: Int): Fragment {
        val day = days.keys.toTypedArray()[position]
        return EventCalendarSlide.newInstance(days[day] ?: listOf())
    }

    override fun getItemCount(): Int {
        return days.size
    }

}