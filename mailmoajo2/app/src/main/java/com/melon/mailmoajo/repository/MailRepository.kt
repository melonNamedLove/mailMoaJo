package com.melon.mailmoajo.repository

import com.melon.mailmoajo.DAOs.mailDao
import entities.mails

class MailRepository(private val mailDao:mailDao) {

    fun getAllMailsOrderedByTime(position:Int): List<mails> {
        return mailDao.getAllMailsOrderedByTime(position)
    }
}