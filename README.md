# Homeostatic [![Project](http://cf.way2muchnoise.eu/full_634466_downloads.svg)](https://minecraft.curseforge.com/projects/634466)
[![](http://cf.way2muchnoise.eu/versions/634466.svg)](https://www.curseforge.com/minecraft/mc-mods/homeostatic/files)
[![](https://img.shields.io/badge/NeoForge-20.4+-orange.svg?longCache=true&style=flat)](https://www.curseforge.com/minecraft/mc-mods/homeostatic/files?gameVersionTypeId=6)
[![](https://img.shields.io/badge/Fabric-0.46.0+-yellowgreen.svg?longCache=true&style=flat)](https://www.curseforge.com/minecraft/mc-mods/homeostatic/files?gameVersionTypeId=4)
![MIT](https://img.shields.io/badge/license-MIT-blue.svg?longCache=true&style=flat)

A temperature and thirst mod that doesn't defy logic:
  - Environment temperature is relative to a number of external factors based on [wet-bulb globe temperature - WGBT](https://en.wikipedia.org/wiki/Wet-bulb_globe_temperature)
    - Biome temperatures in Minecraft are overridden to give a more authentic feel to temperatures that players can relate to.
    - Just because there is snow, doesn't mean that it is stupid cold. That doesn't work in reality with Minecraft anyhow, since the biomes are mixed where they are often close.
    - Temperature smoothing is done to try and normalize temperature with nearby biomes.
    - Seasons are taken into account if using Serene Seasons. (At least until there is a viable season alternative).
  - Thirst is affected by the environment temperature, body temperature and activity.
    - Water is life
    - Being overheated will cause water to be used rapidly, which could lead to bad effects or death.
  - Body temperature is regulated through armor modifications and heat sources.
    - Modifications can be "sewn" into any armor that gives various protective abilities.
  - Cold isn't an energy form like in other mods (and Minecraft actually), that's just dumb.
    - Lack of heat causing the body temperature to lower will eventually cause bad effects, even death.
    - [Being wet](https://en.wikipedia.org/wiki/Hypothermia#Water_immersion) is really bad (unless you're overheated), it can causing rapid cooling.
  - Blocks that generate heat aren't measured in temperature, but rather the [thermal radiation](https://en.wikipedia.org/wiki/Thermal_radiation) they release, which affects how "hot" it is to be near these objects.

## Links of Interest

+ [Homeostatic Wiki](https://github.com/wendall911/Homeostatic/wiki)
+ [Homeostatic Curseforge Page](https://www.curseforge.com/minecraft/mc-mods/homeostatic)
+ [Homeostatic Modrinth Page](https://modrinth.com/mod/homeostatic)
