package crazymeal.fr.montpelliermobility.parking

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.httpGet

class ParkingScrapAsyncTask(parkingFragment: ParkingFragment) : AsyncTask<String, Int, Parking?>() {

    private val parkingFragment = parkingFragment

    override fun doInBackground(vararg taskArguments: String?): Parking? {
        try {
            val (request, response, result) = taskArguments[1]!!.httpGet().responseObject(Parking.Deserializer())

            return when (response.statusCode) {
                200 -> {
                    Log.i("DOWNLOAD_ASYNC", "Got 200 response code for URL > " + taskArguments[1])
                    val parking = result.component1() as Parking
                    parking.name = taskArguments[0]
                    parking
                }
                else -> {
                    Log.i("DOWNLOAD_ASYNC", "Failed to get response (code: " + response.statusCode + ") for URL > " + taskArguments[0])
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