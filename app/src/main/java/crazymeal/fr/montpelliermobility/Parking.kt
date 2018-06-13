package crazymeal.fr.montpelliermobility

data class Parking(val name: String, val freePlaces: Int, val maxPlaces: Int) {
    val occupation get() = (this.freePlaces * 100) / this.maxPlaces
}