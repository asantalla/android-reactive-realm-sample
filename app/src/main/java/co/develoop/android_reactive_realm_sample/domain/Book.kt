package co.develoop.android_reactive_realm_sample.domain

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Book(@PrimaryKey var title: String, var author: String, var favorite: Boolean) : RealmObject() {

    constructor() : this("", "", false)
}

