package com.ewingsa.ohyeah.wheelcomponent

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WheelComponent(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var items: MutableList<CharSequence> = mutableListOf(DEFAULT_ITEM)
    var listener: SelectedItemListener? = null
    var position: Int = 0
        set(value) {
            field = value
            onPositionSet()
        }

    @ColorInt
    private var selectedItemColor: Int

    @ColorInt
    private var closeItemColor: Int

    @ColorInt
    private var normalItemColor: Int
    private var selectedItemTextSize: Float
    private var closeItemTextSize: Float
    private var normalItemTextSize: Float

    private var centerYOfWheel = -1
    private var centerOfMiddleComponent = -1
    private var preSnapDistanceToCenter = -1

    init {
        context.obtainStyledAttributes(attrs, R.styleable.WheelComponent).run {
            items = getTextArray(R.styleable.WheelComponent_android_entries).toMutableList()
            selectedItemColor = getColor(R.styleable.WheelComponent_android_textColor, ContextCompat.getColor(context, R.color.wheel_selected_item))
            closeItemColor = getColor(R.styleable.WheelComponent_closeItemColor, ContextCompat.getColor(context, R.color.wheel_close_item))
            normalItemColor = getColor(R.styleable.WheelComponent_normalItemColor, ContextCompat.getColor(context, R.color.wheel_normal_item))
            selectedItemTextSize = getDimensionPixelSize(R.styleable.WheelComponent_android_textSize, DEFAULT_TEXT_SIZE).toFloat()
            recycle()
        }
        while (items.size < MINIMUM_ITEMS) {
            items.addAll(items)
        }
        closeItemTextSize = selectedItemTextSize + TEXT_SIZE_OFFSET_CLOSE_ITEM
        normalItemTextSize = selectedItemTextSize + TEXT_SIZE_OFFSET_NORMAL_ITEM

        inflate(ContextThemeWrapper(context, R.style.wheel_item), R.layout.wheel_component, this)

        findViewById<RecyclerView>(R.id.wheel_component_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = WheelAdapter(items, normalItemTextSize)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    onScrolled(recyclerView)
                }
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    calculateSnap(recyclerView, newState)
                }
            })
        }
    }

    private fun onPositionSet() {
        findViewById<RecyclerView>(R.id.wheel_component_recycler_view)?.layoutManager?.scrollToPosition(position)
    }

    private fun onScrolled(wheel: RecyclerView) {
        val layoutManager = wheel.layoutManager as LinearLayoutManager
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition() - 1
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition() + 1

        val scrollingToFirstPosition = centerYOfWheel == -1

        val wheelCoordinates = Rect()
        wheel.getGlobalVisibleRect(wheelCoordinates)
        centerYOfWheel = (wheelCoordinates.top + wheelCoordinates.bottom) / 2

        for (itemPosition in firstVisiblePosition until lastVisiblePosition) {
            val itemViewHolderPosition = getItemViewHolderPosition(itemPosition, wheel.childCount)

            wheel.getChildAt(itemViewHolderPosition)?.let {
                val wheelItem = (wheel.getChildViewHolder(it).itemView as? TextView)

                val wheelItemCoordinates = Rect()
                wheelItem?.getGlobalVisibleRect(wheelItemCoordinates)

                val wheelItemBottom = wheelItemCoordinates.bottom
                val wheelItemTop = wheelItemCoordinates.top
                val wheelItemHeight = wheelItemBottom - wheelItemTop

                if (centerYOfWheel in wheelItemTop until wheelItemBottom) {
                    listener?.onItemSelected(wheelItem?.text.toString())
                    wheelItem?.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedItemTextSize)
                    wheelItem?.setTextColor(selectedItemColor)

                    centerOfMiddleComponent = (wheelItemTop + wheelItemBottom) / 2
                } else if (wheelItemBottom < centerYOfWheel && wheelItemBottom + wheelItemHeight > centerYOfWheel ||
                    wheelItemTop > centerYOfWheel && wheelItemTop - wheelItemHeight < centerYOfWheel
                ) {
                    wheelItem?.setTextSize(TypedValue.COMPLEX_UNIT_PX, closeItemTextSize)
                    wheelItem?.setTextColor(closeItemColor)
                } else {
                    wheelItem?.setTextSize(TypedValue.COMPLEX_UNIT_PX, normalItemTextSize)
                    wheelItem?.setTextColor(normalItemColor)
                }
            }
        }

        if (scrollingToFirstPosition) {
            calculateSnap(wheel, RecyclerView.SCROLL_STATE_IDLE)
        }
    }

    private fun getItemViewHolderPosition(itemPosition: Int, wheelChildCount: Int): Int {
        var viewHolderPosition = itemPosition % items.size
        while (viewHolderPosition > wheelChildCount) {
            viewHolderPosition -= wheelChildCount
        }
        return viewHolderPosition
    }

    private fun calculateSnap(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val distanceToCenter = centerOfMiddleComponent - centerYOfWheel
            if (distanceToCenter == -preSnapDistanceToCenter) {
                // Prevents a Newton's cradle-like bug where smoothScrollBy sometimes infinitely overshoots its target
                recyclerView.scrollBy(0, distanceToCenter)
            } else {
                recyclerView.smoothScrollBy(0, distanceToCenter)
                preSnapDistanceToCenter = distanceToCenter
            }
        }
    }

    interface SelectedItemListener {
        fun onItemSelected(item: String)
    }

    class WheelAdapter(private val items: List<CharSequence>, private val textSize: Float) : RecyclerView.Adapter<WheelAdapter.WheelViewHolder>() {

        class WheelViewHolder(val wheelView: TextView) : RecyclerView.ViewHolder(wheelView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WheelViewHolder {
            return WheelViewHolder(TextView(parent.context, null)).apply { wheelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize) }
        }

        override fun onBindViewHolder(holder: WheelViewHolder, position: Int) {
            holder.wheelView.text = items[position % items.size]
        }

        override fun getItemCount() = Int.MAX_VALUE // Hack for "infinite" circular scroll
    }

    private companion object {
        const val DEFAULT_ITEM = "1"
        const val DEFAULT_TEXT_SIZE = 24
        const val MINIMUM_ITEMS = 6 // arbitrary
        const val TEXT_SIZE_OFFSET_CLOSE_ITEM = -2
        const val TEXT_SIZE_OFFSET_NORMAL_ITEM = -4
    }
}
