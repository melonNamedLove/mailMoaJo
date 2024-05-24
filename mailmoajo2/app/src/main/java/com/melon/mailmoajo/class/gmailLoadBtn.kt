package com.melon.mailmoajo

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.melon.mailmoajo.dataclass.mailData
import com.melon.mailmoajo.dataclass.mailId
import com.melon.mailmoajo.fragment.mailmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.util.Base64


class gmailLoadBtn {
    /**
     * CODE - 1 : Int
     *
     * @param context requireContext() 사용할것
     */
    fun gmailLoadFun(context : Context) {
        var i: Intent = Intent(context, GmailLoadActivity::class.java)
        context.startActivity(i)
    }

    /**
     * CODE - 2
     *
     */
    fun retroFun () {
        var accesscode = GoogleSignInActivity.tokenprefs.getString("access_code","")
        accesscode = URLDecoder.decode(accesscode, "UTF-8")
        Log.i("meow",accesscode)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(AccessToken::class.java)

        service.postAccessToken(
            accesscode,
            "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
            "https://www.googleapis.com/auth/gmail.readonly",
            "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",
            "http://localhost:5500/test.html",
            "authorization_code").enqueue(object :Callback<PostResult>{
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {


                Log.d("meow",  response.code().toString())
                if(response.isSuccessful.not()){
                    Log.d("meow", "nope")
                    Log.d("meow", response.errorBody()?.string()!!)

                    return
                }
                Log.d("meow", response.body()?.access_token.toString())
                if (response.body()?.access_token !=null){
                    GoogleSignInActivity.tokenprefs.edit().putString("accesstoken", response.body()?.access_token).apply()
                    val tokenParts = response.body()!!.id_token.split(".")
                    Log.d("tokenmeow",tokenParts[0])
                    Log.d("tokenmeow",tokenParts[1])
                    Log.d("tokenmeow",tokenParts[2])

                    val decoder = Base64.getUrlDecoder()

                    val header = String(decoder.decode(tokenParts[0]))
                    val payload = String(decoder.decode(tokenParts[1]))
                    val signature = String(decoder.decode(tokenParts[2]))

                    Log.d("tokenmeow","header"+header)
                    Log.d("tokenmeow","payload"+payload)
                    Log.d("tokenmeow","signature"+signature)

                    //gson 인스턴스 생성
                    var gson = Gson()

                    val stringtodataclass = gson.fromJson(payload, payload_json::class.java)

                    Log.i("meow",stringtodataclass.email)
                    GoogleSignInActivity.tokenprefs.edit().putString("userid",stringtodataclass.email).apply()

                    //json을 클래스로 변환


                }
            }

            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                Log.d("meow", "Failed API call with call: " + call +
                        " + exception: " + t)
            }


        })

//            AccessToken.create().postAccessToken(
//                accesscode.toString(),
//                "1050701672933-0p8rutpvp8gtafrdqoj9akg2lnp1dcfc.apps.googleusercontent.com",
//                "https://www.googleapis.com/auth/gmail.readonly",
//                "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",
//                "http://localhost:5500/test.html",
//                "authorization_code"
//            ).enqueue(object : Callback<PostResult>{
//                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
//                    if(response.isSuccessful.not()){
//                        Log.d("meow", "nope")
//                        return
//                    }
//
//                        Log.d("meow", response.body()?.access_token.toString())
//
//                }
//
//                override fun onFailure(call: Call<PostResult>, t: Throwable) {
//                    Log.d("meow", "fail")
//                }
//            }
//
//            )
    }

    /**
     * CODE - 3
     *
     */
    fun retro2Fun () {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(AccessToken::class.java)

        val userid = GoogleSignInActivity.tokenprefs.getString("userid","").toString()
        service.getMailList(
            userid,
            "Bearer "+ GoogleSignInActivity.tokenprefs.getString("accesstoken","").toString(),
        ).enqueue(object : Callback<gotMailList> {
            override fun onResponse(call: Call<gotMailList>, response: Response<gotMailList>) {


                Log.d("meow",  response.code().toString())
                if(response.isSuccessful.not()){
                    Log.d("meow", "nope")
                    Log.d("meow", response.errorBody()?.string()!!)

                    return
                }
                val msg =response.body()?.messages
//                    msg?.get(0)
                Log.d("meow", response.body()?.messages.toString())
//
                var gson = Gson()
                for (i:Int in 0 until 50){
                    Log.w("meow", response.body()!!.messages[i].toString())
                    val stringtodataclass = gson.fromJson(response.body()!!.messages[i].toString(), mailData::class.java)
                    Log.d("meow", stringtodataclass.id)
                    mailmail.add(mailId(stringtodataclass.id))
//        listData.add(ItemData(R.drawable.img1,"정석현","01077585738", 1))
                }
//                    Log.i("meow",stringtodataclass.mailids.toString())
//                    item.messages[0]
            }

            override fun onFailure(call: Call<gotMailList>, t: Throwable) {
                Log.d("meow", "Failed API call with call: " + call +
                        " + exception: " + t)
            }


        })
    }



}