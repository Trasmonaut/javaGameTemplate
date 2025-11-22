# JavaGameTemplate

A small, readable Java 2D game starter you can copy into new projects. It focuses on clarity and minimal moving parts: a window, a panel with a tiny game loop (Swing Timer), a simple player entity, basic input, asset helpers, and optional dialogue and logo support.

This README has been updated to reflect the changes in Version 1.1 (see latest commits).

## What's new (Version 1.1.5)
- InputManger class was introduced to handle all user inputs. Inputs are only updated once every game tick, to keep them in track.
 Inputs are frozen during game state scenes, such as transitions, loadings, paused, etc.


## Project structure (updated)

- main/
  - GameApplication.java — entry point
  - GameWindow.java — top-level Swing window (info bar, game panel, buttons)
  - GamePanel.java — rendering surface and game loop (now supports logo trigger & dialogue wiring)
- entities/
  - PlayerEntity.java — example player (draws a circle, moves on input)
  - Entity.java - A framework for all entites
  - GravityEntity.java - A framework for phyiscs based entities
  - MoveableEntity.java - A framework for player interactble entities
- managers/
  - ImageManager.java — image loading helpers (bug fixes / improvements)
  - SoundManager.java — reworked sound singleton (optional)
  - DialogueManager.java — reads lines from a text file one-by-one
  - LogoManager.java — optional manager for logo lifecycle
- scenes
  - DialogueBoxEntity.java — simple overlay box to draw dialogue text
  - LogoEntity.java — displays splash/logo with improved scaling
  - TransitionEntity.java - displays a simple screen transition
- src
  - src/dialouge/test.txt — sample dialogue text (one line per entry)
  - src/logo/logo.png — example splash/logo image (if present)
  - src/sounds/ping.wav — example sound effect
- util/
  - ImageFX.java — basic image effects / helpers

No external dependencies are required beyond the JDK (uses Swing for rendering and simple audio APIs).

## Run

- Launch `main.GameApplication`.
- The window maximizes to your screen; the game panel is centered and uses the screen dimensions.


## Dialogue system (simple)

The Dialogue system is now managed by `SceneManager` and rendered by `scenes/DialogueBoxEntity`.

- managers/DialogueManager
  - loadFromFile(String path): loads lines (one per line) into memory
  - hasNext(): whether more lines remain
  - nextLine(): returns the next line (or null if none)
  - reset(): go back to the start

- scenes/DialogueBoxEntity
  - setText(String): set the current text to render
  - draw(Graphics2D g2, int width, int height): draws a semi-transparent box at the bottom and the current text

- SceneManager wiring
  - `managers/SceneManager` owns a `DialogueManager` + `DialogueBoxEntity` and provides simple APIs:
    - `showDialogueFromFile(String path)` — load a dialogue file and show the first line
    - `advanceDialogue()` — advance to the next line (hides the box when finished)
    - `isAnyActive()` — returns true when dialogue/logo/transition is active (useful to pause gameplay)

- main/GamePanel wiring (current behavior)
  - The panel now delegates overlay control to `SceneManager`.
  - On game start the template loads `src/dialouge/test.txt` and shows the first line by default.
  - The Next button in `GameWindow` calls `GamePanel.advanceDialogue()` (which delegates to `SceneManager.advanceDialogue()`).

To change the script, edit `src/dialouge/test.txt` (one line per entry). To disable dialogue, remove the `showDialogueFromFile` call in `GamePanel.createGameEntities()`.

## Logo / Splash

- Logo files: `src/logo/logo.png`
- Use `LogoManager` and `LogoEntity` to show a splash at game start. Logo scaling preserves aspect ratio and attempts to cover / center on the game panel.
- Trigger from `GamePanel` using the provided `triggerLogo()` method or equivalent hookup.
 - Trigger from `GamePanel` using the provided `triggerLogo()` method or equivalent hookup. You can also use `GamePanel.loadLogo(path)` + `GamePanel.showLogo(durationMs)` to control loading and display separately.

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
 - Use `SceneManager` to centrally manage dialogue/logo/transition overlays; call `GamePanel` wrapper methods or access `SceneManager` directly if you prefer.

## Notes

- The game loop uses a Swing Timer (~60 FPS). For physics or precise timing you can swap in a fixed-step loop later.
- The template intentionally avoids complexity: no scene graph, no ECS. Add what you need as your game grows.

Additional notes about transitions and pause behaviour:
- Transitions: `TransitionManager` supports a timeline of fade-in -> hold -> fade-out. Default fades are 200ms each and are scaled if the total duration is shorter than the sum of fades. Use `SceneManager.showTransition(totalMs)` (via `GamePanel.triggerTransition(...)`) to activate.
- Pause semantics: `GamePanel` checks `SceneManager.isAnyActive()` and pauses player movement and game updates while dialogue, logo, or transition overlays are active. This preserves the expected behaviour where overlays block gameplay.

## Changelog (high level)

- 2025-11-02 — Version 1.1
  - SoundManager rework, logo support, dialogue system, title update, image/animation fixes, project reorganization.
  - TransitionManager rework

Happy game making!
