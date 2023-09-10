package com.example.todoenhancedui.fragments.list

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoenhancedui.R
import com.example.todoenhancedui.data.models.Category
import com.example.todoenhancedui.data.models.Task
import com.example.todoenhancedui.databinding.RecyclerItemTodoDayBinding
import java.time.LocalDate
import java.time.LocalTime

class TaskListAdapter(
    val updateTaskListener: TaskCompletedStatusChangedListener,
    val transferToEditListener: TransferToEditFragmentListener
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var data = emptyList<Task>()

    inner class TaskViewHolder(view: RecyclerItemTodoDayBinding) : ViewHolder(view.root) {
        val timeTv = view.timeTextView
        val titleTv = view.titleTextView
        val categoryIv = view.categoryIconImageView
        val doneCheckBox = view.doneCheckBox

        init {
            doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                val updatedItem = data[adapterPosition]
                updatedItem.isCompleted = isChecked
                updateTaskListener.onCompletedStatusChanged(task = updatedItem)
            }

            itemView.setOnClickListener { transferToEditListener.onTransfer(data[adapterPosition]) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            RecyclerItemTodoDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        bind(holder, position)
    }

    @SuppressLint("SetTextI18n")
    private fun bind(holder: TaskViewHolder, position: Int) {
        holder.titleTv.text = data[position].title
        holder.timeTv.text = if (data[position].time == null) "" else data[position].time.toString()
        holder.doneCheckBox.isChecked = data[position].isCompleted

        when (data[position].category) {
            Category.WORKING -> holder.categoryIv.setImageResource(R.drawable.document_icon)
            Category.DATING -> holder.categoryIv.setImageResource(R.drawable.event_icon)
            Category.READING -> holder.categoryIv.setImageResource(R.drawable.book_icon)
        }

        if (data[position].isCompleted) {
            val paintFlag = Paint.STRIKE_THRU_TEXT_FLAG
            holder.timeTv.paintFlags = paintFlag
            holder.titleTv.paintFlags = paintFlag
            data[position].completedTime?.let { holder.timeTv.text = "${it.hour}:${it.minute}" }
        }

        //Changing the color of the expired tasks of the same day to red:
        with(data[position]) { changeExpiredTaskColors(date, time, isCompleted, holder) }
    }

    private fun changeExpiredTaskColors(
        date: LocalDate?, time: LocalTime?, completed: Boolean, holder: TaskViewHolder
    ) {
        if (completed) return
        val red = ContextCompat.getColor(holder.timeTv.context, R.color.red)
        if (date == null) {
            setDefaultTextViewColors(holder)
        } else if (date.isBefore(LocalDate.now())) {
            setTextViewColors(red, holder)
        } else if (date.isEqual(LocalDate.now())) {
            if (time == null) {
                setDefaultTextViewColors(holder)
            } else {
                if (time.isBefore(LocalTime.now())) {
                    setTextViewColors(red, holder)
                } else {
                    setDefaultTextViewColors(holder)
                }
            }
        } else if (date.isAfter(LocalDate.now())) {
            setDefaultTextViewColors(holder)
        }
    }

    private fun isInDarkMode(context: Context): Boolean {
        return when ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> {
                false
            }
        }
    }

    private fun setTextViewColors(color: Int, holder: TaskViewHolder) {
        holder.titleTv.setTextColor(color)
        holder.timeTv.setTextColor(color)
    }

    private fun setDefaultTextViewColors(holder: TaskViewHolder) {
        val nightMode = isInDarkMode(holder.timeTv.context)
        if (nightMode) setTextViewColors(Color.WHITE, holder) else setTextViewColors(
            Color.BLACK, holder
        )
    }

    fun setData(newData: List<Task>) {
        val diffCB = TaskDiffUtil(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCB)
        data = newData.map { it.copy() }
        diffResult.dispatchUpdatesTo(this)
    }

}