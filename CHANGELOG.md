### Integration
- Reimplemented JEI integration.
- Added Nectar Bowls to JEI, REI and EMI contexts.
- REI integration now treats different custom flowers and mushrooms as their own stack. 
- Made EMI integration no longer show any `bovinesandbuttercups:missing_flower` custom flower items.
- Made EMI integration no longer show any `bovinesandbuttercups:missing_mushroom` custom mushroom items.
- You are now able to hover over Lockdown effects to see each individual duration with EMI's centered effect display. **You'll need to update EMI to at least 0.6.3**

### Bugfixes
- [FORGE] Fixed crashes involving MobEffectEvents.
- [FORGE] Fixed Nectar Bowls not rendering at all.
- Fixed `/effect lockdown` command not working properly.