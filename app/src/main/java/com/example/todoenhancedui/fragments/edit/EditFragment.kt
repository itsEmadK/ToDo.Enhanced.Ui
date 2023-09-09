package com.example.todoenhancedui.fragments.edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoenhancedui.R
import com.example.todoenhancedui.data.models.Category
import com.example.todoenhancedui.data.models.Task
import com.example.todoenhancedui.data.viewmodels.SharedViewModel
import com.example.todoenhancedui.data.viewmodels.TaskViewModel
import com.example.todoenhancedui.databinding.FragmentUpdateTaskBinding
import java.time.LocalTime

class EditFragment() : Fragment() {
    private var _binding: FragmentUpdateTaskBinding? = null
    private val binding: FragmentUpdateTaskBinding
        get() = _binding!!

    private lateinit var titleEditText: EditText
    private lateinit var documentCategoryBtn: ImageButton
    private lateinit var eventCategoryBtn: ImageButton
    private lateinit var readingCategoryBtn: ImageButton
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var notesEditText: EditText
    private val viewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()
    private var category: Category = Category.WORKING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateTaskBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defaultDate = args.date

        binding.closeBtn.setOnClickListener {
            val action = EditFragmentDirections.actionEditFragmentToListFragment()
            action.date = sharedViewModel.dateToString(defaultDate)
            findNavController().navigate(action)
        }

        binding.deleteBtn.setOnClickListener { confirmRemoval(args.taskItem) }

        val task = args.taskItem
        titleEditText.setText(task.title)
        when (task.category) {
            Category.WORKING -> selectWorkingCategory()
            Category.READING -> selectReadingCategory()
            Category.DATING -> selectDatingCategory()
        }
        dateEditText.setText(task.date?.let { sharedViewModel.dateToString(it) })
        timeEditText.setText(task.time?.toString())
        notesEditText.setText(task.notes)

        documentCategoryBtn.setOnClickListener { selectWorkingCategory() }
        eventCategoryBtn.setOnClickListener { selectDatingCategory() }
        readingCategoryBtn.setOnClickListener { selectReadingCategory() }

        timeEditText.setOnClickListener {
            var defHour = LocalTime.now().hour
            var defMin = LocalTime.now().minute
            task.time?.let {
                defHour = it.hour
                defMin = it.minute
            }
            sharedViewModel.showTimePickerDialog(timeEditText, defHour, defMin, requireContext())
        }
        dateEditText.setOnClickListener {
            var defYear = defaultDate.year
            var defMonth = defaultDate.monthValue
            var defDay = defaultDate.dayOfMonth
            task.date?.let {
                defYear = it.year
                defMonth = it.monthValue
                defDay = it.dayOfMonth
            }
            sharedViewModel.showDatePickerDialog(
                dateEditText, defYear, defMonth, defDay, requireContext()
            )

        }
        binding.saveTaskButton.setOnClickListener {
            val validation = sharedViewModel.verifyUserInputs(titleEditText.text.toString())
            if (validation) {
                task.title = titleEditText.text.toString()
                task.date = sharedViewModel.parseToLocalDate(dateEditText.text.toString())
                task.time = sharedViewModel.parseToLocalTime(timeEditText.text.toString())
                task.notes = notesEditText.text.toString()
                task.category = getSelectedCategory()
                updateDataInDB(task)
                val action = EditFragmentDirections.actionEditFragmentToListFragment()
                task.date?.let { action.date = sharedViewModel.dateToString(it) }
                findNavController().navigate(action)
            } else Toast.makeText(requireContext(), "Please enter a title !", Toast.LENGTH_SHORT)
                .show()
        }

    }


    private fun initViews() {
        titleEditText = binding.titleEditText
        documentCategoryBtn = binding.documentCategoryImageView
        eventCategoryBtn = binding.eventCategoryImageView
        readingCategoryBtn = binding.readingCategoryImageView
        dateEditText = binding.dateEditText
        timeEditText = binding.timeEditText
        notesEditText = binding.notesEditText
    }

    private fun selectDatingCategory() {
        category = Category.DATING
        readingCategoryBtn.alpha = 0.3F
        documentCategoryBtn.alpha = 0.3F
        eventCategoryBtn.alpha = 1F
    }

    private fun selectWorkingCategory() {
        category = Category.WORKING
        readingCategoryBtn.alpha = 0.3F
        documentCategoryBtn.alpha = 1F
        eventCategoryBtn.alpha = 0.3F
    }

    private fun selectReadingCategory() {
        category = Category.READING
        readingCategoryBtn.alpha = 1F
        documentCategoryBtn.alpha = 0.3F
        eventCategoryBtn.alpha = 0.3F
    }

    private fun getSelectedCategory(): Category {
        return if (eventCategoryBtn.alpha == 1F) Category.DATING
        else if (readingCategoryBtn.alpha == 1F) Category.READING
        else if (documentCategoryBtn.alpha == 1F) Category.WORKING
        else Category.WORKING
    }

    private fun updateDataInDB(task: Task) {
        viewModel.updateTask(task)
    }

    private fun confirmRemoval(task: Task) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Confirm Removal")
        dialog.setMessage("Are you sure you want to delete \"${task.title}\" task?")
        dialog.setPositiveButton("Delete") { _, _ ->
            viewModel.deleteTask(task)
            findNavController().navigate(R.id.action_editFragment_to_listFragment)
            Toast.makeText(
                requireContext(),
                " Deleted \"${task.title}\" successfully",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        dialog.setNegativeButton("Cancel") { _, _ -> }
        dialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val category = getSelectedCategory().name
        val title = titleEditText.text.toString()
        val date = dateEditText.text.toString()
        val time = timeEditText.text.toString()
        val notes = notesEditText.text.toString()
        outState.putString("title", title)
        outState.putString("category", category)
        outState.putString("date", date)
        outState.putString("time", time)
        outState.putString("notes", notes)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            val savedTitle = it.getString("title")
            val savedCat = it.getString("category", "WORKING")
            val savedDate = it.getString("date")
            val savedTime = it.getString("time")
            val savedNotes = it.getString("notes")
            titleEditText.setText(savedTitle)
            dateEditText.setText(savedDate)
            timeEditText.setText(savedTime)
            notesEditText.setText(savedNotes)
            when (Category.valueOf(savedCat)) {
                Category.WORKING -> selectWorkingCategory()
                Category.DATING -> selectDatingCategory()
                Category.READING -> selectReadingCategory()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
