package com.braisgabin.showmedamoney.presentation.contacts

import android.Manifest
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.braisgabin.showmedamoney.R
import com.braisgabin.showmedamoney.commons.extensions.allGoneExcept
import com.braisgabin.showmedamoney.commons.extensions.exhaustive
import com.braisgabin.showmedamoney.di.DaggerActivity
import kotterknife.bindView
import kotterknife.bindViews
import javax.inject.Inject

class ContactsFragment : Fragment() {

  private val adapterListener = object : ContactsAdapter.Listener {
    override fun onClick(contact: SelectedContact) {
      presenter.events.accept(ContactsEvent.ClickContact(contact))
    }
  }

  @Inject
  internal lateinit var presenter: ContactsPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    (activity as DaggerActivity).activityComponent
        .inject(this)

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

  override fun onResume() {
    super.onResume()
    if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_CONTACTS), 0)
    }
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
