package com.app.swagse.polls


import android.app.ProgressDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.network.NewApiResponse
import com.app.swagse.network.NewRetrofitClient
import com.app.swagse.sharedpreferences.PrefConnect
import com.bumptech.glide.Glide
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import java.util.*

class PollAdapter(val list: ShowPollsResponse) : RecyclerView.Adapter<PollAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById<ImageView>(R.id.userImage)
        val userName: TextView = itemView.findViewById<TextView>(R.id.users_name)
        val Question: TextView = itemView.findViewById<TextView>(R.id.askedQuestion)
        val total_vote: TextView = itemView.findViewById<TextView>(R.id.total_vote)
        val time_left: TextView = itemView.findViewById<TextView>(R.id.time_left)
        val RadioGroup: RadioGroup = itemView.findViewById<RadioGroup>(R.id.RadioGroup)
        val Vote = itemView.findViewById<Button>(R.id.Vote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.active_polls_layout, parent, false);
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Question.text = list.dataItems[position]?.question
        holder.total_vote.text = list.dataItems[position]?.votes_count
        holder.time_left.text = list.dataItems[position]?.deadline
        holder.userName.text = list.dataItems[position].userdata.userName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        Glide.with(holder.userImage).load(list!!.dataItems.get(position).userdata.img)
            .into(holder.userImage)

        for (i in list.dataItems.get(position).options!!) {
            Log.d("TAG", "radiobutton : ${position}")
            val button = RadioButton(holder.RadioGroup.context)
            button.text = i
            holder.RadioGroup.addView(button)
        }

        holder.Vote.setOnClickListener {
            val rdb = holder.RadioGroup.checkedRadioButtonId
            val rb = holder.RadioGroup.findViewById<RadioButton>(rdb)
            val progressDialog = ProgressDialog(holder.Vote.context)
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
            val apiInterface = NewRetrofitClient.getInstance().api
            val responseCall = apiInterface.vote(
                PrefConnect.readString(holder.Vote.context, Constants.USERID, ""),
                list.dataItems.get(position).id,
                rb.text as String?
            )
            responseCall.enqueue(object : retrofit2.Callback<NewApiResponse> {
                override fun onResponse(
                    call: Call<NewApiResponse>, response: Response<NewApiResponse>
                ) {
//                    TODO("Not yet implemented")
                    progressDialog.dismiss()
                    Toast.makeText(
                        holder.Vote.context, "Vote Added Successfully ", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<NewApiResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
                    progressDialog.dismiss()
                    Toast.makeText(holder.Vote.context, " Failed to Vote", Toast.LENGTH_SHORT)
                        .show()
                }

            })

        }
    }


    override fun getItemCount(): Int {
        return list.dataItems.size ?: 0
    }
}