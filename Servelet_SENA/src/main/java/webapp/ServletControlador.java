package webapp;

import datos.PersonaDaoJDBC;
import dominio.Personas;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");


        if (accion != null) {
            switch (accion) {
                case "editar":
                    this.editarPersona(request, response);
                    break;
                case "eliminar":
                    String idPersonaStr = request.getParameter("idPersona");

                    if (idPersonaStr == null || idPersonaStr.trim().isEmpty()) {
                        System.out.println("Error: ID de persona no proporcionado o vacío.");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de persona no válido.");
                        break;
                    }
                    try {
                        int idPersona = Integer.parseInt(idPersonaStr.trim());
                        this.eliminarPersona(request, response, idPersona);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: ID de persona con formato inválido. " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de ID de persona no válido.");
                    }
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Personas> personas = new PersonaDaoJDBC().listar();
        System.out.println("clientes = " + personas);
        HttpSession sesion = request.getSession();
        sesion.setAttribute("personas", personas);
        sesion.setAttribute("totalPersona", personas.size());

        response.sendRedirect("persona.jsp");
    }
    private void editarPersona(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //recuperamos el idPersona
        int idPersona = Integer.parseInt(request.getParameter("idPersona"));
        Personas persona = new PersonaDaoJDBC().encontrar(new Personas(idPersona));
        request.setAttribute("persona", persona);
        String jspEditar = "/WEB-INF/paginas/persona/editarPersona.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar":
                    this.insertarPersona(request, response);
                    break;
                case "modificar":
                    this.modificarPersona(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }
    private void insertarPersona(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //recuperamos los valores del formulario agregarPersona
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //Creamos el objeto de Persona (modelo)
        Personas persona = new Personas(nombre, email, password);

        //Insertamos el nuevo objeto en la base de datos
        int registrosModificados = new PersonaDaoJDBC().insertar(persona);
        System.out.println("registrosModificados = " + registrosModificados);

        //Redirigimos hacia accion por default
        this.accionDefault(request, response);
    }
    private void modificarPersona(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener y validar el ID de la persona

        int idPersona = Integer.parseInt(request.getParameter("idPersona"));

        if (idPersona == 0 ) {
            System.out.println("Error: ID de persona no proporcionado o vacío.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de persona no válido.");
            return;
        }

        // Obtener y validar otros parámetros
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (nombre == null || nombre.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            System.out.println("Error: Uno o más campos están vacíos.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todos los campos son obligatorios.");
            return;
        }

        // Crear objeto de persona
        Personas persona = new Personas(idPersona, nombre.trim(), email.trim(), password.trim());

        // Modificar el objeto en la base de datos con manejo de errores
        int registrosModificados = 0;
        try {
            registrosModificados = new PersonaDaoJDBC().actualizar(persona);
            System.out.println("Registros modificados = " + registrosModificados);
        } catch (Exception e) {
            System.out.println("Error al actualizar la persona: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar la persona.");
            return;
        }

        // Redirigir a la acción por defecto si la actualización fue exitosa
        this.accionDefault(request, response);
    }

    private void eliminarPersona(HttpServletRequest request, HttpServletResponse response, int idPersona2)
            throws ServletException, IOException {
        //recuperamos los valores del formulario editarPersona
        int idPersona = Integer.parseInt(request.getParameter("idPersona"));


        //Creamos el objeto de cliente (modelo)
        Personas persona = new Personas(idPersona);

        //Eliminamos el  objeto en la base de datos
        int registrosModificados = new PersonaDaoJDBC().eliminar(persona);
        System.out.println("registrosModificados = " + registrosModificados);

        //Redirigimos hacia accion por default
        this.accionDefault(request, response);
    }
}

