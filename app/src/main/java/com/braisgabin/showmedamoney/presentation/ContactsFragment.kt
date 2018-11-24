package com.braisgabin.showmedamoney.presentation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.braisgabin.showmedamoney.R
import com.braisgabin.showmedamoney.commons.extensions.allGoneExcept
import com.braisgabin.showmedamoney.commons.extensions.exhaustive
import com.braisgabin.showmedamoney.component
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import kotterknife.bindView
import kotterknife.bindViews
import javax.inject.Inject

class ContactsFragment : Fragment(), ContactsView {

  override val events: Relay<ContactsEvent> = PublishRelay.create()

  private val adapterListener = object : ContactsAdapter.Listener {
    override fun onClick(contact: SelectedContact) {
      events.accept(ContactsEvent.ClickContact(contact))
    }
  }

  @Inject
  internal lateinit var presenter: ContactsPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity!!.application.component
        .contactsBuilder()
        .withView(this)
        .withLifecycle(lifecycle)
        .build()
        .inject(this)

    presenter.start()
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
      events.accept(ContactsEvent.Retry)
    }

    fab.setOnClickListener {
      events.accept(ContactsEvent.NextStepClick)
    }
  }

  override fun render(state: ContactsState) {
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
}
