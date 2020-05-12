package com.example.falihmandiritestapp.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.falihmandiritestapp.R
import com.example.falihmandiritestapp.common.adapter.BindableAdapter
import com.example.falihmandiritestapp.common.ui.InfiniteScroll
import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.generic_list_dialog.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import kotlinx.android.synthetic.main.simple_list_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModel()

    private val categories by lazy {
        resources.getStringArray(R.array.categories)
    }

    private val adapter: BindableAdapter<MainViewModel, Article> by lazy {
        BindableAdapter<MainViewModel, Article>(viewModel = viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initViews()
        initObserver()
    }

    override fun onStart() {
        super.onStart()
        viewModel.setCategory(categories[0])
    }

    private fun initObserver() {
        viewModel.selectedSource.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrBlank()){
                binding.tvSource.text = "All"
            } else {
                binding.tvSource.text = it
            }
        })
        viewModel.loadingArticleListEvent.observe(viewLifecycleOwner, Observer {isLoading ->
            if(isLoading != null && isLoading == true){
                if (viewModel.mPage > 1){
                    binding.tvLoadingContainer.visibility = View.GONE
                    binding.tvLoadMoreContainer.visibility = View.VISIBLE
                } else {
                    binding.tvLoadingContainer.visibility = View.VISIBLE
                    binding.tvLoadMoreContainer.visibility = View.GONE
                }
            } else {
                binding.tvLoadingContainer.visibility = View.GONE
                binding.tvLoadMoreContainer.visibility = View.GONE
            }
        })
    }

    private fun initViews() {
        binding.categoryFilterContainer.setOnClickListener {
            showCategoryList()
        }
        binding.sourceFilterContainer.setOnClickListener {
            showSourceList()
        }

        binding.includeToolbar.toolbarSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                resetRvScrollListener()
                viewModel.setKeyword(v.text.toString())
            }
            false
        }
        binding.includeToolbar.toolbarClose.setOnClickListener {
            resetRvScrollListener()
            binding.includeToolbar.toolbarSearch.setText("")
            viewModel.resetSearch()
        }

        binding.errorWarningContainer.setOnClickListener {
            viewModel.loadNextPage()
        }

        binding.rvUserList.layoutManager = LinearLayoutManager(context)
        binding.rvUserList.adapter = adapter

        resetRvScrollListener()
    }

    private fun resetRvScrollListener() {
        binding.rvUserList.clearOnScrollListeners()
        binding.rvUserList.addOnScrollListener(object: InfiniteScroll(){
            override fun onLoadMore() {
                viewModel.loadNextPage()
            }
        })
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
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
                resetRvScrollListener()
                viewModel.setCategory(categoryString)
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
        val view = layoutInflater.inflate(R.layout.simple_list_item, null)
        val tvTitle: TextView = view.tv_title
        tvTitle.text = "All"
        tvTitle.setOnClickListener {
            resetRvScrollListener()
            viewModel.setSource("")
            dialog.dismiss()
        }
        listContainer.addView(view)

        viewModel.articleSources.value?.forEach {source ->
            val view = layoutInflater.inflate(R.layout.simple_list_item, null)

            val tvTitle: TextView = view.tv_title
            tvTitle.text = source.name
            tvTitle.setOnClickListener {
                viewModel.setSource(source.id!!)
                dialog.dismiss()
            }

            listContainer.addView(view)
        }

        dialog.show()
    }
}
