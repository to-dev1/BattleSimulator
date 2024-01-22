package game

import game.*
import mathematics.*
import terrain.*

import java.awt.Color

/*
*   Plains      Biome(Color(100, 255, 50), Integer3(512, 256, 128), 32)
*   Desert      Biome(Color(200, 200, 50), Integer3(512, 256, 128), 32)
*   Ice         Biome(Color(230, 240, 255), Integer3(512, 1024, 512), 16)
*   Snow        Biome(Color(255, 255, 255), Integer3(512, 1024, 256), 8)
*   Red         Biome(Color(240, 100, 0), Integer3(1024, 1024, 256), 64)
*   Minerals    Biome(Color(100, 100, 100), Integer3(128, 64, 256), 64)
*   Crystals    Biome(Color(150, 150, 220), Integer3(128, 64, 1024), 80)
*
*   Basic       TerrainWave(8, 1, 64, 0.5, 512, 32, 256, 2, Vector3(0, 0, 0))
*   Mountains   TerrainWave(32, 8, 512, 0.5, 256, 64, 128, 4, Vector3(2500, 0, -5000))
*/

/**
 * PlantDatabase contains the settings for all planet types in an easy to access format.
 */
class PlanetDatabase:
  val planets = Array(
    //0 - Earth
    Planet(0.35, 1.5, Color(220, 240, 255), Biome(Color(100, 255, 50), Integer3(512, 256, 128), 32), TerrainWave(8, 1, 64, 0.5, 512, 32, 256, 2, Vector3(0, 0, 0))),
    //1 - Mars
    Planet(0.3, 0.8, Color(230, 150, 20), Biome(Color(240, 100, 0), Integer3(1024, 1024, 256), 64), TerrainWave(8, 1, 64, 0.5, 512, 32, 256, 2, Vector3(0, 0, 0))),
    //2 - Moon
    Planet(0.06, 0.0, Color(10, 10, 10), Biome(Color(80, 80, 80), Integer3(1024, 1024, 256), 16), TerrainWave(8, 1, 64, 0.5, 512, 32, 256, 2, Vector3(0, 0, 0))),
    //3 - Callisto
    Planet(0.2, 0.0, Color(30, 10, 20), Biome(Color(100, 100, 100), Integer3(128, 64, 256), 64), TerrainWave(32, 8, 512, 0.5, 256, 64, 128, 4, Vector3(2500, 0, -5000))),
    //4 - Crystal
    Planet(0.3, 0.5, Color(30, 10, 20), Biome(Color(150, 150, 220), Integer3(128, 64, 1024), 80), TerrainWave(32, 8, 512, 0.5, 256, 64, 128, 4, Vector3(2500, 0, -5000))),
    //5 - Desert
    Planet(0.35, 1.4, Color(240, 250, 180), Biome(Color(200, 200, 50), Integer3(512, 256, 128), 32), TerrainWave(32, 8, 64, 0.5, 700, 64, 256, 1, Vector3(-1000, 0, 6000))),
    //6 - Snow
    Planet(0.35, 1.2, Color(200, 200, 240), Biome(Color(255, 255, 255), Integer3(512, 1024, 256), 8), TerrainWave(32, 8, 512, 0.5, 256, 64, 128, 3, Vector3(-2000, 0, 3000))),
    //7 - Forest
    Planet(0.35, 1.6, Color(100, 180, 140), Biome(Color(32, 128, 32), Integer3(256, 512, 128), 45), TerrainWave(32, 12, 64, 0.5, 128, 16, 128, 1.5, Vector3(-1000, 0, 6000))),
    //8 - Tundra
    Planet(0.35, 1.5, Color(120, 140, 180), Biome(Color(60, 75, 100), Integer3(1024, 2048, 512), 32), TerrainWave(8, 1, 64, 0.5, 512, 32, 256, 2, Vector3(0, 0, 0))),
  )
