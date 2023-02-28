### Gameplay
- Readjusted Limelight Moobloom breeding conditions to utilise Big or Small Dripleaf instead of Azaleas. Oak wood is no longer valid and you should instead use Flowering Azalea Leaves for the condition, you can still use Flowering Azaleas as per normal.
- Successful breeding particles now have a low random amount of delta applied to them.
- Buttercup (Poison), Hyacinth (Wither) and Pink Daisy (Strength) nectar lockdown durations have been reduced to 2 minutes. (Previously 3 minutes)
- Freesia (Water Breathing) and Tropical Blue (Fire Resistance) nectar lockdown durations have been reduced to 8 minutes. (Previously 10 minutes)
- Snowdrop (Mining Fatigue) nectar lockdown durations has been reduced to 6 minutes. (Previously 10 minutes)

Your existing nectars prior to this update will keep their durations, use them wisely!

### Data Packing
- Any Weighted Configured Cow Type field now supports directly passing through a Resource Location.
- Updated Breeding Condition systems, please check the documentation at a later date for more information.
- Moved all base Moobloom types' breeding conditions to use the new system. 

### Texture Changes
- Updated Freesia Moobloom texture spot color.

### Language
- Added Chinese Traditional language support (#13/#14 - Pancakes0228)

### Bugfixes
- Fixed Mojang Textures and No Grass Back built-in resource packs having an outdated path for their textures.
- [FORGE] Fixed broken built-in resource pack implementation.
- [FORGE] Fixed reload command failing due to faulty packet code.
- [FORGE] Fixed Forge thinking that Bovines always has an update by changing the version schema. 

### Miscellaneous
- Modified built-in rendered items from this mod's rendering logic.
- gui_light is now gotten from the model that is being displayed as opposed to the direct model.
- Updated JAR version schema to match Modrinth version schema.