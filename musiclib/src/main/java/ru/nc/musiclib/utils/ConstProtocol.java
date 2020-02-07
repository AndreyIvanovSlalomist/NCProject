package ru.nc.musiclib.utils;

/**Перечень команд которыми обмениваются клиент и сервер
 * прежде чем начать передавать наддые
 *
 */
public enum ConstProtocol {exit, add, find, delete, update, getAll, sort, getFile, loadFromFile, finish, addUser, checkUser, setRole, getAllUsers, filter, errorUser, deleteUser}
