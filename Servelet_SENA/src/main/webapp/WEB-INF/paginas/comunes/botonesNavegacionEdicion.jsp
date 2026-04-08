<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<section id="actions" class="py-4 mb-4 bg-light">
    <div class="container">
        <div class="row">
            <div class="col-md-3">
                <a href="index.jsp" class="btn btn-ligth btn-block">
                    <i class="fas fa-arrow-left"></i> Regresar al inicio
                </a>
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-success btn-block">
                    <i class="fas fa-check"></i> Guardar Persona
                </button>
            </div>
            <c:if test="${not empty persona.idPersona}">
                <div class="col-md-3">
                    <a href="${pageContext.request.contextPath}/ServletControlador?accion=eliminar&idPersona=${persona.idPersona}"
                       class="btn btn-danger btn-block"
                       onclick="return confirm('¿Estás seguro de eliminar a ${persona.nombre}?');">
                        <i class="fas fa-trash"></i> Eliminar Persona
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</section>