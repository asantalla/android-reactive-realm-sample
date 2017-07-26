package co.develoop.android_reactive_realm_sample.domain

import android.os.HandlerThread
import co.develoop.android_reactive_realm_sample.screen.MainActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.realm.Realm

class BookChangeObservable : Observable<Book>() {

    lateinit var bookToObserve: Book

    override fun subscribeActual(observer: Observer<in Book>?) {
        object : HandlerThread("realm") {
            override fun onLooperPrepared() {
                super.onLooperPrepared()

                val realm = Realm.getDefaultInstance()

                bookToObserve = realm.where(Book::class.java).equalTo("title", MainActivity.bookTitle).findFirstAsync()

                bookToObserve.addChangeListener<Book> { book, _ ->
                    observer!!.onNext(realm.copyFromRealm(book))
                }
            }
        }.start()
    }
}