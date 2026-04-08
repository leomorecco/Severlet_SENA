package datos;

import dominio.Personas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDaoJDBC {
    private static final String SQL_SELECT = "SELECT idPersona, nombre, email" +
            ",password FROM persona";
    /////////////////
    private static final String SQL_SELECT_BY_ID = "SELECT idPersona, nombre, email, password "
            + " FROM persona WHERE idPersona = ?";

    private static final String SQL_INSERT = "INSERT INTO persona(nombre, email, password) "
            + " VALUES(?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE persona "
            + " SET nombre=?, email=?, password=? WHERE idPersona=?";

    private static final String SQL_DELETE = "DELETE FROM persona WHERE idPersona = ?";

    ///////////////
    public List<Personas> list() {
        List<Personas> listar = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()){
                Personas persona = new Personas();
                persona.setIdPersona(rs.getInt("idPersona"));
                persona.setNombre(rs.getString("nombre"));
                persona.setEmail(rs.getString("email"));

                listar.add(persona);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                rs.close();
                stmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return listar;
    }


    //////////////////////////
    public List<Personas> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Personas persona = null;
        List<Personas> personas = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int idPersona = rs.getInt("IdPersona");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");
                persona = new Personas(idPersona, nombre, email, password);
                personas.add(persona);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return personas;
    }
    public Personas encontrar(Personas personas) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Personas persona = null; // Inicializamos en null para retornar null si no se encuentra

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, personas.getIdPersona());
            rs = stmt.executeQuery();

            if (rs.next()) { // Verificamos si hay resultados
                int idPersona = rs.getInt("IdPersona");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");

                // Crear un nuevo objeto Personas con los datos obtenidos
                persona = new Personas(idPersona,nombre, email, password);
            } else {
                System.out.println("No se encontró una persona con ID: ");
            }

        } catch (SQLException ex) {
            System.out.println("Error al buscar persona: " + ex.getMessage());
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return persona; // Retorna el objeto encontrado o null si no se encontró
    }


    public int insertar(Personas persona) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getEmail());
            stmt.setString(3, persona.getPassword());

            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    public int actualizar(Personas persona) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getEmail());
            stmt.setString(3, persona.getPassword());
            stmt.setInt(4, persona.getIdPersona());

            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    public int eliminar(Personas persona) {
        if (persona == null || persona.getIdPersona() == 0) {
            throw new IllegalArgumentException("El objeto persona es nulo o el ID no es válido.");
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false); // Se desactiva el auto-commit para mayor seguridad en transacciones

            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, persona.getIdPersona());

            rows = stmt.executeUpdate();
            conn.commit(); // Se confirma la eliminación

            System.out.println("Registro eliminado correctamente. Filas afectadas: " + rows);
        } catch (SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback(); // Se revierte la transacción en caso de error
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(System.out);
            }
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    ////////////////////////
}


