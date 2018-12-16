package com.braisgabin.showmedamoney.presentation.confirmation

import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.braisgabin.showmedamoney.R
import com.braisgabin.showmedamoney.commons.extensions.toArrayList
import com.braisgabin.showmedamoney.di.DaggerActivity
import com.braisgabin.showmedamoney.entities.Contact
import com.braisgabin.showmedamoney.presentation.ContactDTO
import kotterknife.KotterKnife
import kotterknife.bindView
import java.math.BigDecimal
import java.text.NumberFormat

class ConfirmationFragment : Fragment() {

  private lateinit var presenter: ConfirmationPresenter

  private val recyclerView: RecyclerView by bindView(R.id.recyclerView)
  private val textView: TextView by bindView(R.id.textView)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val amount = BigDecimal(arguments!!.getString(AMOUNT))
    val contacts = arguments!!.getParcelableArrayList<ContactDTO>(CONTACTS)!!.map { it.toDomain() }

    val component = (activity as DaggerActivity).activityComponent
        .confirmationBuilder()
        .with(amount)
        .with(contacts)
        .build()

    presenter = ViewModelProviders.of(this, object : ViewModelProvider.Factory {

      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return component.presenter() as T
      }
    }).get(ConfirmationPresenter::class.java)


    LiveDataReactiveStreams.fromPublisher(presenter.states)
        .observe(this, Observer<ConfirmationState> { state -> render(state!!) })
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_confirmation, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerView.layoutManager = LinearLayoutManager(context)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    KotterKnife.reset(this)
  }

  private fun render(state: ConfirmationState) {
    val adapter = recyclerView.adapter as ConfirmationAdapter? ?: ConfirmationAdapter()
    adapter.submitList(state.contacts)
    if (recyclerView.adapter == null) {
      recyclerView.adapter = adapter
    }
    textView.text = NumberFormat.getCurrencyInstance().format(state.amount)
  }

  companion object {
    fun create(amount: BigDecimal, contacts: List<Contact>): ConfirmationFragment {
      return ConfirmationFragment().apply {
        arguments = Bundle().apply {
          putString(AMOUNT, amount.toString())
          putParcelableArrayList(CONTACTS, contacts.map { ContactDTO(it) }.toArrayList())
        }
      }
    }
  }
}

private const val AMOUNT = "amount"
private const val CONTACTS = "contacts"
