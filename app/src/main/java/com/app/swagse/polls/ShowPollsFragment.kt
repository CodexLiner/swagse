package com.app.swagse.polls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShowPollsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_show_polls, container, false)

        val polls_Rec = view.findViewById<RecyclerView>(R.id.polls_Rec);
        polls_Rec.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        val x = listOf<String>("sdsd" , "dsdsdsds" , "dsdsd" , "dsdsds")
        val list = mutableListOf<PollModel>()
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        list.add(PollModel("tilele elleleelel" , x as MutableList<String>))
        val adapter = PollAdapter(list);
        adapter.setHasStableIds(true)
        polls_Rec.adapter = adapter
        return view;
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShowPollsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}