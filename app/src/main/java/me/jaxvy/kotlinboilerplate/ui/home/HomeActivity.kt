package me.jaxvy.kotlinboilerplate.ui.home

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*
import me.jaxvy.kotlinboilerplate.R
import me.jaxvy.kotlinboilerplate.persistence.entity.Item
import me.jaxvy.kotlinboilerplate.ui.common.AuthenticatedActivity
import me.jaxvy.kotlinboilerplate.ui.item.CreateItemFragment
import me.jaxvy.kotlinboilerplate.ui.login.LoginActivity
import me.jaxvy.kotlinboilerplate.utils.consume
import me.jaxvy.kotlinboilerplate.utils.dismissOnOk
import me.jaxvy.kotlinboilerplate.utils.pop
import me.jaxvy.kotlinboilerplate.utils.startActivity

class HomeActivity : AuthenticatedActivity(), HomeActivityCallback {

    lateinit private var adapter: ItemAdapter

    lateinit private var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupToolbar(toolbar, R.string.HomeActivity_toolbar)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.items.observe(this, Observer<List<Item>> {
            items ->
            adapter.setItems(items!!)
        })
        viewModel.registerCreateItemHandlers(
                onSuccess = { onCreateItemSuccessful() },
                onError = { throwable -> onCreateItemError(throwable) })

        newItemFloatingActionButton.setOnClickListener { showCreateItemFragment() }

        adapter = ItemAdapter {
            item ->
            Log.d("Item clicked: ", "${item.title} ${item.description}")
        }
        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        itemRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        itemRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean = consume {
        /**
         * I need to re-setup the toolbar here otherwise if the orientation changes during
         * item creation, HomeActivity's toolbar does not show the menu.
         * I suspect this is being caused by the fact that currently LifecycleActivity is
         * extended from FragmentActivity. Once AppCompatActivity supports lifecycle, it will
         * hopefully fix this problem
         */
        setupToolbar(toolbar, R.string.HomeActivity_toolbar)
        menuInflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.signout -> consume { onLogout() }
        else -> super.onOptionsItemSelected(item)
    }

    fun onLogout() {
        viewModel.logout()
        finish()
        startActivity<LoginActivity>()
    }

    fun showCreateItemFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,
                R.anim.slide_in_down,
                R.anim.slide_out_down,
                R.anim.slide_out_up);
        fragmentTransaction.replace(android.R.id.content,
                CreateItemFragment(),
                CreateItemFragment.TAG)
        fragmentTransaction.addToBackStack(CreateItemFragment.TAG)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun createNewItem(title: String, description: String) {
        viewModel.createNewItem(title, description)
    }

    fun onCreateItemSuccessful() {
        if (supportFragmentManager.findFragmentByTag(CreateItemFragment.TAG) != null) {
            supportFragmentManager.popBackStack()
            viewModel.fetchItems()
            Handler().postDelayed({ invalidateOptionsMenu() }, 1000)
        }
    }

    fun onCreateItemError(throwable: Throwable) {
        updateCreateItemFragmentOnError()
        AlertDialog.Builder(this).pop(R.string.HomeActivity_create_item_error_dialog_title, throwable.message) {
            dismissOnOk()
        }
    }

    private fun updateCreateItemFragmentOnError() {
        val createItemFragment = supportFragmentManager.findFragmentByTag(CreateItemFragment.TAG)
        if (createItemFragment != null && createItemFragment.isAdded) {
            (createItemFragment as CreateItemFragment).updateSaveButton()
        }
    }

    override fun isCreatingItem() = viewModel.isCreatingItem
}
