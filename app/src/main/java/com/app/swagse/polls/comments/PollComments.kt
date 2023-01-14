package com.app.swagse.polls.comments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.network.NewRetrofitClient
import com.app.swagse.sharedpreferences.PrefConnect
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var commentRecyclerView: RecyclerView

class PollComments(val comments: MutableList<comments>, val id: String) : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_poll_comments, container, false)
        commentRecyclerView = view.findViewById(R.id.commentRecyclerView)
        commentRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        commentRecyclerView.adapter = CommentAdapter(comments)
        val enterComment =
            view.findViewById<AppCompatEditText>(R.id.enterComment) as AppCompatEditText;
        val add_swagTubeComment = view.findViewById<AppCompatTextView>(R.id.add_swagTubeComment)

        add_swagTubeComment.setOnClickListener {
            if (enterComment.text != null) {
                addComment(
                    enterComment.text.toString(),
                    PrefConnect.readString(context, Constants.USERID, ""),
                    id
                )
            }
        }

        return view
    }

    private fun addComment(text: String, id: String?, poll_id: String) {

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterface = NewRetrofitClient.getInstance().api
        val responseCall = apiInterface.postComment(id, text, poll_id)
        responseCall.enqueue(object : retrofit2.Callback<comments> {
            override fun onResponse(call: Call<comments>, response: Response<comments>) {
                progressDialog.dismiss()
                if (activity?.supportFragmentManager?.backStackEntryCount!! >0){
                    Toast.makeText(context, "Comment Added Successfully", Toast.LENGTH_SHORT).show()
                    activity?.supportFragmentManager?.popBackStack()
                }
            }

            override fun onFailure(call: Call<comments>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(context, "failed to comment", Toast.LENGTH_SHORT).show()
            }

        })
    }

}