package com.example.falihmandiritestapp.ui.main

import android.os.Bundle
import android.view.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.falihmandiritestapp.MyApp
import com.example.falihmandiritestapp.R
import com.example.falihmandiritestapp.common.adapter.BindableAdapter
import com.example.falihmandiritestapp.common.ui.InfiniteScroll
import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.databinding.MainFragmentBinding
import com.example.falihmandiritestapp.ui.filter.FilterViewModel
import com.example.falihmandiritestapp.ui.filter.FilterViewModelFactory
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @set:Inject
    var mainViewModelFactory: MainViewModelFactory? = null
    private lateinit var mainViewModel: MainViewModel
    @set:Inject
    var filterViewModelFactory: FilterViewModelFactory? = null
    private lateinit var filterViewModel: FilterViewModel

    private lateinit var binding: MainFragmentBinding

    private val adapter: BindableAdapter<MainViewModel, Article> by lazy {
        BindableAdapter<MainViewModel, Article>(viewModel = mainViewModel)
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
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        initViews()
        initObserver()
    }

    private fun initObserver() {
        mainViewModel.loadingArticleListEvent.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading != null && isLoading == true){
                if (mainViewModel.mPage > 1){
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
        mainViewModel.openArticleDetailEvent.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                if (context == null) return@let
                val intent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                    .setStartAnimations(context!!, R.anim.slide_in_right, R.anim.hold)
                    .setExitAnimations(context!!, R.anim.hold, R.anim.slide_out_right)
                    .build()
                intent.launchUrl(context!!, it.toUri())
            }
        })

        filterViewModel.eFilter.observe(viewLifecycleOwner, Observer {
            it?.let {
                mainViewModel.applyFilter(it)
            }
        })
    }

    private fun initViews() {
//        binding.categoryFilterContainer.setOnClickListener {
//            showCategoryList()
//        }
//        binding.sourceFilterContainer.setOnClickListener {
//            showSourceList()
//        }
//
//        binding.includeToolbar.toolbarSearch.setOnEditorActionListener { v, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                resetRvScrollListener()
//                viewModel.setKeyword(v.text.toString())
//            }
//            false
//        }
//        binding.includeToolbar.toolbarClose.setOnClickListener {
//            resetRvScrollListener()
//            binding.includeToolbar.toolbarSearch.setText("")
//            viewModel.resetSearch()
//        }

        binding.errorWarningContainer.setOnClickListener {
            mainViewModel.loadNextPage()
        }

        binding.rvUserList.layoutManager = LinearLayoutManager(context)
        binding.rvUserList.adapter = adapter

        resetRvScrollListener()
    }

    private fun resetRvScrollListener() {
        binding.rvUserList.clearOnScrollListeners()
        binding.rvUserList.addOnScrollListener(object: InfiniteScroll(){
            override fun onLoadMore() {
                mainViewModel.loadNextPage()
            }
        })
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mainViewModel
    }
}
