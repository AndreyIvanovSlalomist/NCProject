package ru.nc.musiclib.interfaces;

/**Интерфейс для контроллеров
 * Знает о модели
 * о въюшке знать неодязательно
 * через методы получает данные обробатывает и вызывает методы из Model
 *
 *
 *
 */
public interface Controller {
    void setModel (Model model);
    void setView (View view);
    boolean validAppend (Object ... objects);
    boolean validUpdate (Object ... objects);
    boolean validDelete (int number);
}
