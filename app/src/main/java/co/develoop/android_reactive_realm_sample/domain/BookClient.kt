package co.develoop.android_reactive_realm_sample.domain

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration

class BookClient {

    companion object {

        fun initDatabase(context: Context) {
            Realm.init(context)

            val configuration = RealmConfiguration.Builder().build()

            Realm.setDefaultConfiguration(configuration)
        }

        fun createOrUpdate(book: Book): Completable {
            return Completable.create {
                val realm = Realm.getDefaultInstance()

                try {
                    realm.beginTransaction()
                    realm.copyToRealmOrUpdate(book)
                    realm.commitTransaction()
                } catch (exception: Exception) {
                    realm.cancelTransaction()
                } finally {
                    realm.close()
                }
            }
        }

        fun get(title: String): Observable<Book> {
            return Observable.create<Book> { observer ->
                val realm = Realm.getDefaultInstance()

                val result = realm.where(Book::class.java).equalTo("title", title).findFirst()

                observer.onNext(realm.copyFromRealm(result))
            }
        }

        fun observe(title: String): Observable<Book> {
            return Observable.create<Book> { observer ->
                object : HandlerThread("realm") {

                    override fun onLooperPrepared() {
                        super.onLooperPrepared()

                        val realm = Realm.getDefaultInstance()
                        val handler = Handler()
                        handler.post {
                            realm.where(Book::class.java).equalTo("title", title).findFirst().addChangeListener<Book> { book, _ ->
                                observer.onNext(realm.copyFromRealm(book))
                            }
                        }
                    }
                }.start()
            }
        }
    }
}