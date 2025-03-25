package awabi2048.hrw

import org.bukkit.Location

object Lib {
    /**
     * @param location Location
     * @return Log に保存される形式の文字列座標（ex: '10,10,10'）
     */
    fun toStringCoordinate(location: Location): String {
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ

        return ("$x,$y,$z")
    }

//    fun revertStringCoordinate(string: String, world: World): Location? {
//        try {
//            val elements = string.split(",")
//            val x = elements[0]
//            val y = elements[1]
//            val z = elements[2]
//
//            return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
//        } catch (e: Exception) {
//            return null
//        }
//    }
//
//    fun equalsLocation(location1: Location, location2: Location): Boolean {
//        val hasSameWorld = location1.world == location2.world
//        val hasSameCoordinateX = location1.blockX == location2.blockX
//        val hasSameCoordinateY = location1.blockY == location2.blockY
//        val hasSameCoordinateZ = location1.blockZ == location2.blockZ
//
//        return hasSameWorld && hasSameCoordinateX && hasSameCoordinateY && hasSameCoordinateZ
//    }
}
