package com.example.dropbox_base.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.example.dropbox_base.R
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.*


class DropboxServices(private val mDropContext: Context) {

    companion object {
        private lateinit var instance: DropboxServices

        fun getInstance(context: Context): DropboxServices {
            if (!::instance.isInitialized) {
                instance = DropboxServices(context)
            }
         //   AppConstants
            return instance
        }
    }

    private val fileName = "content.json"

    private lateinit var clientV2: DbxClientV2
    private var config: DbxRequestConfig = DbxRequestConfig.newBuilder("ModsofValheim").build()

    private var mOnJsonResult: OnJsonResult? = null

    //Initialize first in application class of main module
    init {
        initializeClient()
    }

    private fun initializeClient() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accessToken =
                    getAccessToken(mDropContext.resources.getString(R.string.REFRESH_TOKEN))
                if (accessToken != null) {
                    clientV2 = DbxClientV2(config, accessToken)
                }
                PRDownloader.initialize(mDropContext)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun isAccessTokenValid(accessToken: String): Boolean {
        return try {
            clientV2.users().currentAccount // if successful, access token is valid
            true
        } catch (e: Exception) {
            false // access token is invalid or has expired
        }
    }

    private suspend fun getAccessToken(refreshToken: String) =
        CoroutineScope(Dispatchers.IO).async {
            var accessToken: String? = null
            val client = OkHttpClient()
            val requestBody = FormBody.Builder().add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .add("client_id", mDropContext.resources.getString(R.string.APP_KEY_DROPBOX)).add(
                    "client_secret", mDropContext.resources.getString(R.string.APP_SECRET_DROPBOX)
                ).build()
            val request =
                Request.Builder().url("https://api.dropbox.com/oauth2/token").post(requestBody)
                    .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                val json = JSONObject(responseData)
                accessToken = json.getString("access_token")
                accessToken
                println("New access token: $accessToken")
            } else {
                println("Error getting new access token: ${response.code} ${response.message}")
                accessToken
            }
            accessToken
        }.await()

    fun getJsonResponse(onJsonResult: OnJsonResult) {
        mOnJsonResult = onJsonResult
    }

    fun loadFileDataDrop(jsonFilePathDropbox: String, cacheDirPath: File) {
        val savedFilePath = File(cacheDirPath, fileName)
        //For Read
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accessToken =
                    getAccessToken(mDropContext.resources.getString(R.string.REFRESH_TOKEN))
                if (accessToken != null) {
                    clientV2 = DbxClientV2(config, accessToken)
                    isAccessTokenValid(accessToken)
                }

                if (savedFilePath.exists()) {
                    withContext(Dispatchers.Main) {
                        mOnJsonResult?.getJsonResult(savedFilePath.absolutePath)
                    }
                } else {
                    val dbxFile = clientV2.files().getTemporaryLink(jsonFilePathDropbox)
                    Log.e("APPCAKE", dbxFile.link)

                    withContext(Dispatchers.Main) {
                        downloadFileAndSave(dbxFile.link, cacheDirPath)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun downloadFileAndSave(url: String, cacheDirPath: File) {
        val savedFilePath = File(cacheDirPath, fileName)

        if (!cacheDirPath.exists()) {
            cacheDirPath.mkdirs()
        }

        PRDownloader.download(url, cacheDirPath.absolutePath, fileName).build()
            .setOnStartOrResumeListener {}.setOnPauseListener { }.setOnCancelListener { }
            .setOnProgressListener {}.start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    mOnJsonResult?.getJsonResult(savedFilePath.absolutePath)
                }

                override fun onError(error: com.downloader.Error?) {
                    mOnJsonResult?.getJsonError()
                }
            })
    }

    @Throws(FileNotFoundException::class)
    fun <T> getJsonRes(fileName: String?, type: Class<T>?): T {
        val gson = GsonBuilder().create()
        val json = FileReader(fileName)
        return gson.fromJson(json, type)
    }

    suspend fun loadImageToView(fileName: String, fileDir: String) =
        CoroutineScope(Dispatchers.IO).async {
            try {
                val accessToken =
                    getAccessToken(mDropContext.resources.getString(R.string.REFRESH_TOKEN))
                if (accessToken != null) {
                    clientV2 = DbxClientV2(config, accessToken)
                    isAccessTokenValid(accessToken)
                }

                val dbxFile = clientV2.files().getTemporaryLink("$fileDir$fileName")
                return@async dbxFile.link
            } catch (e: Exception) {
                e.printStackTrace()
                return@async ""
            }
        }.await()

    suspend fun loadImageToView1(fileNameDropbox: String) =
        CoroutineScope(Dispatchers.IO).async {
            try {
                val accessToken =
                    getAccessToken(mDropContext.resources.getString(R.string.REFRESH_TOKEN))
                if (accessToken != null) {
                    clientV2 = DbxClientV2(config, accessToken)
                    isAccessTokenValid(accessToken)
                }

                Log.e("APPCAKE", "loadImageToView:/$fileNameDropbox")
                val dbxFile = clientV2.files().getTemporaryLink("/$fileNameDropbox")
                Log.e("APPCAKE", "loadImageToView Link:::${dbxFile.link}")

                return@async dbxFile.link
            } catch (e: Exception) {
                e.printStackTrace()
                return@async ""
            }
        }.await()

    interface OnJsonResult {
        fun getJsonResult(jsonFilePath: String)
        fun getJsonError()
    }
}