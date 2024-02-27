## Notes on Quilt
- The separate Quilt based JAR is no longer going to be supported. Please use the Fabric JAR for any future Quilt versions.

## Bugfixes
- Fixed a rare crash involving Mooblooms not being able to find their texture. #22
- Fixed missing mooshrooms becoming an actual mooshroom type upon tracking.
- Fixed infinite lockdown effects being dimmed and prioritised in the HUD.

## Miscellaneous
- Updated mod icon.
- Removed Cardinal Components API dependency in favour of Fabric's Attachment/API Lookup.
  - This also rewrites attachments/capabilities on the NeoForge end.
  - This changes the NBT path of everything on Fabric.
- Updated for NeoForge Networking rewrite.
- Re-added JEI and EMI compatibility.
  - REI compatibility is not available on NeoForge.