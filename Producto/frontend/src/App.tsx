import { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from "react-router-dom";

import { AuthProvider, useAuth } from "./contexts/AuthContext";
import { NotificationProvider } from "./contexts/NotificationContext";

import Sidebar from "./components/layout/Sidebar";
import Navbar from "./components/layout/Navbar";
import ModuleTabBar, { getActiveModule } from "./components/layout/ModuleTabBar";
import ModuleLandingPage from "./components/layout/ModuleLandingPage";

/* AUTH */
import Login from "./pages/auth/Login";

/* HOME */
import Welcome from "./pages/home/Welcome";

/* COMERCIAL */
import NotaDeVenta from "./pages/commercial/sales-notes/NotaDeVenta";
import SolicitudCostosContainer from "./pages/commercial/costing/SolicitudCostosContainer";
import AdministracionNegocios from "./pages/commercial/administration/AdministracionNegocios";
import GestionPlantillas from "./pages/commercial/templates/GestionPlantillas";
import TableroComercial from "./pages/commercial/TableroComercial";
import GestionProyectos from "./pages/commercial/projects/GestionProyectos";
import OrdenesProduccion from "./pages/commercial/production-orders/OrdenesProduccion";

/* GESTION USUARIOS */
import GestionClientes from "./pages/users/GestionClientes";
import GestionProveedores from "./pages/users/GestionProveedores";
import GestionUsuariosColaboradores from "./pages/users/GestionUsuariosColaboradores";
import GestionVendedores from "./pages/users/GestionVendedores";

/* ADMIN */
import GestionDatosMaestros from "./pages/master/GestionDatosMaestros";
import GestionAreas from "./pages/admin/GestionAreas";
import GestionRoles from "./pages/admin/GestionRoles";
import GestionPermisosRol from "./pages/admin/GestionPermisosRol";
import GestionPermisos from "./pages/admin/GestionPermisos";

/* PRODUCCION */
import DashboardOP from "./pages/production/DashboardOP";
import TableroOP from "./pages/production/TableroOP";
import OpRegistro from "./pages/production/production-orders/OpRegistro";
import CosteosOP from "./pages/production/costing/CosteosOP";
import EmitirOC_OP from "./pages/production/EmitirOC_OP";
import OrdenProduccionContainer from "./pages/production/production-orders/OrdenProduccionContainer";
import CompraProduccionContainer from "./pages/production/purchases/CompraProduccionContainer";
import HojaCompra from "./pages/production/purchase-sheet/HojaCompra";

/* TRAZABILIDAD */
import TrazabilidadNV from "./pages/traceability/TrazabilidadNV";
import TimelineGlobal from "./pages/traceability/TimelineGlobal";

/* COMPONENTES COMPARTIDOS */
import UnderMaintenance from "./ui/feedback/UnderMaintenance";



/* =========================
   PROTECT ROUTE
 ========================= */

function PrivateRoute({ children }) {

  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
}


/* =========================
   MAIN LAYOUT
 ========================= */

function MainLayout({ children }) {

  const [isSidebarOpen, setIsSidebarOpen] = useState(() => window.innerWidth >= 768);
  const location = useLocation();
  const hasModule = !!getActiveModule(location.pathname);

  return (
    <div className="flex min-h-screen">

      <Sidebar
        isOpen={isSidebarOpen}
        setIsOpen={setIsSidebarOpen}
      />

      <div
        className={`flex-1 transition-all duration-300 ${isSidebarOpen ? "md:ml-64" : "md:ml-20"
          } ml-0 flex flex-col min-h-screen overflow-hidden`}
      >

        <Navbar isSidebarOpen={isSidebarOpen} setIsSidebarOpen={setIsSidebarOpen} />

        {/* Tab bar superior contextual por módulo */}
        <ModuleTabBar isSidebarOpen={isSidebarOpen} />

        <main
          className={`${hasModule ? 'mt-[120px]' : 'mt-[76px]'
            } p-4 sm:p-6 lg:p-8 flex-1 animate-in fade-in duration-500 overflow-auto`}
        >
          {children}
        </main>

      </div>

    </div>
  );
}


/* =========================
   APP
 ========================= */

function App() {

  return (
    <AuthProvider>

      <NotificationProvider>

        <Router>

          <Routes>

            {/* PUBLIC */}
            <Route path="/login" element={<Login />} />

            {/* HOME */}
            <Route
              path="/"
              element={
                <PrivateRoute>
                  <MainLayout>
                    <Welcome />
                  </MainLayout>
                </PrivateRoute>
              }
            />

            {/* PRODUCCION */}
            <Route
              path="/dashboard-op"
              element={<PrivateRoute><MainLayout><DashboardOP /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/produccion/tablero-op"
              element={<PrivateRoute><MainLayout><TableroOP /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/op-registro"
              element={<PrivateRoute><MainLayout><OpRegistro /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/produccion/costeo-mp"
              element={<PrivateRoute><MainLayout><CosteosOP /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/produccion/emitir-oc"
              element={<PrivateRoute><MainLayout><EmitirOC_OP /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/produccion/ordenes"
              element={<PrivateRoute><MainLayout><OrdenProduccionContainer /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/produccion/compras"
              element={<PrivateRoute><MainLayout><CompraProduccionContainer /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/produccion/hoja-compra"
              element={<PrivateRoute><MainLayout><HojaCompra /></MainLayout></PrivateRoute>}
            />


            {/* COMERCIAL */}
            <Route
              path="/registros-nv"
              element={<PrivateRoute><MainLayout><NotaDeVenta /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/detalle-nv"
              element={<PrivateRoute><MainLayout><NotaDeVenta /></MainLayout></PrivateRoute>}
            />


            <Route
              path="/comercial/solicitudes-costos"
              element={<PrivateRoute><MainLayout><SolicitudCostosContainer /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/comercial/solicitudes-cotizaciones"
              element={<PrivateRoute><MainLayout><UnderMaintenance /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/comercial/administracion-negocios"
              element={<PrivateRoute><MainLayout><AdministracionNegocios /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/comercial/gestion-plantillas"
              element={<PrivateRoute><MainLayout><GestionPlantillas /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/comercial/tablero"
              element={<PrivateRoute><MainLayout><TableroComercial /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/comercial/gestion-proyectos"
              element={<PrivateRoute><MainLayout><GestionProyectos /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/comercial/ordenes-produccion"
              element={<PrivateRoute><MainLayout><OrdenesProduccion /></MainLayout></PrivateRoute>}
            />

            {/* TRAZABILIDAD */}
            <Route
              path="/trazabilidad/completa"
              element={<PrivateRoute><MainLayout><TrazabilidadNV /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/trazabilidad/global"
              element={<PrivateRoute><MainLayout><TimelineGlobal /></MainLayout></PrivateRoute>}
            />

            {/* MANTENCION / DESARROLLO */}
            <Route
              path="/comercial/registros-sc"
              element={<PrivateRoute><MainLayout><UnderMaintenance /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/adquisiciones/estado-sc"
              element={<PrivateRoute><MainLayout><UnderMaintenance /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/trazabilidad/alertas"
              element={<PrivateRoute><MainLayout><UnderMaintenance /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/trazabilidad/historial"
              element={<PrivateRoute><MainLayout><UnderMaintenance /></MainLayout></PrivateRoute>}
            />


            {/* GESTION USUARIOS */}
            <Route
              path="/gestion-usuarios/colaboradores"
              element={<PrivateRoute><MainLayout><GestionUsuariosColaboradores /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/gestion-usuarios/clientes"
              element={<PrivateRoute><MainLayout><GestionClientes /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/gestion-usuarios/proveedores"
              element={<PrivateRoute><MainLayout><GestionProveedores /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/gestion-usuarios/vendedores"
              element={<PrivateRoute><MainLayout><GestionVendedores /></MainLayout></PrivateRoute>}
            />

            {/* ADMIN */}
            <Route
              path="/admin/datos-maestros"
              element={<PrivateRoute><MainLayout><GestionDatosMaestros /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/admin/areas"
              element={<PrivateRoute><MainLayout><GestionAreas /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/admin/roles"
              element={<PrivateRoute><MainLayout><GestionRoles /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/admin/permisos"
              element={<PrivateRoute><MainLayout><GestionPermisos /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/admin/roles/:id/permisos"
              element={<PrivateRoute><MainLayout><GestionPermisosRol /></MainLayout></PrivateRoute>}
            />


            {/* MÓDULO LANDING PAGES */}
            <Route
              path="/comercial"
              element={<PrivateRoute><MainLayout><ModuleLandingPage moduleId="comercial" /></MainLayout></PrivateRoute>}
            />
            <Route
              path="/produccion"
              element={<PrivateRoute><MainLayout><ModuleLandingPage moduleId="produccion" /></MainLayout></PrivateRoute>}
            />
            <Route
              path="/gestion-usuarios"
              element={<PrivateRoute><MainLayout><ModuleLandingPage moduleId="gestion-usuarios" /></MainLayout></PrivateRoute>}
            />
            <Route
              path="/trazabilidad"
              element={<PrivateRoute><MainLayout><ModuleLandingPage moduleId="trazabilidad" /></MainLayout></PrivateRoute>}
            />

            {/* DEFAULT */}
            <Route path="*" element={<Navigate to="/" />} />

          </Routes>

        </Router>

      </NotificationProvider>

    </AuthProvider>
  );
}

export default App;