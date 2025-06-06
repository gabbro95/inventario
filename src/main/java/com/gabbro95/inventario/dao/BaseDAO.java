package com.gabbro95.inventario.dao;

public abstract class BaseDAO {
    protected <T> T execute(DBManager.ConnectionFunction<T> function) {
        return DBManager.withConnection(function);
    }
}
