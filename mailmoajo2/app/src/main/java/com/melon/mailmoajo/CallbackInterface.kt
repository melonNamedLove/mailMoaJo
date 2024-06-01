package com.melon.mailmoajo

import com.melon.mailmoajo.dataclass.gotOutlookMail

interface CallbackInterface {
    fun onMailDataReceived(data: gotOutlookMail)
    fun onMailDataError(error: Throwable)
}