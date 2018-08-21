package crazymeal.fr.montpelliermobility.parking

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.httpGet

class ParkingScrapAsyncTask(private val parkingFragment: ParkingFragment) : AsyncTask<Array<Any>, Int, Parking?>() {

    override fun doInBackground(vararg taskArguments: Array<Any>?): Parking? {
        val url = taskArguments[0]!![0] as String
        val parkingName = taskArguments[0]!![1] as String
        val latitude = taskArguments[0]!![2] as Double
        val longitude = taskArguments[0]!![3] as Double

        try {
            val (request, response, result) = url.httpGet().responseObject(Parking.Deserializer())

            return when (response.statusCode) {
                200 -> {
                    Log.i("DOWNLOAD_ASYNC", "Got 200 response code for URL > $url")
                    val parking = result.component1() as Parking
                    parking.name = parkingName
                    parking.longitude = longitude
                    parking.latitude = latitude
                    parking
                }
                else -> {
                    Log.i("DOWNLOAD_ASYNC", "Failed to get response (code: " + response.statusCode + ") for URL > " + url)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("DOWNLOAD_ASYNC", "An exception occured during AsyncTask", e)
            return null
        }

    }

    override fun onPostExecute(result: Parking?) {
        result?.let {
            Log.i("DOWNLOAD_ASYNC", "Notifying the fragment with parking > $result")
            parkingFragment.notifyAdapter(result)
        }

        super.onPostExecute(result)
    }
}