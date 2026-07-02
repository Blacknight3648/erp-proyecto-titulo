import LogoNegro from '../../assets/LogoNegro.svg';

export default function AuthLayout({ children }) {
    return (
        <div className="min-h-screen w-full flex bg-sidebar font-sans text-sidebar-foreground relative overflow-hidden antialiased">

            {/* =========================
               COLUMNA IZQUIERDA
            ========================= */}
            <div className="hidden lg:flex flex-col w-[40%] p-16 xl:p-20 relative overflow-hidden border-r border-sidebar-border bg-sidebar-popup/40">

                {/* Grid técnica sutil */}
                <div className="absolute inset-0 opacity-[0.02] bg-[linear-gradient(to_right,#808080_1px,transparent_1px),linear-gradient(to_bottom,#808080_1px,transparent_1px)] bg-[size:32px_32px]" />

                {/* Glow ambiental */}
                <div className="absolute top-[-10%] left-[-10%] w-[400px] h-[400px] bg-primary/[0.02] blur-[120px] rounded-full" />

                {/* Logo */}
                <div className="relative z-10 flex items-center gap-3">
                    <img src={LogoNegro} alt="Antuan Jury S.A." className="w-11 h-11 object-contain" />

                    <div className="flex flex-col">
                        <span className="text-xl font-bold tracking-tight text-white leading-none">
                            Antuan <span className="text-sidebar-muted font-normal">Jury S.A.</span>
                        </span>

                        <span className="text-[9px] text-sidebar-muted tracking-widest uppercase mt-1 font-bold">
                            Sistemas de Producción
                        </span>
                    </div>
                </div>

                {/* Mensaje */}
                <div className="relative z-10 my-auto pr-8">
                    <h1 className="text-4xl xl:text-5xl font-semibold tracking-tight text-white leading-tight">
                        Control integral de
                        <br />
                        <span className="text-sidebar-muted font-normal">
                            confección institucional.
                        </span>
                    </h1>

                    <p className="mt-6 text-sm text-sidebar-muted leading-relaxed max-w-md">
                        Planificación avanzada de órdenes, tizados y gestión de
                        talleres desde una infraestructura centralizada para
                        uniformidad corporativa.
                    </p>
                </div>

                {/* Footer lateral */}
                <div className="relative z-10 mt-auto text-xs text-sidebar-muted font-medium tracking-wide">
                    Viña del Mar · Chile
                </div>
            </div>

            {/* =========================
               PANEL LOGIN
            ========================= */}
            <div className="flex-1 flex flex-col justify-between p-8 sm:p-16 md:p-20 bg-sidebar relative z-20">

                {/* Logo móvil */}
                <div className="flex lg:hidden items-center gap-2.5 mb-12">
                    <img src={LogoNegro} alt="Antuan Jury S.A." className="w-8 h-8 object-contain" />

                    <span className="text-md font-bold tracking-tight text-white">
                        Antuan <span className="text-sidebar-muted font-normal">Jury S.A.</span>
                    </span>
                </div>

                {/* Contenedor principal */}
                <div className="my-auto w-full max-w-xl lg:ml-8 xl:ml-16">

                    {/* Encabezado */}
                    <div className="mb-10">

                        <div className="mb-6">
                            <span className="text-[10px] uppercase tracking-[0.25em] text-sidebar-muted font-semibold">
                                Sistema Corporativo
                            </span>
                        </div>

                        <h2 className="text-4xl sm:text-5xl font-bold tracking-tight text-white">
                            Iniciar Sesión
                        </h2>

                        <p className="text-sidebar-foreground text-base leading-relaxed mt-4 max-w-md">
                            Introduce tus credenciales autorizadas para acceder
                            a la mesa de control de procesos textiles.
                        </p>

                        <div className="w-16 h-px bg-gradient-to-r from-primary to-transparent mt-6"></div>
                    </div>

                    {/* Tarjeta login */}
                    <div className="relative rounded-3xl border border-sidebar-border bg-sidebar-popup/50 backdrop-blur-xl p-8 shadow-[0_20px_80px_rgba(0,0,0,0.25)]">

                        <div className="absolute inset-0 rounded-3xl bg-gradient-to-b from-white/[0.02] to-transparent pointer-events-none" />

                        <div className="relative">
                            {children}
                        </div>
                    </div>
                </div>

                {/* Footer */}
                <div className="mt-12 pt-6 border-t border-sidebar-border text-left text-xs text-sidebar-muted max-w-xl lg:ml-8 xl:ml-16 w-full flex flex-col sm:flex-row sm:justify-between gap-2">

                    <span>
                        Uso interno restringido · Antuan SA
                    </span>

                    <a
                        href="#"
                        className="text-sidebar-primary/80 hover:text-sidebar-primary transition-colors font-medium"
                    >
                        Soporte TI Planta
                    </a>
                </div>
            </div>

            {/* Glow superior derecho */}
            <div className="absolute top-[-10%] right-[-5%] w-[500px] h-[500px] bg-primary/[0.03] blur-[140px] rounded-full pointer-events-none" />

            {/* Glow inferior */}
            <div className="absolute bottom-[-10%] left-[25%] w-[450px] h-[450px] bg-brand-indigo/[0.02] blur-[140px] rounded-full pointer-events-none" />
        </div>
    );
}
