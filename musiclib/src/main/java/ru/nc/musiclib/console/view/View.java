package ru.nc.musiclib.console.view;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.services.Model;

/**
 * Интерфейс для въюшек
 * Знает о Controller и  о Model
 * имплиментирует Observer для получения оповещений от Model (возомжно както реагирует на некоторые виды соповещений)
 * Использует методы из Model для получения данных
 * опказывает возможные действия для пользователя (базовые : добавить , удалить, изменить, просмотр, выход. Для каждого их них свои вареанты и поведение)
 * запрашивает ввод данных пользователем, отправляет их в Controller, по результатам работы контроллера омжет вернуться на предыдущее действие
 * <p>
 * может содержать методы проверки вводимых данных (т.е. если ожидаем число то введено должно быть число)
 */
public interface View {
    void runView();

    void setModel(Model model);

    void setController(Controller controller);
}
