package com.app.swagse.polls

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.sharedpreferences.PrefConnect
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PollFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var add_option: TextView
    lateinit var createPoll: Button
    lateinit var list : MutableList<EditText>

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
        val view : View =  inflater.inflate(R.layout.fragment_poll, container, false)

        view.findViewById<TextView>(R.id.users_name).text =
            PrefConnect.readString(context, Constants.USERNAME, "")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val image = view.findViewById(R.id.userImage) as ImageView
        Glide.with(image).load(PrefConnect.readString(context, Constants.USERPIC, "")).into(image)

        list = mutableListOf<EditText>()
        add_option = view.findViewById(R.id.another_option)
        add_option.setOnClickListener {
            addView(view);
        }
        createPoll = view.findViewById(R.id.create_poll)
        createPoll.setOnClickListener {
            if (list.size == 0){
                Toast.makeText(context , "Please Add Poll Option" , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val map = HashMap<String , String>()
            for ( text in list){ map[text.toString()] = text.text.toString() }
            Log.d("TAG", "hashmap value: ${map.values}")
        }

        return view;
    }

    private fun addView(view: View) {
        val layout : LayoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val group = view.findViewById(R.id.layout) as ViewGroup
        val new_edit =  layout.inflate(R.layout.poll_edittext , group)

        val edit = new_edit.findViewById(R.id.edit_poll) as EditText
        edit.id = 1212+list.size
        edit.setText("random"+edit.hashCode())

        list.add(edit)

        new_edit.findViewById<TextView>(R.id.title_poll).setText("Option "+list.size)
        new_edit.findViewById<TextView>(R.id.title_poll).id = list.size
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            PollFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}