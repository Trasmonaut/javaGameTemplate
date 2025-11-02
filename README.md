# JavaGameTemplate

A small, readable Java 2D game starter you can copy into new projects. It focuses on clarity and minimal moving parts: a window, a panel with a tiny game loop (Swing Timer), a simple player entity, basic input, asset helpers, and optional dialogue and logo support.

This README has been updated to reflect the changes in Version 1.1 (see latest commits).

## What's new (Version 1.1)

- Reworked SoundManager to improve sound effect handling and performance. A sound test file is included at `src/sounds/ping.wav`.
- Added LogoEntity and LogoManager to display a splash/logo at game start. The logo image is located at `src/logo/logo.png`. The logo can be triggered from `GamePanel` with `triggerLogo()` (or similar wiring in the panel).
- Added a simple Dialogue system:
  - `managers/DialogueManager` — load dialogue lines from a text file and iterate them
  - `entities/DialogueBoxEntity` — draw a semi-transparent dialogue box at the bottom of the screen
  - Sample dialogue file: `src/dialouge/test.txt`
- Updated `GameWindow` title to "Java Game Template v1.1".
- Improved logo scaling logic in `LogoEntity` to preserve aspect ratio and better cover the game panel.
- Minor bug fixes in `Animation` and `ImageManager` for more robust asset handling.
- Project structure has been reorganized for clarity and maintainability.
- Added `ImageFX.java` (image helper / effects utilities).

## Project structure (updated)

- main/
  - GameApplication.java — entry point
  - GameWindow.java — top-level Swing window (info bar, game panel, buttons)
  - GamePanel.java — rendering surface and game loop (now supports logo trigger & dialogue wiring)
- entities/
  - PlayerEntity.java — example player (draws a circle, moves on input)
  - DialogueBoxEntity.java — simple overlay box to draw dialogue text
  - LogoEntity.java — displays splash/logo with improved scaling
- managers/
  - ImageManager.java — image loading helpers (bug fixes / improvements)
  - SoundManager.java — reworked sound singleton (optional)
  - DialogueManager.java — reads lines from a text file one-by-one
  - LogoManager.java — optional manager for logo lifecycle
- src/dialouge/test.txt — sample dialogue text (one line per entry)
- src/logo/logo.png — example splash/logo image (if present)
- src/sounds/ping.wav — example sound effect
- util/
  - ImageFX.java — basic image effects / helpers

No external dependencies are required beyond the JDK (uses Swing for rendering and simple audio APIs).

## Run

- Launch `main.GameApplication`.
- The window maximizes to your screen; the game panel is centered and uses the screen dimensions.

## Controls (default)

- Start Game: Start button
- Exit: Exit button
- Move: Left Arrow / A (left), Right Arrow / D (right)
- Attack example: Left mouse click (triggers a demo point update)
- Shield example: Right mouse press/release (demo toggle)
- Dialogue Next: Next button (advances one line when dialogue is present)

## Dialogue system (simple)

- managers/DialogueManager
  - loadFromFile(String path): loads lines (one per line) into memory
  - hasNext(): whether more lines remain
  - nextLine(): returns the next line (or null if none)
  - reset(): go back to the start

- entities/DialogueBoxEntity
  - setText(String): set the current text to render
  - draw(Graphics2D g2, int width, int height): draws a semi-transparent box at the bottom and the current text

- main/GamePanel wiring
  - On start, the panel can read `src/dialouge/test.txt` and show the first line if available
  - `gameRender()` draws the dialogue box every frame when text is set
  - `nextDialogue()` advances to the next text line (connected to the window’s Next button)

To change the script, edit `src/dialouge/test.txt` (one line per entry). To disable dialogue, skip calling `loadFromFile` or set empty text in the panel.

## Logo / Splash

- Logo files: `src/logo/logo.png`
- Use `LogoManager` and `LogoEntity` to show a splash at game start. Logo scaling preserves aspect ratio and attempts to cover / center on the game panel.
- Trigger from `GamePanel` using the provided `triggerLogo()` method or equivalent hookup.

## Sound

- Sound helper: `managers/SoundManager`
- Example sound: `src/sounds/ping.wav` — used for sound tests / examples.
- SoundManager is optional — if you don't require audio, you can ignore or remove the code.

## Quick customization

- Replace the PlayerEntity with your own entity and draw it in `GamePanel.gameRender()`.
- Map more keys in `GameWindow.keyPressed()` (e.g., up/down) and translate to `updateGameEntities(direction)`.
- Use `ImageManager` / `ImageFX` to load and transform sprites and draw them instead of the sample circle.
- Use `SoundManager` as a starting point for sound effects or background music.
- Use `DialogueManager` + `DialogueBoxEntity` for simple in-game text/dialogue sequences.
- Use `LogoManager` + `LogoEntity` for splash screens.

## Notes

- The game loop uses a Swing Timer (~60 FPS). For physics or precise timing you can swap in a fixed-step loop later.
- The template intentionally avoids complexity: no scene graph, no ECS. Add what you need as your game grows.

## Changelog (high level)

- 2025-11-02 — Version 1.1
  - SoundManager rework, logo support, dialogue system, title update, image/animation fixes, project reorganization.

Happy game making!
