## Bugfixes
- Fixed incompatibilities with ModernFix's dynamic asset loader by moving to loader specific model loading implementations.

# Notes for Datapack Developers
This verison of Bovines and Buttercups contains a breaking change due to the above bugfix.

### Will I need to change anything?
If you load any custom models, you will need to update where you place your bovinestate files, as well as update your Configured Cow Type JSON files to accommodate for this too.

1. BovineState files have moved from the base directory's `bovinesandbuttercups` folder to `blockstates/bovinesandbuttercups`. The format should still be the same.
2. Replace any mentions of the `models/item/` path with nothing as you don't need to mention this extra path anymore.