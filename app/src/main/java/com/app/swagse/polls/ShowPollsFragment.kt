package com.app.swagse.polls

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.network.NewRetrofitClient
import com.app.swagse.sharedpreferences.PrefConnect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShowPollsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var polls_Rec : RecyclerView


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
        val view: View = inflater.inflate(R.layout.fragment_show_polls, container, false)

        polls_Rec = view.findViewById<RecyclerView>(R.id.polls_Rec);
        polls_Rec.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        getPollDetails()

        return view;
    }

    private fun getPollDetails() {
        Log.d("TAG", "onResponse: getPolls ")

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterface = NewRetrofitClient.getInstance().api
        val responseCall = apiInterface.getPolls(PrefConnect.readString(context , Constants.USERID , ""))
        responseCall.enqueue(object : Callback<ShowPollsResponse> {
            override fun onResponse(
                call: Call<ShowPollsResponse>,
                response: Response<ShowPollsResponse>
            ) {
                Log.d("TAG", "onResponse: before reponse ")
                if (response.code() == 200) {
                    Log.d("TAG", "onResponse: 200 reponse ")
                    progressDialog.dismiss()
                    Log.d("TAG", "onResponse: body reponse ${response.body().toString()}")

                    if (response.body()!!.success == "true") {
                        val polldataitems = response.body()!!.dataItems
                        Log.d("TAG", "onResponse: ${response.body()!!.dataItems.toString()}")

                        if (polldataitems.size != 0) {
                            val adapter = PollAdapter(response.body()!!);
                            adapter.setHasStableIds(true)
                            polls_Rec.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ShowPollsResponse>, t: Throwable) {
                Log.d("TAG", "onResponse: failed ${t.message} ")
                progressDialog.dismiss()
            }
        })

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