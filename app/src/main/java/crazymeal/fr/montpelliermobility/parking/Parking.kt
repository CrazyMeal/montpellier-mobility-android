package crazymeal.fr.montpelliermobility.parking

import android.os.Parcel
import android.os.Parcelable
import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

data class Parking(val technicalName: String, val name: String, val freePlaces: Int, val maxPlaces: Int) : Parcelable {
    val occupation get() = (this.freePlaces * 100) / this.maxPlaces

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(technicalName)
        parcel.writeString(name)
        parcel.writeInt(freePlaces)
        parcel.writeInt(maxPlaces)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Parking> {
        override fun createFromParcel(parcel: Parcel): Parking {
            return Parking(parcel)
        }

        override fun newArray(size: Int): Array<Parking?> {
            return arrayOfNulls(size)
        }
    }

    class Deserializer : ResponseDeserializable<Parking> {
        override fun deserialize(content: String): Parking {
            val input = InputSource(StringReader(content))
            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input)

            val parkingName = document.getElementsByTagName("Name").item(0).textContent

            val associatedName = this.getAssociatedName(parkingName)

            val freePlaces = document.getElementsByTagName("Free").item(0).textContent
            val totalPlaces = document.getElementsByTagName("Total").item(0).textContent

            return Parking(parkingName, associatedName, freePlaces.toInt(), totalPlaces.toInt())
        }

        private fun getAssociatedName(parkingName: String?): String {
            return when(parkingName) {
                "ANTI" -> "Antigone"
                "ARCT" -> "Arc de Triomphe"
                "COME" -> "Comédie"
                "CORU" -> "Corum"
                "EURO" -> "Europa"
                "FOCH" -> "Foch"
                "GAMB" -> "Gambetta"
                "GARE" -> "Gare"
                "Triangle" -> "Triangle"
                "Pitot" -> "Pitot"
                "CIRC" -> "Circe"
                "GARD" -> "Garcia Lorca"
                "MOSS" -> "Mosson"
                "SABI" -> "Sabines"
                "SABL" -> "Sablassou"
                "SJLC" -> "Saint Jean Le Sec"
                "MEDC" -> "Euromédecine"
                "OCCI" -> "Occitanie"
                "VICA" -> "Vicarello"
                "GAUMONT-EST" -> "Gaumont EST"
                "GAUMONT-OUEST" -> "Gaumont OUEST"
                else -> parkingName ?: "?"
            }
        }
    }
}