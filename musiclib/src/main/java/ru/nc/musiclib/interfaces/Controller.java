package ru.nc.musiclib.interfaces;

public interface Controller {
    void setModel (Model model);
    void setView (View view);
    boolean validAppend (Object ... objects);
    boolean validUpdate (Object ... objects);
    boolean validDelete (Object ... objects);
}
