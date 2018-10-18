package com.nerdery.pkarels.life.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nerdery.pkarels.life.R
import java.lang.ClassCastException

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val ARG_MODULE_NAME = "module_name"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DownloadFragment.OnDownloadFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DownloadFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DownloadFragment : Fragment() {
    private var moduleName: String? = null
    private var listener: OnDownloadFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            moduleName = it.getString(ARG_MODULE_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_download, container, false)
        val downloadBtn: View = rootView.findViewById(R.id.btn_download)
        downloadBtn.setOnClickListener {
            onButtonPressed(moduleName)
        }
        return rootView
    }

    private fun onButtonPressed(moduleName: String?) {
        listener?.onFragmentInteraction(moduleName)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnDownloadFragmentInteractionListener) {
            listener = context
        } else {
            if (context != null) {
                val builder = StringBuilder("Activity")
                builder.append(" does not implement ")
                        .append(OnDownloadFragmentInteractionListener::class.simpleName)
                throw ClassCastException(builder.toString())

            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnDownloadFragmentInteractionListener {
        fun onFragmentInteraction(moduleName: String?)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment BaseWeatherFragment.
         */
        @JvmStatic
        fun newInstance(param1: String) =
                DownloadFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_MODULE_NAME, param1)
                    }
                }
    }
}
