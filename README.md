# Android Reactive Realm Sample

The aim of this project is to provide a simple example on how to observe a Realm entity and auto-update some view/s.

Contents
--------

- [Dependencies](#dependencies)
- [Limitations](#limitations)
- [How to auto-update a view observing Realm entity changes](#how-to-auto-update-a-view-observing-realm-entity-changes)
- [License](#license)

Dependencies
------------

- Kotlin: 1.1.3-2
- RxKotlin: 2.1.0
- RxAndroid: 2.0.1
- Realm: 3.5.0

Limitations
-----------

Due to a Realm dependency with RxJava 1, if you use RxJava 2 or RxKotlin 2 you can't use the default methods to convert queries to Observables provided by the Realm library.

You can see more information in [this Realm project issue ticket](https://github.com/realm/realm-java/issues/3497).

How to auto-update a view observing Realm entity changes
--------------------------------------------------------

This a common functionality of many Android applications. For example, if you have a *functionality to mark some entity as favorite* you can achieve this using this technique.

Due to the limitation above commented, we have to create an **Observable** manually and add the Realm entity change listener on it. For example:

```kotlin
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
```

Now, we can subscribe to the above **Observable** and updates the view every time the Realm entity is updated.

License
-------

Copyright 2017 Adrián Santalla

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
