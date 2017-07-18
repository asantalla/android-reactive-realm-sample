package co.develoop.android_reactive_realm_sample.screen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.develoop.android_reactive_realm_sample.R
import co.develoop.android_reactive_realm_sample.domain.Book
import co.develoop.android_reactive_realm_sample.domain.BookClient
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    companion object {
        val bookTitle: String = "Clean Architecture: A Craftsman's Guide to Software Structure and Design"
        val bookAuthor: String = "Robert C. Martin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        BookClient.initDatabase(this)

        BookClient.createOrUpdate(Book(MainActivity.bookTitle, MainActivity.bookAuthor, false))
                .subscribeOn(Schedulers.newThread())
                .subscribe()

        fragmentManager.beginTransaction().add(R.id.actionContainer, ActionFragment.newInstance()).commit()
    }
}