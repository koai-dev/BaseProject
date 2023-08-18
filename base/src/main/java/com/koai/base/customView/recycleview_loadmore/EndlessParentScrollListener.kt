package com.koai.base.customView.recycleview_loadmore

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessParentScrollListener(layoutManager: RecyclerView.LayoutManager) :
    NestedScrollView.OnScrollChangeListener {
    // The current offset index of data you have loaded
    private var currentPage = 0

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private val startingPageIndex = 0

    // The minimum amount of pixels to have below your current scroll position
    // before loading more.
    private val visibleThresholdDistance = 300
    var mLayoutManager: RecyclerView.LayoutManager
    override fun onScrollChange(scrollView: NestedScrollView, x: Int, y: Int, oldx: Int, oldy: Int) {
        // We take the last son in the scrollview
        val view: View = scrollView.getChildAt(scrollView.childCount - 1)
        val distanceToEnd: Int =
            view.bottom - (scrollView.height + scrollView.scrollY)
        val totalItemCount: Int = mLayoutManager.itemCount
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && distanceToEnd <= visibleThresholdDistance) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
            loading = true
        }
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    init {
        mLayoutManager = layoutManager
    }
}