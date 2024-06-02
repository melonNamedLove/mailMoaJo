package com.melon.mailmoajo.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.melon.mailmoajo.Database.MailMoaJoDatabase
import com.melon.mailmoajo.repository.MailRepository
import entities.mails

class MailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MailRepository

    init {
        val mailDao = MailMoaJoDatabase.getDatabase(application).mailDao()
        repository = MailRepository(mailDao)
    }

    fun getAllMailsOrderedByTime(position:Int): List<mails>{
        return repository.getAllMailsOrderedByTime(position)


    }
}