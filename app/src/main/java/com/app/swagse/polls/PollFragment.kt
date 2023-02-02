package com.app.swagse.polls

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.network.NewApiResponse
import com.app.swagse.network.NewRetrofitClient
import com.app.swagse.sharedpreferences.PrefConnect
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_poll.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PollFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var add_option: TextView
    lateinit var createPoll: Button
    lateinit var list: MutableList<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_poll, container, false)

        view.findViewById<TextView>(R.id.users_name).text =
            PrefConnect.readString(context, Constants.USERNAME, "")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val image = view.findViewById(R.id.userImage) as ImageView
        Glide.with(image).load(PrefConnect.readString(context, Constants.USERPIC, "")).into(image)

        val deadline = view.findViewById<TextView>(R.id.deadline)

        list = mutableListOf<EditText>()
        add_option = view.findViewById(R.id.another_option)
        add_option.setOnClickListener {
            addView(view);
        }
        createPoll = view.findViewById(R.id.create_poll)
        createPoll.setOnClickListener {
            if (deadline.text.isEmpty()){
                Toast.makeText(context, "Please Add Deadline", Toast.LENGTH_SHORT).show()
                deadline.requestFocus()
                return@setOnClickListener
            }
            if (list.size == 0) {
                Toast.makeText(context, "Please Add Poll Option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val map = mutableListOf<String>()
            for (text in list) {
                map.add(text.text.toString())
            }
            addPoll(map, view.findViewById<TextView>(R.id.poll_decription).text.toString())

        }

        return view;
    }

    private fun addPoll(map: MutableList<String>, text: String) {
        if (text.isEmpty() || map.isEmpty()) {
            return
        }
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterface = NewRetrofitClient.getInstance().api
        val jsonObject: JSONObject = JSONObject()

        jsonObject.put("user_id", PrefConnect.readString(context, Constants.USERID, ""))
        jsonObject.put("question", text)
        jsonObject.put("options", map)
        jsonObject.put("end_date", getDaysAgo(5).toString())
        val pollModel = pollModel(
            PrefConnect.readString(context, Constants.USERID, ""),
            text,
            getDaysAgo( deadline.text.toString().toInt()).toString(),
            map
        )
        val responseCall = apiInterface.createPoll(pollModel)

        responseCall.enqueue(object  : retrofit2.Callback<pollModel>{
            override fun onResponse(call: Call<pollModel>, response: Response<pollModel>) {
                progressDialog.dismiss()
                val group = view?.findViewById(R.id.layout) as ViewGroup
                group.removeAllViews()
                view!!.findViewById<TextView>(R.id.poll_decription).text = ""
                Toast.makeText(context, "Poll Added Successfully", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<pollModel>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, "Failed to add poll", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun getDaysAgo(daysAgo: Int): String? {

        val ldt = LocalDateTime.now().plusDays(5)
        val formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        return formmat1.format(ldt)
    }

    private fun addView(view: View) {
        val layout: LayoutInflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val group = view.findViewById(R.id.layout) as ViewGroup
        val new_edit = layout.inflate(R.layout.poll_edittext, group)

        val edit = new_edit.findViewById(R.id.edit_poll) as EditText
        edit.requestFocus()
        edit.id = 1212 + list.size

        list.add(edit)

        new_edit.findViewById<TextView>(R.id.title_poll).setText("Option " + list.size)
        new_edit.findViewById<TextView>(R.id.title_poll).id = list.size
    }

    companion object {
        fun newInstance(param1: String, param2: String) = PollFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}