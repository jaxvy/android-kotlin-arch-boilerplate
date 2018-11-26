package me.jaxvy.kotlinboilerplate.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupToolbar(toolbar, R.string.HomeActivity_toolbar)

        val itemAdapter = ItemAdapter { item ->
            Log.d("Item clicked: ", "${item.title} ${item.description}")
        }
        itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            addItemDecoration(
                    DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL)
            )
            adapter = itemAdapter
        }

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.items.observe(
                this,
                Observer<List<Item>> { items ->
                    itemAdapter.setItems(items)
                }
        )
        viewModel.registerCreateItemHandlers(
                onSuccess = { onCreateItemSuccessful() },
                onError = { throwable -> onCreateItemError(throwable) })

        newItemFloatingActionButton.setOnClickListener { showCreateItemFragment() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean = consume {
        menuInflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.signout -> consume { onLogout() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun onLogout() {
        viewModel.logout()
        finish()
        startActivity<LoginActivity>()
    }

    private fun showCreateItemFragment() {
        with(supportFragmentManager.beginTransaction()) {
            setCustomAnimations(
                    R.anim.slide_in_up,
                    R.anim.slide_in_down,
                    R.anim.slide_out_down,
                    R.anim.slide_out_up
            )
            replace(
                    android.R.id.content,
                    CreateItemFragment(),
                    CreateItemFragment.TAG
            )
            addToBackStack(CreateItemFragment.TAG)
            commitAllowingStateLoss()
        }
    }

    override fun createNewItem(title: String, description: String) {
        viewModel.createNewItem(title, description)
    }

    private fun onCreateItemSuccessful() {
        supportFragmentManager.findFragmentByTag(CreateItemFragment.TAG)
                ?.run {
                    supportFragmentManager.popBackStack()
                    viewModel.fetchItems()
                    Handler().postDelayed({ invalidateOptionsMenu() }, 1000)
                }
    }

    private fun onCreateItemError(throwable: Throwable) {
        updateCreateItemFragmentOnError()
        AlertDialog.Builder(this)
                .pop(R.string.HomeActivity_create_item_error_dialog_title, throwable.message) {
                    dismissOnOk()
                }
    }

    private fun updateCreateItemFragmentOnError() {
        (supportFragmentManager.findFragmentByTag(CreateItemFragment.TAG) as? CreateItemFragment)
                ?.takeIf { it.isAdded }
                ?.run { updateSaveButton() }
    }

    override fun isCreatingItem() = viewModel.isCreatingItem
}
