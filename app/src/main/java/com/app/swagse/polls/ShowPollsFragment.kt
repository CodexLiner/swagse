package com.app.swagse.polls

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.swagse.R
import com.app.swagse.constants.Constants
import com.app.swagse.fragment.SwagTubeFragment
import com.app.swagse.fragment.SwaggerNewFragment
import com.app.swagse.fragment.TrendingFragment
import com.app.swagse.network.NewRetrofitClient
import com.app.swagse.polls.polls.ResultFragment
import com.app.swagse.polls.polls.ShowFragment
import com.app.swagse.sharedpreferences.PrefConnect
import com.google.android.material.tabs.TabLayout
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
    lateinit var Tab : TabLayout


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
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_show_polls, container, false)
        Tab = view.findViewById(R.id.tab_layout)

        Tab.addTab(Tab.newTab().setText("Active"))
        Tab.addTab(Tab.newTab().setText("Result"))

        val fm: FragmentManager? = activity?.supportFragmentManager
        val ft = fm?.beginTransaction()
        ft?.replace(R.id.frame_poll, ShowFragment())
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft?.commit()

        Tab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var fragment: Fragment? = null
                when (tab!!.position) {
                    0 -> fragment = ShowFragment()
                    1 -> fragment = ResultFragment()
                }
                val fm: FragmentManager? = activity?.supportFragmentManager
                val ft = fm?.beginTransaction()
                ft?.replace(R.id.frame_poll, fragment!!)
                ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft?.commit()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

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