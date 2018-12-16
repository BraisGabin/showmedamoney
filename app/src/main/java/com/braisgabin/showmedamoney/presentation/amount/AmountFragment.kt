package com.braisgabin.showmedamoney.presentation.amount

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.braisgabin.showmedamoney.R
import com.braisgabin.showmedamoney.di.DaggerActivity
import com.braisgabin.showmedamoney.entities.Contact
import com.braisgabin.showmedamoney.presentation.ContactDTO
import kotterknife.bindView
import javax.inject.Inject
import javax.inject.Named

class AmountFragment : Fragment() {

  private val amountEditText: EditText by bindView(R.id.editText)
  private val fab: View by bindView(R.id.fab)

  private lateinit var presenter: AmountPresenter

  @Inject
  @field:Named("decimalSeparator")
  internal lateinit var decimalSeparator: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val component = (activity as DaggerActivity).activityComponent


    presenter = ViewModelProviders.of(this, object : ViewModelProvider.Factory {

      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return (activity as DaggerActivity).activityComponent.amountPresenter() as T
      }
    }).get(AmountPresenter::class.java)

    component
        .inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_amount, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    amountEditText.filters = arrayOf(AmountFilter(1_000L, decimalSeparator))
  }

  companion object {
    fun create(contacts: List<Contact>): AmountFragment {
      return AmountFragment().apply {
        arguments = Bundle().apply {
          putParcelableArray(CONTACTS, contacts.map { ContactDTO(it) }.toTypedArray())
        }
      }
    }
  }
}

private const val CONTACTS = "contacts"
