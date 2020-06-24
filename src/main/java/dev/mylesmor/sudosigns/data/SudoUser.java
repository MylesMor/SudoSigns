package dev.mylesmor.sudosigns.data;

import dev.mylesmor.sudosigns.menus.SignEditor;
import org.bukkit.entity.Player;

/**
 * SudoUser class to store all user variables.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class SudoUser {

    private Player p;
    private boolean edit = false;
    private boolean create = false;
    private boolean delete = false;
    private boolean selectToCopy = false;
    private boolean copy = false;
    private boolean run = false;
    private boolean view = false;
    private boolean textInput = false;
    private boolean editing = false;
    private SignEditor editor;

    private PlayerInput inputType;

    public SudoUser(Player p) {
        this.p = p;
    }

    public boolean isSelectToCopy() {
        return selectToCopy;
    }

    public void setSelectToCopy(boolean selectToCopy) {
        this.selectToCopy = selectToCopy;
    }

    public boolean isEdit() {
        return edit;
    }

    public SignEditor getEditor() {
        return editor;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditor(SignEditor editor) {
        editing = true;
        this.editor = editor;
    }

    public void removeEditor() {
        editing = false;
        editor = null;
    }

    public boolean isTextInput() {
        return textInput;
    }

    public PlayerInput getInputType() {
        return inputType;
    }

    public void addTextInput(PlayerInput type) {
        this.textInput = true;
        this.inputType = type;
    }

    public void removeTextInput() {
        this.textInput = false;
        this.inputType = null;
    }


    private String passThru;

    public String getPassThru() {
        return passThru;
    }

    public void setPassThru(String passThru) {
        this.passThru = passThru;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }
}
