![SudoSigns logo](https://mylesmor.dev/sudosigns/logo.png)

# SudoSigns
A Spigot plugin used to run commands by interacting with a sign.
This plugin has a convenient inventory-based GUI which allows you perform actions on SudoSigns such as:
* Renaming the SudoSign
* Adding permissions that a player must have to run the sign's commands
* Adding either player or console commands to a sign

## Installation
1. Download the Spigot plugin [HERE](https://spigot.com/LINK).
2. Place the JAR file inside your `./plugins` directory.
3. Reload or restart the server.

## Important information
* A _player command_ is executed by the player and the player must therefore have the correct permissions.
* A _console command_ is executed by the console and doesn't require the player to have the relevant permission.
* **Player commands will always execute before console commands.**
* When adding a permission, bear in mind that a player must have **ALL** the permissions to be able to run the sign's commands.


## Getting Started

### Creating a SudoSign
1. Place a sign down and write your own text on it.
2. Use `/ss create <name>` and click on your sign.

### Editing a SudoSign
1. Either:
    1. Break the SudoSign and the selection menu will appear. Click [EDIT]. This looks like the below image:
    ![SudoSigns select menu](https://mylesmor.dev/sudosigns/selectmenu.jpg)
    2. Use `/ss edit [name]`. Either provide the name in the command or exclude it and click the relevant sign.
2. The inventory-based Sign Editor will then open.

### Adding and removing commands
1. Edit the sign using the above steps.
2. Click the command block.
3. This page shows all the commands currently assigned to the sign.
4. Click the writeable book at the bottom.
5. Select either a player command (the player using the sign must have the correct permissions) or a console command.
6. Enter the command in chat.
7. You will now see your command listed as a book in the page.
8. If you'd like to remove the command, click the relevant book.

### Adding and removing permissions
1. Edit the sign by following [these steps](#editing-a-sudosign).
2. Click the barrier block.
3. This page shows all the permissions currently assigned to the sign.
4. Click the writeable book at the bottom.
5. Select either the default or custom permission.
    1. If default, the permission will be added straight away.
    2. If custom, enter the permission in chat.
6. You will now see your command listed as a book in the page.
7. If you'd like to remove the permission, click the relevant book.


## Commands
The following table lists all of the commands and permissions associated with each one.
* <> indicates a required argument.
* [] indicates an optional argument.

**All of the commands are for admin/staff use only, average players should not have any permissions.** In particular,
the `sudosigns.console` permission would allow players to execute **_any console command_**, so this is very important.

| Action   | Command                                    | Description                                                         | Permission         |
|----------|--------------------------------------------|---------------------------------------------------------------------|--------------------|
| Help     | `/ss help`                                 | Displays the help menu.                                             | `sudosigns.help`   |
| Create   | `/ss create <name>`                        | Creates a SudoSign with the specified name.                         | `sudosigns.create` |
| Edit     | `/ss edit [name]`                          | Displays the editor for the specified SudoSign.                     | `sudosigns.edit`   |
| Delete   | `/ss delete [name]`                        | Deletes the specified SudoSign (but not the Sign block).            | `sudosigns.delete` |
| Run      | `/ss run [name]`                           | Runs a SudoSign's commands remotely.                                | `sudosigns.run`    |
| Teleport | `/ss tp <name>`                            | Teleport to a SudoSign.                                             | `sudosigns.tp`     |
| View     | `/ss view [name]`                          | Displays the details of a SudoSign in chat.                         | `sudosigns.view`   |
| Copy     | `/ss copy [old-sign-name] <new-sign-name>` | Copies the specified SudoSign's commands/permissions to a new sign. | `sudosigns.copy`   |
| List     | `/ss list`                                 | Lists all SudoSigns.                                                | `sudosigns.list`   |
| Near     | `/ss near [radius]`                        | Lists SudoSigns within a specified radius (default 5 blocks).       | `sudosigns.near`   |

## Permissions
Most of the permissions are listed in the table above, except for the following two permissions:

| Permission                                | Description                                                                                                                                                         |
|-------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `sudosigns.select`                        | Allows the player to open the selection menu when a SudoSign is destroyed. ![SudoSigns select menu](https://mylesmor.dev/sudosigns/selectmenu.jpg) <br>The commands listed will only apply to the ones that the player has permission for. E.g., the [TP] command won't be shown if the player doesn't have `sudosigns.tp`.|
| `sudosigns.console`                       | **DANGEROUS** Allows the player to create a SudoSign that can **_execute a console command_**. The player must also have the permission `sudosigns.edit` to do this.|

## Upcoming Features
These features will be available in future updates!
* Edit the sign's text from the GUI menu.