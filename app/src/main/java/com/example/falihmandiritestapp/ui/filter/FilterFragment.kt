package com.example.falihmandiritestapp.ui.filter

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.falihmandiritestapp.MyApp

import com.example.falihmandiritestapp.R
import com.example.falihmandiritestapp.ui.main.MainViewModel
import com.example.falihmandiritestapp.ui.main.MainViewModelFactory
import kotlinx.android.synthetic.main.filter_fragment.*
import kotlinx.android.synthetic.main.generic_list_dialog.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import kotlinx.android.synthetic.main.simple_list_item.view.*
import javax.inject.Inject

class FilterFragment : Fragment() {

    companion object {
        fun newInstance() = FilterFragment()
    }

    @set:Inject
    var mainViewModelFactory: MainViewModelFactory? = null
    private lateinit var mainViewModel: MainViewModel
    @set:Inject
    var filterViewModelFactory: FilterViewModelFactory? = null
    private lateinit var filterViewModel: FilterViewModel

    private val categories by lazy {
        filterViewModel.getAllCategories()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApp).appComponent?.doInjection(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(requireActivity(), mainViewModelFactory!!).get(MainViewModel::class.java)
        filterViewModel = ViewModelProvider(requireActivity(), filterViewModelFactory!!).get(FilterViewModel::class.java)
        return inflater.inflate(R.layout.filter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initViews() {
        category_filter_container.setOnClickListener {
            showCategoryList()
        }
        source_filter_container.setOnClickListener {
            showSourceList()
        }

        include_toolbar.toolbarSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //resetRvScrollListener()
                filterViewModel.setKeyword(v.text.toString())
            }
            false
        }
        include_toolbar.toolbarClose.setOnClickListener {
            //resetRvScrollListener()
            include_toolbar.toolbarSearch.setText("")
            filterViewModel.resetSearch()
        }
    }

    private fun initObserver() {
        filterViewModel.selectedCategory.observe(viewLifecycleOwner, Observer {
            tv_category.text = it
        })
        filterViewModel.selectedSource.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrBlank()){
                tv_source.text = getString(R.string.all)
            } else {
                tv_source.text = it
            }
        })

        filterViewModel.loadingSourceListEvent.observe(viewLifecycleOwner, Observer {isLoading ->
            if(isLoading != null && isLoading == true){
                source_filter_container.visibility = View.GONE
                source_filter_loading_container.visibility = View.VISIBLE
            } else {
                source_filter_container.visibility = View.VISIBLE
                source_filter_loading_container.visibility = View.GONE
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun showCategoryList() {
        if (context == null) return
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.generic_list_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.dialog_title.text = getString(R.string.category_list_dialog_title)
        val listContainer = dialog.listContainer

        listContainer.removeAllViews()

        categories.forEach {categoryString ->
            val view = layoutInflater.inflate(R.layout.simple_list_item, null)

            val tvTitle: TextView = view.tv_title
            tvTitle.text = categoryString
            tvTitle.setOnClickListener {
                //resetRvScrollListener()
                filterViewModel.setCategory(categoryString)
                dialog.dismiss()
            }

            listContainer.addView(view)
        }

        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showSourceList() {
        if (context == null) return
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.generic_list_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.dialog_title.text = getString(R.string.source_list_dialog_title)
        val listContainer = dialog.listContainer

        listContainer.removeAllViews()

        //first selection is for all sources / blank source
        val firstView = layoutInflater.inflate(R.layout.simple_list_item, null)
        val firstTvTitle: TextView = firstView.tv_title
        firstTvTitle.text = getString(R.string.all)
        firstTvTitle.setOnClickListener {
            //resetRvScrollListener()
            filterViewModel.setSource("")
            dialog.dismiss()
        }
        listContainer.addView(firstView)

        filterViewModel.articleSources.value?.forEach { source ->
            val view = layoutInflater.inflate(R.layout.simple_list_item, null)

            val tvTitle: TextView = view.tv_title
            tvTitle.text = source.name
            tvTitle.setOnClickListener {
                filterViewModel.setSource(source.id!!)
                dialog.dismiss()
            }

            listContainer.addView(view)
        }

        dialog.show()
    }
}
