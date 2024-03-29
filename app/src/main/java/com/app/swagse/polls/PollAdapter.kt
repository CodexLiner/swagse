package com.app.swagse.polls


import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.network.NewApiResponse
import com.app.swagse.network.NewRetrofitClient
import com.app.swagse.polls.comments.PollComments
import com.app.swagse.sharedpreferences.PrefConnect
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.poll_list_layout.view.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

class PollAdapter(val list: ShowPollsResponse) : RecyclerView.Adapter<PollAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById<ImageView>(R.id.userImage)
        val comment: ImageView = itemView.findViewById<ImageView>(R.id.comment)
        val mainLayout_options: LinearLayout =
            itemView.findViewById<LinearLayout>(R.id.mainLayout_options)
        val like_button: ImageView = itemView.findViewById<ImageView>(R.id.like_button)
        val userName: TextView = itemView.findViewById<TextView>(R.id.users_name)
        val Question: TextView = itemView.findViewById<TextView>(R.id.askedQuestion)
        val total_vote: TextView = itemView.findViewById<TextView>(R.id.total_vote)
        val start_data: TextView = itemView.findViewById<TextView>(R.id.start_data)
        val time_left: TextView = itemView.findViewById<TextView>(R.id.time_left)
        val likes_count: TextView = itemView.findViewById<TextView>(R.id.likes_count)
        val comments_count: TextView = itemView.findViewById<TextView>(R.id.comments_count)
        val RadioGroup: RadioGroup = itemView.findViewById<RadioGroup>(R.id.RadioGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.active_polls_layout, parent, false);
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Question.text = list.dataItems[position].question.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        if (list.dataItems[position].votes_count != null) {
            holder.total_vote.text = list.dataItems[position]?.votes_count + " votes"
        }
        holder.time_left.text = list.dataItems[position]?.deadline
        holder.likes_count.text = list.dataItems[position]?.likes_count.toString()
        holder.comments_count.text = list.dataItems[position]?.comments_count.toString()
        holder.time_left.text = list.dataItems[position]?.deadline
        holder.userName.text = list.dataItems[position].userdata.userName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        Glide.with(holder.userImage).load(list!!.dataItems.get(position).userdata.img)
            .into(holder.userImage)

        if (list.dataItems[position].start_date!=null){
            holder.start_data.text = list.dataItems[position].start_date.split(" ")[0]
        }
        for (i in list.dataItems[position].options.indices!!) {

            val view: View = LayoutInflater.from(holder.mainLayout_options.context)
                .inflate(R.layout.poll_list_layout, null)
            view.progressView1.labelText = list.dataItems[position].options[i]
            holder.mainLayout_options.addView(view)

            if (list.dataItems[position].votes != null) {
                if (list.dataItems[position].votes.answer.equals(list.dataItems[position].options[i])) {
                    view.progressView1.colorBackground = R.color.black
                }
                changeViewState(holder.mainLayout_options)
            }

            if (list.dataItems[position].result_count != null && list.dataItems[position].result_count.isNotEmpty()) {
                if (list.dataItems[position].result_count.sum() > 0) {
                    val devide: Double =
                        (list.dataItems[position].result_count[i].toDouble() / list.dataItems[position].result_count.sum()
                            .toDouble()).toDouble()
                    val percentage = devide * 100
                    view.progressView1.progress = percentage.toFloat()
                    view.progressView1.labelText =
                        list.dataItems[position].options[i] + " (" + percentage.toInt() + "%)"
                    if (isHighest(percentage, list.dataItems[position].result_count)) {
                        view.progressView1.colorBackground = R.color.blue
                    }
                } else {
                    view.progressView1.labelText =
                        list.dataItems[position].options[i] + " (" + 0 + "%)"
                }



                changeViewState(holder.mainLayout_options)
            }

            view.setOnClickListener {
                view.progressView1.colorBackground = R.color.black
                changeViewState(holder.mainLayout_options)

                val progressDialog = ProgressDialog(view.context)
                progressDialog.setMessage("Please wait...")
                progressDialog.show()
                val apiInterface = NewRetrofitClient.getInstance().api
                val responseCall = apiInterface.vote(
                    PrefConnect.readString(view.context, Constants.USERID, ""),
                    list.dataItems[position].id,
                    view.progressView1.labelText
                )
                Log.d(
                    "TAG",
                    "onBindViewHolder: ${view.progressView1.labelText as String?} ${list.dataItems[position].id}"
                )
                responseCall.enqueue(object : retrofit2.Callback<NewApiResponse> {
                    override fun onResponse(
                        call: Call<NewApiResponse>, response: Response<NewApiResponse>
                    ) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            view.context, "Vote Added Successfully ", Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<NewApiResponse>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(view.context, " Failed to Vote", Toast.LENGTH_SHORT).show()
                    }

                })


            }
        }

        if (list.dataItems[position].likes != null) {
            list.dataItems.get(position).likes.id = "1";
            Glide.with(holder.like_button)
                .load(holder.like_button.resources.getDrawable(R.drawable.ic_unlike))
                .into(holder.like_button)
        }

        holder.like_button.setOnClickListener {
            likePoll(
                PrefConnect.readString(holder.like_button.context, Constants.USERID, ""),
                list.dataItems.get(position).id,
                holder.like_button.context
            )
            if (list.dataItems.get(position).likes == null) {

                if (list.dataItems.get(position).likes == null) {
                    list.dataItems.get(position).likes = com.app.swagse.polls.votes()
                }

                list.dataItems[position].likes.id = "1";
                Glide.with(holder.like_button)
                    .load(holder.like_button.resources.getDrawable(R.drawable.ic_unlike))
                    .into(holder.like_button)

                list.dataItems[position]?.likes_count = list.dataItems[position]?.likes_count?.plus(
                    1
                )!!
                holder.likes_count.text = list.dataItems[position]?.likes_count.toString()
            }
        }

        holder.comment.setOnClickListener {
            if (list.dataItems.get(position).comments != null) {
                val fm: FragmentManager =
                    (holder.comment.context as AppCompatActivity).supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(
                    R.id.frame_poll,
                    PollComments(
                        list.dataItems[position]?.comments!!,
                        list.dataItems.get(position).id
                    )
                )
                    .addToBackStack("adapter")
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft.commit()
            }

        }
    }


    private fun isHighest(value: Double, resultCount: IntArray?): Boolean {
        println(resultCount?.sum())
        if (resultCount != null) {
            var highest = -1.0;
            for (i in resultCount) {
                val devide: Double = (i.toDouble() / resultCount.sum().toDouble()).toDouble()
                val percentage = devide * 100
                if (percentage > highest) {
                    highest = percentage.toDouble()
                }
            }
            return value > highest
        }
        return false
    }

    private fun publishResult(results: IntArray?, position: Int) {
        if (results != null) {
            var highestIndex = -1;
            results.forEach { i ->
                var percentage = ((((i / results.sum()) * 100)));
            }


        }
    }

    private fun changeViewState(linearLayout: LinearLayout) {
        val childCount: Int = linearLayout.childCount
        for (i in 0 until childCount) {
            val v: View = linearLayout.getChildAt(i)
            v.isEnabled = false;
            v.isActivated = false
            v.isClickable = false
//            Toast.makeText(linearLayout.context , "inside is = ${v.}")

        }
    }

    private fun likePoll(readString: String?, id: String?, context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        val apiInterface = NewRetrofitClient.getInstance().api
        val responseCall = apiInterface.like(readString, id)
        responseCall.enqueue(object : retrofit2.Callback<votes> {
            override fun onResponse(call: Call<votes>, response: Response<votes>) {
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<votes>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }

    override fun getItemCount(): Int {
        return list.dataItems.size ?: 0
    }
}