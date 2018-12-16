package com.braisgabin.showmedamoney.presentation.confirmation

import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.braisgabin.showmedamoney.commons.extensions.toArrayList
import com.braisgabin.showmedamoney.di.DaggerActivity
import com.braisgabin.showmedamoney.entities.Contact
import com.braisgabin.showmedamoney.presentation.ContactDTO
import java.math.BigDecimal

class ConfirmationFragment : Fragment() {

  private lateinit var presenter: ConfirmationPresenter

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

  private fun render(state: ConfirmationState) {

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
