import { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import { AuthProvider, useAuth } from "./contexts/AuthContext";
import { NotificationProvider } from "./contexts/NotificationContext";

import ModernSidebar from "./components/layout/ModernSidebar";
import ModernNavbar from "./components/layout/ModernNavbar";

/* LOGIN */
import Login from "./components/pages/6. LOGIN/Login";

/* HOME */
import Welcome from "./components/pages/7. HOME PAGE/Welcome";


/* COMERCIAL */
import NotaDeVenta from "./components/pages/2. COMERCIAL/NOTA DE VENTA/NotaDeVenta";
import SolicitudCostosContainer from "./components/pages/2. COMERCIAL/COSTEOS/components/SCOS/SolicitudCostosContainer.jsx";
import AdministracionNegocios from "./components/pages/2. COMERCIAL/ADMINISTRACION/AdministracionNegocios";
import GestionPlantillas from "./components/pages/2. COMERCIAL/PLANTILLAS/GestionPlantillas";
import TableroComercial from "./components/pages/2. COMERCIAL/TableroComercial";
import GestionProyectos from "./components/pages/2. COMERCIAL/GESTION PROYECTO/GestionProyectos";
import OrdenesProduccion from "./components/pages/2. COMERCIAL/ORDENES DE PRODUCCION/OrdenesProduccion";

/* GESTION USUARIOS */
import GestionClientes from "./components/pages/9. GESTION USUARIOS/GestionClientes";
import GestionProveedores from "./components/pages/9. GESTION USUARIOS/GestionProveedores";
import GestionUsuariosColaboradores from "./components/pages/9. GESTION USUARIOS/GestionUsuariosColaboradores";

/* ADMIN */
import GestionAreas from "./components/pages/admin/GestionAreas";
import GestionRoles from "./components/pages/admin/GestionRoles";
import GestionPermisosRol from "./components/pages/admin/GestionPermisosRol";


/* PRODUCCION */
import DashboardOP from "./components/pages/4. PRODUCCION/DashboardOP";
import TableroOP from "./components/pages/4. PRODUCCION/TableroOP";
import OpRegistro from "./components/pages/4. PRODUCCION/ORDENES_PRODUCCION/OpRegistro";
import CosteosOP from "./components/pages/4. PRODUCCION/COSTEOS/CosteosOP";
import EmitirOC_OP from "./components/pages/4. PRODUCCION/EmitirOC_OP";
import OrdenProduccionContainer from "./components/pages/4. PRODUCCION/ORDENES_PRODUCCION/OrdenProduccionContainer";
import CompraProduccionContainer from "./components/pages/4. PRODUCCION/COMPRAS/CompraProduccionContainer";
import HojaCompra from "./components/pages/4. PRODUCCION/HOJA DE COMPRA/HojaCompra";

/* TRAZABILIDAD */
import TrazabilidadNV from "./components/pages/5. TRAZABILIDAD/TrazabilidadNV";
import TimelineGlobal from "./components/pages/5. TRAZABILIDAD/TimelineGlobal";



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

  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  return (
    <div className="flex bg-gray-100 min-h-screen">

      <ModernSidebar
        isOpen={isSidebarOpen}
        setIsOpen={setIsSidebarOpen}
      />

      <div
        className={`flex-1 transition-all duration-300 ${isSidebarOpen ? "md:ml-64" : "md:ml-20"
          } ml-0 flex flex-col min-h-screen overflow-hidden`}
      >

        <ModernNavbar isSidebarOpen={isSidebarOpen} />

        <main className="mt-20 p-8 flex-1 animate-in fade-in duration-500 overflow-auto">
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

            {/* ADMIN */}
            <Route
              path="/admin/areas"
              element={<PrivateRoute><MainLayout><GestionAreas /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/admin/roles"
              element={<PrivateRoute><MainLayout><GestionRoles /></MainLayout></PrivateRoute>}
            />

            <Route
              path="/admin/roles/:id/permisos"
              element={<PrivateRoute><MainLayout><GestionPermisosRol /></MainLayout></PrivateRoute>}
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