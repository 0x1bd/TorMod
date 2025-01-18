# TorMod

TorMod is a Minecraft Fabric mod that allows you to play Minecraft while routing your traffic through the [Tor network](https://www.torproject.org/). This mod automatically installs and runs Tor in the background, providing enhanced privacy and anonymity while playing Minecraft. However, please note that using Tor with Minecraft is easily detectable and **may result in bans on certain servers**. Use this mod at your own risk.

---

## Features
- **Automatic Tor Installation**: The mod downloads and installs the Tor expert bundle for your platform (Linux, macOS, or Windows).
- **Customizable Tor Port**: Configure Tor to use a custom port (e.g., 1337) instead of the default 9050.
- **Seamless Integration**: Tor runs in the background while you play Minecraft, routing all mc network traffic through the Tor network.
- **Cross-Platform Support**: Works on Linux, macOS, and Windows.
- **Easy to Use**: No manual configuration required—just install the mod and play.

---

## Installation
1. **Install Fabric Loader**:
    - Download and install the [Fabric Loader](https://fabricmc.net/use/) for your Minecraft version.
    - Ensure you have the [Fabric API](https://modrinth.com/mod/fabric-api) installed.
    - Ensure you have [Cloth Config](https://modrinth.com/mod/cloth-config) installed.
    - Ensure you have [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) installed.

2. **Download TorMod**:
    - Download the latest release of TorMod from the [releases page](https://github.com/meo209/TorMod/releases) or [modrinth]().

3. **Install the Mod**:
    - Place the downloaded `.jar` file into your Minecraft `mods` folder.

4. **Launch Minecraft**:
    - Start Minecraft with the Fabric profile.
    - TorMod will automatically install and configure Tor.

---

## Configuration
TorMod comes with a few configurable options. You can modify these settings in the `config/tormod.json` file or in the ingame config screen (accessible from modmenu):

```json
{
  "enabled": true,       // Is the mod enabled.
  "port": 9050,          // The port tor and the internal proxy will be configured to run on. 
  "torVersion": "14.0.4" // What version of tor to use.
}
```