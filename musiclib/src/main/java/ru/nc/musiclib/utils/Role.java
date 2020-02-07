package ru.nc.musiclib.utils;

import java.io.Serializable;

public enum Role implements Serializable {
    user {
        @Override
        public String toString() {
            return "Пользователь";
        }
    },
    moderator {
        @Override
        public String toString() {
            return "Модератор";
        }
    },
    administrator {
        @Override
        public String toString() {
            return "Администратор";
        }
    }
}
