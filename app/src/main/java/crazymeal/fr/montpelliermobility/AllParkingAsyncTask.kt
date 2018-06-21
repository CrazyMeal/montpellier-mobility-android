package crazymeal.fr.montpelliermobility

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class AllParkingAsyncTask() : AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg urls: String?): String {
        var resultToReturn = ""
        if (!this.isCancelled && urls.isNotEmpty()) {
            val urlString = urls[0]

            urlString!!.httpGet().responseString { request, response, result ->
                resultToReturn = when(result) {
                    is Result.Failure -> {
                        result.getException().toString()
                    }
                    is Result.Success -> {
                        result.get()
                    }
                }

                Log.i("DOWNLOAD_RESULT", "Downloaded some stuff > " + resultToReturn)
            }
        }

        return resultToReturn;
    }

}