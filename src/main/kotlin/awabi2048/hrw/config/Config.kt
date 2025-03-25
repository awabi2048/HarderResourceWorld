package awabi2048.hrw.config

import awabi2048.hrw.Main.Companion.instance
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import java.io.File

object Config {
    lateinit var uniqueItems: Map<String, UniqueItem>
    lateinit var mobExtraDrops: Map<EntityType, Set<ExtraDrop>>
    lateinit var oreExtraDrops: Map<Material, Set<ExtraDrop>>

    private val dataFile = File(instance.dataFolder, "config.yml")

    init {
        if (!dataFile.exists()) {
            instance.saveResource("config.yml", true)
        }
    }

    private val config = YamlConfiguration.loadConfiguration(dataFile)

    /**
     * @return スポーン地点からの距離に乗じて難易度が決定される
     */
    val DISTANCE_DIFFICULTY_MULTIPLIER: Double
    get() {
        if (!config.contains("distance_difficulty_multiplier")) throw IllegalStateException("config.ymlの読み込みに失敗しました。distance_difficulty_multiplier キーが必要です。")
        val configValue = config.getDouble("distance_difficulty_multiplier")
        if (configValue < 0.0) throw IllegalStateException("config.ymlの読み込みに失敗しました。正数である必要があります: distance_difficulty_multiplier")

        return configValue
    }

    /**
     * @return 難易度に乗じてモブへの強化度合いが決定される
     */
    val DIFFICULTY_MOB_MULTIPLIER: Double
        get() {
            if (!config.contains("difficulty_mob_multiplier")) throw IllegalStateException("config.ymlの読み込みに失敗しました。difficulty_mob_multiplier キーが必要です。")
            val configValue = config.getDouble("difficulty_mob_multiplier")
            if (configValue < 0.0) throw IllegalStateException("config.ymlの読み込みに失敗しました。正数である必要があります: difficulty_mob_multiplier")

            return configValue
        }

    /**
     * @return 難易度に乗じてルートの倍率が決定される
     */
    val DIFFICULTY_LOOT_MULTIPLIER: Double
        get() {
            if (!config.contains("difficulty_loot_multiplier")) throw IllegalStateException("config.ymlの読み込みに失敗しました。difficulty_loot_multiplier キーが必要です。")
            val configValue = config.getDouble("difficulty_loot_multiplier")
            if (configValue < 0.0) throw IllegalStateException("config.ymlの読み込みに失敗しました。正数である必要があります: difficulty_loot_multiplier")

            return configValue
        }

    /**
     * @return 設定が適用されるワールドの一覧
     */
    val APPLIED_WORLDS: Set<World>
        get() {
            if (!config.contains("applied_worlds")) throw IllegalStateException("config.ymlの読み込みに失敗しました。applied_worlds キーが必要です。")
            val worlds = config.getStringList("applied_worlds").map { Bukkit.getWorld(it)?: throw IllegalStateException("config.ymlの読み込みに失敗しました。指定されたワールドが見つかりませんでした: applied_worlds") }
            return worlds.toSet()
        }

    val BLOOD_MOON_RATE: Double
        get() {
            if (!config.contains("blood_moon_rate")) throw IllegalStateException("config.ymlの読み込みに失敗しました。blood_moon_rate キーが必要です。")
            val configValue = config.getDouble("blood_moon_rate")
            if (configValue < 0.0 || configValue > 1.0) throw IllegalStateException("config.ymlの読み込みに失敗しました。0以上1以下の小数である必要があります: blood_moon_rate")

            return configValue
        }


    fun loadAll() {
        uniqueItems = loadUniqueItems()
        mobExtraDrops = loadMobExtraDrops()
        oreExtraDrops = loadOreExtraDrops()
    }

    private fun loadUniqueItems(): Map<String, UniqueItem> {
        val uniqueItemKeys = config.getConfigurationSection("unique_items")?.getKeys(false)?: throw IllegalStateException("config.ymlの読み込みに失敗しました。unique_itemsキーが必要です。")
        return uniqueItemKeys.associateWith { key ->
            val individualSection = config.getConfigurationSection("unique_items.$key")
            val materialString = individualSection!!.getString("material")?: throw IllegalStateException("config.ymlの読み込みに失敗しました。materialキーが必要です: unique_items.$key")
            val material = Material.getMaterial(materialString)?: throw IllegalStateException("config.ymlの読み込みに失敗しました。materialが無効です: unique_items.$key")
            val itemName = individualSection.getString("item_name")
            val lore = if (individualSection.contains("lore")) individualSection.getStringList("lore") else null
            val customModelData = if (individualSection.contains("custom_model_data")) individualSection.getInt("custom_model_data") else null

            UniqueItem(material, itemName, lore, customModelData)
        }
    }

    private fun loadMobExtraDrops(): Map<EntityType, Set<ExtraDrop>> {
        val mobExtraDropKeys = config.getConfigurationSection("mob_extra_drops")?.getKeys(false)?: return mapOf()
        return mobExtraDropKeys.toList().associate { key ->

            if (key.uppercase() !in EntityType.entries.map {it.name}) throw IllegalStateException("config.ymlの読み込みに失敗しました。entityTypeが無効です: mob_extra_drops.$key")

            val individualSection = config.getConfigurationSection("mob_extra_drops.$key")!!
            val mobType = EntityType.valueOf(key.uppercase())

            val extraDrops = individualSection.getKeys(false).map {
                ExtraDrop(uniqueItems[it]!!, individualSection.getDouble(it))
            }.toSet()

            mobType to extraDrops
        }
    }

    private fun loadOreExtraDrops(): Map<Material, Set<ExtraDrop>> {
        val oreExtraDropKeys = config.getConfigurationSection("ore_extra_drops")?.getKeys(false)?: return mapOf()
        return oreExtraDropKeys.toList().associate { key ->

            if (key.uppercase() !in Material.entries.map {it.name}) throw IllegalStateException("config.ymlの読み込みに失敗しました。materialが無効です: ore_extra_drops.$key")

            val individualSection = config.getConfigurationSection("ore_extra_drops.$key")!!
            val material = Material.valueOf(key.uppercase())

            val extraDrops = individualSection.getKeys(false).map {
                ExtraDrop(uniqueItems[it]!!, individualSection.getDouble(it))
            }.toSet()

            material to extraDrops
        }
    }
}
