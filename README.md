# JavaGameTemplate

A small, readable Java 2D game starter you can copy into new projects. It focuses on clarity and minimum moving parts: a window, a panel with a tiny game loop (Swing Timer), a simple player entity, basic input, asset helpers, and optional dialogue support.

## Project structure

- main/
  - GameApplication.java — entry point
  - GameWindow.java — top-level Swing window (info bar, game panel, buttons)
  - GamePanel.java — rendering surface and game loop
- entities/
  - PlayerEntity.java — example player (draws a circle, moves on input)
  - DialogueBoxEntity.java — simple overlay box to draw dialogue text
- managers/
  - ImageManager.java — image loading helpers
  - SoundManager.java — example audio singleton (optional)
  - DialogueManager.java — reads lines from a text file one-by-one
- src/dialouge/test.txt — sample dialogue text (one line per entry)

No external dependencies are required beyond the JDK.

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

- main/GamePanel wiring (already included)
  - On start, reads `src/dialouge/test.txt` and shows the first line if available
  - gameRender() draws the dialogue box every frame
  - nextDialogue(): advances to the next text line (called by the window’s Next button)

To change the script, edit `src/dialouge/test.txt` (one line per entry). To disable dialogue, skip calling `loadFromFile` or set empty text in the panel.

## Customize quickly

- Replace the PlayerEntity with your own entity and draw it in `GamePanel.gameRender()`.
- Map more keys in `GameWindow.keyPressed()` (e.g., up/down) and translate to `updateGameEntities(direction)`.
- Use `ImageManager` to load sprites and draw them instead of the sample circle.
- If you need sound, use `managers.SoundManager` as a starting point.

## Notes

- The loop uses a Swing Timer (~60 FPS). For physics or precise timing you can swap in a fixed-step loop later.
- The template avoids complexity: no scene graph, no ECS. Add what you need as your game grows.

Happy game making!
