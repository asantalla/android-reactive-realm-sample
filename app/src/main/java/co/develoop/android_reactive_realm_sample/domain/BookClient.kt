package co.develoop.android_reactive_realm_sample.domain

import android.content.Context
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
            return Completable.create { observer ->
                val realm = Realm.getDefaultInstance()

                realm.executeTransactionAsync { realmInstance ->
                    try {
                        realmInstance.copyToRealmOrUpdate(book)
                        realmInstance.commitTransaction()

                    } catch (exception: Exception) {
                        realmInstance.cancelTransaction()
                    } finally {
                        realmInstance.close()
                        observer.onComplete()
                    }
                }
            }
        }

        fun get(title: String): Observable<Book> {
            return Observable.create<Book> { observer ->
                val realm = Realm.getDefaultInstance()

                realm.executeTransactionAsync { realmInstance ->
                    val result = realmInstance.where(Book::class.java).equalTo("title", title).findFirst()

                    observer.onNext(realmInstance.copyFromRealm(result))

                    realmInstance.close()
                }
            }
        }

        fun observe(): Observable<Book>? {
            return BookChangeObservable()
        }
    }
}