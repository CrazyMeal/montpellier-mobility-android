package crazymeal.fr.montpelliermobility.parking

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.httpGet

class ParkingScrapAsyncTask(parkingFragment: ParkingFragment) : AsyncTask<String, Int, Parking?>() {

    val parkingFragment = parkingFragment

    override fun doInBackground(vararg urls: String?): Parking? {
        var resultToReturn: Parking? = null

        val urlString = "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_MOSS.xml"
//        urlString!!.httpGet().responseObject(Parking.Deserializer()) { request, response, result ->
//            when(result) {
//                is Result.Failure -> {
//                    Log.e("DOWNLOAD_ASYNC", result.getException().toString())
//                }
//                is Result.Success -> {
//                    Log.i("DOWNLOAD_ASYNC", "Got some parking > " + result.component1().toString())
//                    resultToReturn = result.component1()
//                }
//            }
//        }

        val (request, response, result) = urlString!!.httpGet().responseObject(Parking.Deserializer())

        return result.component1();
    }

    override fun onPostExecute(result: Parking?) {
        Log.i("DOWNLOAD_ASYNC", "Notifying the fragment with parking > " + result)
        parkingFragment.notifyAdapter(result!!)

        super.onPostExecute(result)
    }
}