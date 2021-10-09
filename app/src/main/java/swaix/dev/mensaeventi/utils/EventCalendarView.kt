package swaix.dev.mensaeventi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.LayoutTimelineCalendarBinding
import swaix.dev.mensaeventi.model.EventItemWithDate
import java.util.*
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@SuppressLint("ClickableViewAccessibility")
class EventCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    override fun isInEditMode(): Boolean = true

    private val startSet: ConstraintSet = ConstraintSet()
    val endSet: ConstraintSet = ConstraintSet()

//   private var binding: LayoutTimelineCalendarBinding = LayoutTimelineCalendarBinding.inflate(LayoutInflater.from(context), this, true)

    @ExperimentalTime
    var data: List<EventItemWithDate> by Delegates.observable(mutableListOf()) { _, _, _ ->
        startCalc()
    }

    init {
      LayoutTimelineCalendarBinding.inflate(LayoutInflater.from(context), this, true)

    }

    @ExperimentalTime
    fun daysDiff(c1: Calendar, c2: Calendar): Long {
        val diffInMillis = c1.timeInMillis - c2.timeInMillis
        return Duration.hours(diffInMillis).inWholeHours
    }

    @ExperimentalTime
    private fun startCalc() {
        val dayMap = data
            .sortedBy {
                it.dateFrom
            }
            .groupBy {
                it.dateFrom.shortDayString()
            }

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

            (0 until minHour).forEach {
                val format = "%02d".format(it)
                findViewById<View>(context.getIdResByName("t$format"))?.visibility = View.GONE
            }

            (maxHour + 1..24).forEach {
                val format = "%02d".format(it)
                findViewById<View>(context.getIdResByName("t$format"))?.visibility = View.GONE
            }

            Log.d(TAG, "$diff ")


//            val constraints = ConstraintSet()
//            constraints.clone(this)
            data.forEach {

                val element = TextView(context).apply {
                    setBackgroundColor(Int.randomColor())
                    layoutParams = LayoutParams(0, 0)
                    id = generateViewId()
                    text = it.name
                }
                addView(element)
////
//                val guideline = Guideline(context).apply {
//                    layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
//                        orientation = LayoutParams.VERTICAL
//                        guidePercent = 0.2f
//                    }
//                    id = generateViewId()
//                }
//                addView(guideline)
                val topString = "%02d".format(it.dateFrom.hour())
                val botString = "%02d".format(it.dateTo.hour())
//                val idTop = findViewById<View>(context.getIdResByName("t$topString")).id
//                val idBot = findViewById<View>(context.getIdResByName("t$botString")).id
                val timeLimitId = findViewById<View>(R.id.time_limit).id

//                constraints.constrainDefaultHeight(element.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
//                constraints.constrainDefaultWidth(element.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)

                startSet.clear(element.id, ConstraintSet.TOP)
                startSet.clear(element.id, ConstraintSet.BOTTOM)
                startSet.clear(element.id, ConstraintSet.START)
                startSet.clear(element.id, ConstraintSet.END)
                startSet.connect(element.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                startSet.connect(element.id, ConstraintSet.BOTTOM, timeLimitId , ConstraintSet.BOTTOM)
                startSet.connect(element.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                startSet.connect(element.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)


            }
//            val element = View(context).apply {
//                    setBackgroundColor(Int.randomColor())
//                    layoutParams = LayoutParams(100,100)
//                    id = generateViewId()
//                }
//            addView(element)
//
//            //apply the default width and height constraints in code
////            constraints.constrainDefaultHeight(element.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
////            constraints.constrainDefaultWidth(element.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
//
//            val id = findViewById<View>(R.id.t01).id
//            constraints.connect(element.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
//            constraints.connect(element.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
//            constraints.connect(element.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
//            constraints.connect(element.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            startSet.clone(this)
            startSet.applyTo(this)

        }

    }


//    fun setFlow(){
//        dayMap.onEachIndexed { row, entry ->
//            val header = TextView(context).apply {
//                text = "${entry.key}"
//                layoutParams = LayoutParams(100, 100)
//                setBackgroundColor(Int.randomColor())
//                setOnClickListener {
//                    Toast.makeText(context, "$entry.key", Toast.LENGTH_SHORT).show()
//                }
//                id = generateViewId()
//                Log.d(TAG, "addView: $id")
//            }
//            listOfGridViews.add(
//                header
//            )
//            addView(header)
//
//            (0..diff).forEach { col ->
//                val element = View(context).apply {
//                    tag = "$row-$col"
//                    layoutParams = LayoutParams(0, 100)
//                    setBackgroundColor(Int.randomColor())
//                    setOnClickListener {
//                        Toast.makeText(context, "$row-$col", Toast.LENGTH_SHORT).show()
//                    }
//                    id = generateViewId()
//                    Log.d(TAG, "addView: $id")
//                }
//                listOfGridViews.add(
//                    element
//                )
//                addView(element)
//            }
//
//        }
//
//        listOfGridViews.forEach {
//            Log.d(TAG, "*!*!*!*!*!* ${it.tag}")
//        }
//
//        val flow = Flow(context).apply {
//            id = generateViewId()
//            setWrapMode(Flow.WRAP_CHAIN)
//            setHorizontalStyle(Flow.CHAIN_PACKED)
//            setHorizontalAlign(Flow.HORIZONTAL_ALIGN_START)
//            setHorizontalBias(0f)
//            setOrientation(Flow.HORIZONTAL)
//            setMaxElementsWrap(diff+2)
//            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
//        }
//
//        flow.referencedIds = listOfGridViews.map {
//            it.id
//        }.toIntArray()
//
//        val constraintSet = ConstraintSet().apply {
//            clone(this)
//            clear(flow.id, ConstraintSet.END)
//            clear(flow.id, ConstraintSet.START)
//            clear(flow.id, ConstraintSet.BOTTOM)
//            connect(flow.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
//            connect(flow.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
//            connect(flow.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
//        }
//
//        constraintSet.applyTo(this)
//        addView(flow)
//    }

}