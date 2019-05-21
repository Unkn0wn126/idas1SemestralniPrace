/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.SQLException;
import model.Uzivatel;

/**
 *
 * @author Lukas
 */
public class DbHelper {
    private Connection con;
    private Uzivatel currentUser;

    public DbHelper(Connection con) throws SQLException {
        this.con = con;
    }
}
