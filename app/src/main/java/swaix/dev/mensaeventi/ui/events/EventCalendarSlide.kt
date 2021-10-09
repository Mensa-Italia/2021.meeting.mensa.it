package swaix.dev.mensaeventi.ui.events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.ItemTimelineActivityBinding
import swaix.dev.mensaeventi.databinding.LayoutTimelineCalendarBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import swaix.dev.mensaeventi.utils.*
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class EventCalendarSlide : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutTimelineCalendarBinding.inflate(inflater, container, false).root
    }

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {

            LayoutTimelineCalendarBinding.bind(view).bindView((it.getSerializable("list") as List<*>).filterIsInstance(EventItemWithDate::class.java))
        }

    }


    @ExperimentalTime
    fun daysDiff(c1: Calendar, c2: Calendar): Long {
        val diffInMillis = c1.timeInMillis - c2.timeInMillis
        return Duration.hours(diffInMillis).inWholeHours
    }

    @ExperimentalTime
    private fun LayoutTimelineCalendarBinding.bindView(data: List<EventItemWithDate>) {

        val minDate = data.map {
            it.dateFrom
        }.minByOrNull {
            it
        }

        val maxDate = data.map {
            it.dateTo
        }.maxByOrNull {
            it
        }

        if (minDate != null && maxDate != null) {
            val c1 = Calendar.getInstance()
            val c2 = Calendar.getInstance()

            c1.time = minDate
            c2.time = maxDate

            val diff = daysDiff(c1, c2)


            val minHour = c1.get(Calendar.HOUR_OF_DAY)
            val maxHour = if (c1.get(Calendar.DAY_OF_MONTH) < c2.get(Calendar.DAY_OF_MONTH)) c2.get(Calendar.HOUR_OF_DAY) + 24 else c2.get(Calendar.HOUR_OF_DAY)

            val startSet = ConstraintSet()
            val openSet = ConstraintSet()
            startSet.clone(root)

            val listOfGridViewsId = mutableListOf<Int>()


            (0..24).forEach {
                val format = "%02d".format(it)
                root.findViewById<View>(requireContext().getIdResByName("t$format"))?.let { view ->
                    if (it < minHour || it > maxHour)
                        startSet.setVisibility(view.id, View.GONE)
                    else
                        listOfGridViewsId.add(view.id)
                }
            }

            calendarFlow.referencedIds = listOfGridViewsId.toIntArray()


            Log.d(TAG, "$diff ")

            val resultList = mutableListOf<Pair<EventItemWithDate, View>>()

            data.forEachIndexed { index, eventItemWithDate ->
                val element = LayoutInflater.from(requireContext()).inflate(R.layout.item_timeline_activity, root, false).apply {
                    id = ConstraintLayout.generateViewId()
                }

                with(ItemTimelineActivityBinding.bind(element)) {
                    activityName.text = eventItemWithDate.name.asHtml()
                    activityDescription.text = eventItemWithDate.description.asHtml()

                    activityDate.text = resources.getString(R.string.date_time_label, eventItemWithDate.dateFrom.dayString(), eventItemWithDate.dateFrom.hourMinuteString() , eventItemWithDate.dateTo.hourMinuteString())

                    activityDetailButton.setOnClickListener {
                        findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToEventDetailExtraFragment(eventItemWithDate))
                    }
                    activityDirectionButton.setOnClickListener {
                        val gmmIntentUri = Uri.parse("google.navigation:q=${eventItemWithDate.position.latitude},${eventItemWithDate.position.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)
                    }
                }

                element.setOnClickListener {
                    openActivityDetail(root, it, openSet, startSet)
                }

                root.addView(element)

                if (index == 0)
                    resultList.add(eventItemWithDate to element)
                else {
                    // verifico l'attuale massimo numero di colonne già processate
                    val maxColumn = resultList.maxOf { it.first.columnOnCalendar }
                    var i = 0
                    var stop = false


                    while (i <= maxColumn && !stop) {
                        // prendo tutte gli oggetti sulla colonna iesima
                        val itemOnColumn = resultList.filter { it.first.columnOnCalendar == i }
                        // cerco l'ultimo oggetto sulla colonna con data di fine PRECEDENTE alla data di inizio del mio attuale elemento

                        val lastElementOnColumn = itemOnColumn.last()
                        if (lastElementOnColumn.first.dateTo.before(eventItemWithDate.dateFrom) || lastElementOnColumn.first.dateTo == eventItemWithDate.dateFrom) {
                            // se trovo uno slot sulla colonna inserisco ed esco
                            resultList.add(eventItemWithDate.apply {
                                columnOnCalendar = lastElementOnColumn.first.columnOnCalendar
                            } to element)
                            stop = true
                        }
                        i++
                    }

                    if (!stop) {
                        eventItemWithDate.columnOnCalendar = maxColumn + 1
                        resultList.add(eventItemWithDate to element)
                    }
                }
            }


            val maxColumn = resultList.maxOf { it.first.columnOnCalendar }

            resultList.forEachIndexed { index, pair ->
                val item = pair.first
                val element = pair.second
//
//                val itemsOnColumn = resultList.filter { p ->
//                    p.first.columnOnCalendar == item.columnOnCalendar
//                }

                val topString = "%02d".format(item.dateFrom.hour())
                val botString = "%02d".format(item.dateTo.hour())

                val idTop = root.findViewById<View>(requireContext().getIdResByName("t$topString")).id
                val idBot = root.findViewById<View>(requireContext().getIdResByName("t$botString")).id

                val topBias = item.dateFrom.minutes().toFloat() / 60f
                val topAnchor = View(requireContext()).apply {
                    id = ConstraintLayout.generateViewId()
                    root.addView(this)
                }.id

                val botBias = item.dateTo.minutes().toFloat() / 60f
                val bottomAnchor = View(requireContext()).apply {
                    id = ConstraintLayout.generateViewId()
                    root.addView(this)
                }.id

                startSet.constrainHeight(topAnchor, 1)
                startSet.constrainHeight(bottomAnchor, 1)

                startSet.connect(topAnchor, ConstraintSet.TOP, idTop, ConstraintSet.TOP)
                startSet.connect(topAnchor, ConstraintSet.BOTTOM, idTop, ConstraintSet.BOTTOM)
                startSet.connect(topAnchor, ConstraintSet.START, idTop, ConstraintSet.START)
                startSet.connect(topAnchor, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                startSet.setVerticalBias(topAnchor, topBias)

                startSet.connect(bottomAnchor, ConstraintSet.TOP, idBot, ConstraintSet.TOP)
                startSet.connect(bottomAnchor, ConstraintSet.BOTTOM, idBot, ConstraintSet.BOTTOM)
                startSet.connect(bottomAnchor, ConstraintSet.START, idBot, ConstraintSet.START)
                startSet.connect(bottomAnchor, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                startSet.setVerticalBias(bottomAnchor, botBias)


                val (startId, startSide) =
                    if (item.columnOnCalendar == 0) {
                        calendarFlow.id to ConstraintSet.END
                    } else {
                        val previousColumn = resultList.find {
                            it.first.columnOnCalendar == item.columnOnCalendar - 1
                        }
                        (previousColumn?.second?.id ?: -1) to ConstraintSet.END
                    }

                val (endId, endSide) = if (item.columnOnCalendar == maxColumn) {
                    ConstraintSet.PARENT_ID to ConstraintSet.END
                } else {
                    val nextColumn = resultList.find {
                        it.first.columnOnCalendar == item.columnOnCalendar + 1
                    }
                    (nextColumn?.second?.id ?: -1) to ConstraintSet.START
                }

                startSet.setElevation(element.id, 0f)
                startSet.clear(element.id, ConstraintSet.TOP)
                startSet.clear(element.id, ConstraintSet.BOTTOM)
                startSet.clear(element.id, ConstraintSet.START)
                startSet.clear(element.id, ConstraintSet.END)
                startSet.connect(element.id, ConstraintSet.TOP, topAnchor, ConstraintSet.TOP)
                startSet.connect(element.id, ConstraintSet.BOTTOM, bottomAnchor, ConstraintSet.TOP)
                startSet.connect(element.id, ConstraintSet.START, startId, startSide)
                startSet.connect(element.id, ConstraintSet.END, endId, endSide)
//
            }
            TransitionManager.beginDelayedTransition(root)
            startSet.applyTo(root)
        }


    }

    private fun openActivityDetail(root: ConstraintLayout, view: View?, openSet: ConstraintSet, startSet: ConstraintSet) {
        openSet.clone(root)
        view?.let { element ->

            openSet.clear(element.id, ConstraintSet.TOP)
            openSet.clear(element.id, ConstraintSet.BOTTOM)
            openSet.clear(element.id, ConstraintSet.START)
            openSet.clear(element.id, ConstraintSet.END)
            openSet.connect(element.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            openSet.connect(element.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            openSet.connect(element.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            openSet.connect(element.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            openSet.setElevation(element.id, 1f)
            TransitionManager.beginDelayedTransition(root)
            openSet.applyTo(root)

            (element as MotionLayout).transitionToEnd()


            element.setOnClickListener {
                TransitionManager.beginDelayedTransition(root)
                startSet.applyTo(root)
                element.transitionToStart()
                it.setOnClickListener {
                    openActivityDetail(root, element, openSet, startSet)

                }
            }
        }

    }

    companion object {
        fun newInstance(data: List<EventItemWithDate>): EventCalendarSlide {
            val args = bundleOf("list" to data)

            val fragment = EventCalendarSlide()
            fragment.arguments = args
            return fragment
        }
    }

}