package com.kvxd.tormod

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip

@Config(name = "tormod")
class TorModConfig : ConfigData {

    @Tooltip
    var enabled = true
    @Tooltip
    var port = 9050
    @Tooltip
    var controlPort = 9051
    @Tooltip
    var torVersion = "14.0.4"

}