package co.develoop.android_reactive_realm_sample.screen

import android.app.Fragment
import android.os.Bundle
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.develoop.android_reactive_realm_sample.R
import co.develoop.android_reactive_realm_sample.domain.Book
import co.develoop.android_reactive_realm_sample.domain.BookClient
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_action.*


class ActionFragment : Fragment() {

    companion object {
        fun newInstance() = ActionFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View = LayoutInflater.from(context).inflate(R.layout.fragment_action, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BookClient.get(MainActivity.bookTitle)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { book -> updateView(book) }

        BookClient.observe()!!
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { book -> updateView(book) }

        RxView.clicks(fragmentActionFavoriteButton)
                .flatMap { BookClient.get(MainActivity.bookTitle) }
                .flatMapCompletable { book ->
                    book.favorite = !book.favorite
                    BookClient.createOrUpdate(book)
                }
                .subscribe()
    }

    private fun updateView(book: Book) {
        fragmentActionBookTitle.text = book.title
        fragmentActionBookAuthor.text = book.author
        fragmentActionBookIsFavorite.text = book.favorite.toString()
    }
}