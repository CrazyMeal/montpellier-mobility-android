package crazymeal.fr.montpelliermobility

import android.os.AsyncTask
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class ParkingScrapAsyncTask(parkingFragment: ParkingFragment) : AsyncTask<String, Int, Parking?>() {

    val parkingFragment = parkingFragment

    override fun doInBackground(vararg urls: String?): Parking? {
        var resultToReturn: Parking? = null

        val urlString = "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_MOSS.xml"
        urlString!!.httpGet().responseObject(Parking.Deserializer()) { request, response, result ->
            when(result) {
                is Result.Failure -> {
                    Log.e("DOWNLOAD_ASYNC", result.getException().toString())
                }
                is Result.Success -> {
                    Log.i("DOWNLOAD_ASYNC", "Got some parking > " + result.component1().toString())
                    resultToReturn = result.component1()
                }
            }
        }

        return resultToReturn;
    }

    override fun onPostExecute(result: Parking?) {
        parkingFragment.notifyAdapter(result)

        super.onPostExecute(result)
    }
}