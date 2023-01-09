package com.app.swagse.polls


import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.Drawable
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
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.math.log

class PollAdapter(val list: ShowPollsResponse) : RecyclerView.Adapter<PollAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById<ImageView>(R.id.userImage)
        val like_button: ImageView = itemView.findViewById<ImageView>(R.id.like_button)
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
            val button = RadioButton(holder.RadioGroup.context)
            if (list.dataItems.get(position).votes!=null){
                if (list.dataItems.get(position).votes.answer.equals(i)){
                    button.isEnabled = false
                    button.isChecked = true
//                    holder.Vote.text = "voted"
                    holder.Vote.isEnabled = false
                }
            }

            button.text = i
            holder.RadioGroup.addView(button)
        }

        if (list.dataItems[position].likes!=null){
            if (list.dataItems[position].likes.polling_id.equals(list.dataItems[position].id)){
                Glide.with(holder.like_button).load(holder.like_button.resources.getDrawable(R.drawable.ic_like_filled))
            }
        }

        holder.like_button.setOnClickListener{
            likePoll(PrefConnect.readString(holder.Vote.context , Constants.USERID , "") , list.dataItems.get(position).id , holder.like_button.context)
            if (list.dataItems.get(position).likes==null || list.dataItems.get(position).likes.id == "1"){
                if (list.dataItems.get(position).likes == null){
                    list.dataItems.get(position).likes = com.app.swagse.polls.votes()
                }
                list.dataItems.get(position).likes.id = "0";
                Glide.with(holder.like_button).load(holder.like_button.resources.getDrawable(R.drawable.ic_like_filled)).into(holder.like_button)

            }else{
                list.dataItems.get(position).likes.id = "1";
                Glide.with(holder.like_button).load(holder.like_button.resources.getDrawable(R.drawable.like)).into(holder.like_button)
            }
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
            Log.d("TAG", "onBindViewHolder: ${ rb.text as String? } ${list.dataItems.get(position).id}")
            responseCall.enqueue(object : retrofit2.Callback<NewApiResponse> {
                override fun onResponse(
                    call: Call<NewApiResponse>, response: Response<NewApiResponse>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        holder.Vote.context, "Vote Added Successfully ", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<NewApiResponse>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(holder.Vote.context, " Failed to Vote", Toast.LENGTH_SHORT)
                        .show()
                }

            })

        }
    }

    private fun likePoll(readString: String?, id: String? , context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterface = NewRetrofitClient.getInstance().api
        val responseCall = apiInterface.like(readString , id)
        responseCall.enqueue(object : retrofit2.Callback<votes>{
            override fun onResponse(call: Call<votes>, response: Response<votes>) {

            }
            override fun onFailure(call: Call<votes>, t: Throwable) {

            }
        })
    }

    override fun getItemCount(): Int {
        return list.dataItems.size ?: 0
    }
}