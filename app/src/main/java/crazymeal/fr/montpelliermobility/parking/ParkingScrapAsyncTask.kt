package crazymeal.fr.montpelliermobility.parking

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class ParkingScrapAsyncTask(parkingFragment: ParkingFragment) : AsyncTask<String, Int, Parking?>() {

    val parkingFragment = parkingFragment

    override fun doInBackground(vararg url: String?): Parking? {
        val (request, response, result) = url[0]!!.httpGet().responseObject(Parking.Deserializer())

        return when (response.statusCode) {
            200 -> {
                Log.i("DOWNLOAD_ASYNC", "Got 200 response code for URL > " + url[0])
                result.component1()
            }
            else -> {
                Log.i("DOWNLOAD_ASYNC", "Failed to get response (code: " + response.statusCode + ") for URL > " + url[0])
                null
            }
        }
    }

    override fun onPostExecute(result: Parking?) {
        Log.i("DOWNLOAD_ASYNC", "Notifying the fragment with parking > " + result!!)
        parkingFragment.notifyAdapter(result)
        super.onPostExecute(result)
    }
}