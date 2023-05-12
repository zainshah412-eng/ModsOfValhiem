package com.example.dropbox_base.utils

object Constantdropbox {
    //content.json file contains seeds, textures, maps, skins, mods
    const val ARMOR_JSON_PATH_DROPBOX = "/armor/armor.json"
    const val ARMOR_IMAGE_JSON_PATH_DROPBOX = "/armor/"

    const val BIOME_JSON_PATH_DROPBOX = "/biomes/biomes.json"
    const val BIOME_IMAGE_JSON_PATH_DROPBOX = "/biomes/"

    const val MODS_JSON_PATH_DROPBOX = "/mods/mods.json"
    const val MODS_IMAGE_JSON_PATH_DROPBOX = "/mods/"

    const val SEEDS_JSON_PATH_DROPBOX = "/seeds/seeds.json"
    const val SEEDS_IMAGE_JSON_PATH_DROPBOX = "/seeds/"

    const val KEYS_JSON_PATH_DROPBOX = "/keys/keys.json"
    const val KEYS_IMAGE_JSON_PATH_DROPBOX = "/keys/"

    const val RECIPE_JSON_PATH_DROPBOX = "/recepies/recepies.json"
    const val RECIPE_IMAGE_JSON_PATH_DROPBOX = "/recepies/"

    const val CREATURE_JSON_PATH_DROPBOX = "/creatures/creatures.json"
    const val CREATURE_IMAGE_JSON_PATH_DROPBOX = "/creatures/"

    const val CHARAACTER_JSON_PATH_DROPBOX = "/Character/character.json"
    const val CHARAACTER_IMAGE_JSON_PATH_DROPBOX = "/Character/"

    const val MAPS_JSON_PATH_DROPBOX = "/Maps/maps.json"
    const val MAPS_IMAGE_JSON_PATH_DROPBOX = "/Maps/"

    const val GUIDES_JSON_PATH_DROPBOX = "/Guides/Guides.json"
    const val GUIDES_IMAGE_JSON_PATH_DROPBOX = "/Guides/"

    const val BUILDS_JSON_PATH_DROPBOX = "/builds/builds.json"
    const val BUILDS_IMAGES_JSON_PATH_DROPBOX = "/builds/"

    const val COMMANDS_JSON_PATH_DROPBOX = "/commands.json"

    const val CHEATS_JSON_PATH_DROPBOX = "/cheats/cheats.json"

    const val TOOLS_JSON_PATH_DROPBOX = "/Tools/tools.json"

    const val TOOLS_IMAGE_JSON_PATH_DROPBOX = "/Tools/"

    const val SERVERS_JSON_PATH_DROPBOX = "/serverContent.json"
    const val NAME_GENERATOR_JSON_PATH_DROPBOX = "/name_generator.json"

    const val SKIN_MAKER_JSON_PATH_DROPBOX = "/skinmaker.json"
//    const val BIOME_JSON_PATH_DROPBOX = "/biome.json"
    const val WALLPAPERS_JSON_PATH_DROPBOX = "/wallpapers.json"
    const val ADDONS_JSON_PATH_DROPBOX = "/addons.json"
    const val QUIZ_JSON_PATH_DROPBOX = "/quiz.json"

    val jsonFileAryList=ArrayList<String>().apply {
        add(ARMOR_JSON_PATH_DROPBOX)
        add(COMMANDS_JSON_PATH_DROPBOX)
        add(SERVERS_JSON_PATH_DROPBOX)
        add(NAME_GENERATOR_JSON_PATH_DROPBOX)
        add(GUIDES_JSON_PATH_DROPBOX)
    }

}