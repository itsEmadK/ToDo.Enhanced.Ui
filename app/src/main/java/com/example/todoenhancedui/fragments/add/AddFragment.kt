package com.example.todoenhancedui.fragments.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoenhancedui.data.models.Category
import com.example.todoenhancedui.data.models.Task
import com.example.todoenhancedui.data.viewmodels.SharedViewModel
import com.example.todoenhancedui.data.viewmodels.TaskViewModel
import com.example.todoenhancedui.databinding.FragmentAddTaskBinding
import java.time.LocalTime

class AddFragment() : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args: AddFragmentArgs by navArgs()
    private var _binding: FragmentAddTaskBinding? = null
    private val binding: FragmentAddTaskBinding
        get() = _binding!!

    private lateinit var titleEditText: EditText
    private lateinit var documentCategoryBtn: ImageButton
    private lateinit var eventCategoryBtn: ImageButton
    private lateinit var readingCategoryBtn: ImageButton
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var notesEditText: EditText
    private var category: Category = Category.WORKING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defaultDate = args.date

        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }
        selectWorkingCategory()

        eventCategoryBtn.setOnClickListener {
            selectDatingCategory()
        }
        documentCategoryBtn.setOnClickListener {
            selectWorkingCategory()
        }
        readingCategoryBtn.setOnClickListener {
            selectReadingCategory()
        }

        dateEditText.setOnClickListener {

            sharedViewModel.showDatePickerDialog(
                dateEditText,
                defaultDate.year,
                defaultDate.monthValue,
                defaultDate.dayOfMonth,
                requireContext()
            )
        }
        timeEditText.setOnClickListener {
            sharedViewModel.showTimePickerDialog(
                timeEditText, LocalTime.now().hour, LocalTime.now().minute, requireContext()
            )
        }
        binding.saveTaskButton.setOnClickListener {
            insertDataToDB()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = AddFragmentDirections.actionAddFragmentToListFragment()
                action.date = sharedViewModel.dateToString(args.date)
                findNavController().navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )


    }

    private fun insertDataToDB() {
        val validation = sharedViewModel.verifyUserInputs(titleEditText.text.toString())
        if (validation) {
            val title = titleEditText.text.toString()
            val date = sharedViewModel.parseToLocalDate(dateEditText.text.toString())
            val time = sharedViewModel.parseToLocalTime(timeEditText.text.toString())
            val notes = notesEditText.text.toString()
            val task = Task(title, category, date, time, false, notes, null, null)
            viewModel.insertTask(task)
            val action = AddFragmentDirections.actionAddFragmentToListFragment()
            date?.let { action.date = sharedViewModel.dateToString(it) } ?: action.let {
                it.date = sharedViewModel.dateToString(args.date)
            }
            findNavController().navigate(action)

        } else {
            Toast.makeText(requireContext(), "Please enter a title !", Toast.LENGTH_SHORT).show()
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