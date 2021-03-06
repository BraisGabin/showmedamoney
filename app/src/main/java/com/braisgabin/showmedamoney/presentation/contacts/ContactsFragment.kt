package com.braisgabin.showmedamoney.presentation.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.braisgabin.showmedamoney.R
import com.braisgabin.showmedamoney.commons.extensions.allGoneExcept
import com.braisgabin.showmedamoney.commons.extensions.exhaustive
import com.braisgabin.showmedamoney.di.DaggerActivity
import kotterknife.KotterKnife
import kotterknife.bindView
import kotterknife.bindViews

class ContactsFragment : Fragment() {

  private val adapterListener = object : ContactsAdapter.Listener {
    override fun onClick(contact: SelectedContact) {
      presenter.events.accept(ContactsEvent.ClickContact(contact))
    }
  }

  private lateinit var presenter: ContactsPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    presenter = ViewModelProviders.of(this, object : ViewModelProvider.Factory {

      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return (activity as DaggerActivity).activityComponent.contactsPresenter() as T
      }
    }).get(ContactsPresenter::class.java)

    LiveDataReactiveStreams.fromPublisher(presenter.states)
        .observe(this, Observer<ContactsState> { state -> render(state!!) })
  }

  private val progressView: View by bindView(R.id.progressView)
  private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
  private val retryView: View by bindView(R.id.retryView)
  private val fab: View by bindView(R.id.fab)
  private val views: List<View> by bindViews(R.id.progressView, R.id.recyclerView, R.id.retryView, R.id.fab)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_contacts, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerView.layoutManager = LinearLayoutManager(context)

    retryView.setOnClickListener {
      presenter.events.accept(ContactsEvent.Retry)
    }

    fab.setOnClickListener {
      presenter.events.accept(ContactsEvent.NextStepClick)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    KotterKnife.reset(this)
  }

  override fun onResume() {
    super.onResume()
    if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 0)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    presenter.events.accept(ContactsEvent.Retry)
  }

  private fun render(state: ContactsState) {
    when (state) {
      ContactsState.Progress -> views.allGoneExcept(progressView)
      ContactsState.Retry -> views.allGoneExcept(retryView)
      is ContactsState.Data -> {
        if (state.allowNextStep) {
          views.allGoneExcept(recyclerView, fab)
        } else {
          views.allGoneExcept(recyclerView)
        }
        val adapter = recyclerView.adapter as ContactsAdapter? ?: ContactsAdapter(adapterListener)
        adapter.submitList(state.contacts)
        if (recyclerView.adapter == null) {
          recyclerView.adapter = adapter
        }
        Unit
      }
    }.exhaustive
  }

  companion object {
    fun create(): ContactsFragment = ContactsFragment()
  }
}
