package me.jaxvy.kotlinboilerplate.ui.item

import android.content.Context
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.fragment_create_item.*
import me.jaxvy.kotlinboilerplate.R
import me.jaxvy.kotlinboilerplate.ui.common.BaseFragment
import me.jaxvy.kotlinboilerplate.ui.home.HomeActivityCallback

class CreateItemFragment : BaseFragment() {

    companion object {
        const val TAG = "CreateItemFragment"
    }

    private lateinit var activityCallback: HomeActivityCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityCallback = context as HomeActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar(toolbar, R.string.CreateItemFragment_toolbar, true)
        updateSaveButton()
        save.setOnClickListener { saveItem() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // I need to re-setup the toolbar here so that it will override the toolbar update
        // in HomeActivity
        setupToolbar(toolbar, R.string.CreateItemFragment_toolbar, true)
        menu.clear()
    }

    private fun saveItem() {
        val titleText = title.text.toString()
        val descriptionText = description.text.toString()
        if (!titleText.isEmpty() && !descriptionText.isEmpty()) {
            activityCallback.createNewItem(titleText, descriptionText)
            updateSaveButton()
            hideKeyboard()
        }
    }

    fun updateSaveButton() {
        if (activityCallback.isCreatingItem()) {
            save.text = "Saving..."
            save.setText(R.string.CreateItemFragment_save_button_in_progress)
            save.isEnabled = false
        } else {
            save.setText(R.string.CreateItemFragment_save_button_default)
            save.isEnabled = true
        }
    }
}