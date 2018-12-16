package com.braisgabin.showmedamoney.presentation.amount

import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.braisgabin.showmedamoney.R
import com.braisgabin.showmedamoney.di.DaggerActivity
import com.braisgabin.showmedamoney.entities.Contact
import com.braisgabin.showmedamoney.presentation.ContactDTO
import kotterknife.KotterKnife
import kotterknife.bindView
import java.util.ArrayList
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


    LiveDataReactiveStreams.fromPublisher(presenter.states)
        .observe(this, Observer<AmountState> { state -> render(state!!) })
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_amount, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    amountEditText.filters = arrayOf(AmountFilter(1_000L, decimalSeparator))
    presenter.events.accept(AmountEvent.WriteAmount(amountEditText.text.toString()))
    amountEditText.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable) {
        presenter.events.accept(AmountEvent.WriteAmount(s.toString()))
      }

      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
      }

      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      }
    })

    fab.setOnClickListener { _ ->
      val contacts = arguments!!.getParcelableArrayList<ContactDTO>(CONTACTS)!!.map { it.toDomain() }
      presenter.events.accept(AmountEvent.ClickNextStep(amountEditText.text.toString(), contacts))
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    KotterKnife.reset(this)
  }

  private fun render(state: AmountState) {
    fab.visibility = if (state.allowNextStep) View.VISIBLE else View.GONE
  }

  companion object {
    fun create(contacts: List<Contact>): AmountFragment {
      return AmountFragment().apply {
        arguments = Bundle().apply {
          putParcelableArrayList(CONTACTS, contacts.map { ContactDTO(it) }.toArrayList())
        }
      }
    }
  }
}

private fun <E> Collection<E>.toArrayList(): ArrayList<E> {
  return ArrayList(this)
}

private const val CONTACTS = "contacts"
