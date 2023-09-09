package com.example.todoenhancedui.fragments.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoenhancedui.R
import com.example.todoenhancedui.data.models.Task
import com.example.todoenhancedui.data.viewmodels.SharedViewModel
import com.example.todoenhancedui.data.viewmodels.TaskViewModel
import com.example.todoenhancedui.databinding.FragmentTaskListBinding
import kotlinx.coroutines.launch
import java.time.LocalDate

class ListFragment() : Fragment(), UpdateTaskListener, TransferToEditFragmentListener {
    private var _binding: FragmentTaskListBinding? = null
    private val binding: FragmentTaskListBinding
        get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val args: ListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val openAdapter = TaskListAdapter(this, this)
        val completedAdapter = TaskListAdapter(this, this)
        val openTasksRecyclerView = binding.todoRecyclerView
        val completedTasksRecyclerView = binding.todoDoneRecyclerView

        openTasksRecyclerView.adapter = openAdapter
        openTasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        completedTasksRecyclerView.adapter = completedAdapter
        completedTasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        if (args.date.isNotEmpty()) {
            viewModel.selectedDate =
                sharedViewModel.parseToLocalDate(args.date)!!
        }
        binding.dateTextView.text = convertDateFormat(viewModel.selectedDate)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasksForDate.observe(viewLifecycleOwner) {
                openAdapter.setData(viewModel.getOpenTasks(it))
                completedAdapter.setData(viewModel.getCompletedTasks(it))
            }
        }
        binding.addNewTaskBtn.setOnClickListener {
            val action =
                ListFragmentDirections.actionListFragmentToAddFragment(viewModel.selectedDate)
            findNavController().navigate(action)
        }
        binding.arrowBackBtn.setOnClickListener {
            viewModel.selectedDate = viewModel.selectedDate.minusDays(1L)
            binding.dateTextView.text = convertDateFormat(viewModel.selectedDate)

        }
        binding.arrowForwardBtn.setOnClickListener {
            viewModel.selectedDate = viewModel.selectedDate.plusDays(1L)
            binding.dateTextView.text = convertDateFormat(viewModel.selectedDate)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onUpdate(task: Task) {
        viewModel.updateTask(task)
    }

    private fun convertDateFormat(date: LocalDate): String =
        "${date.month} ${date.dayOfMonth}, ${date.year}"

    override fun onTransfer(task: Task) {
        val action =
            ListFragmentDirections.actionListFragmentToEditFragment(task, viewModel.selectedDate)
        findNavController().navigate(action)
    }

}