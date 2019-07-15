package com.byted.camp.todolist.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.byted.camp.todolist.db.AppDataBase
import com.byted.camp.todolist.model.Note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    fun deleteNote(note: Note, onSuccess: (Int) -> Unit, onError: (Throwable) -> Unit){
        compositeDisposable.add(AppDataBase.getInstance(getApplication())
            .todoDao()
            .delete(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        )
    }

    fun updateNode(note: Note, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(AppDataBase.getInstance(getApplication())
            .todoDao()
            .update(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        )
    }

    fun getAllNodes(consumer: (List<Note>) -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(AppDataBase.getInstance(getApplication())
            .todoDao()
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(consumer, onError)
        )
    }

    fun insert(note: Note, onSuccess: (Long) -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(AppDataBase.getInstance(getApplication())
            .todoDao()
            .insert(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}