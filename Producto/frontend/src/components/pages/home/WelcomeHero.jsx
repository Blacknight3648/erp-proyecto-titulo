import { ShieldCheck, UserCircle, Activity } from 'lucide-react';

export function WelcomeHero({ userName, role = "Administrador del Sistema" }) {
    const hour = new Date().getHours();
    const greeting =
        hour < 12 ? 'Buenos días' : hour < 18 ? 'Buenas tardes' : 'Buenas noches';

    return (
        <div className="relative overflow-hidden rounded-2xl border border-slate-200/80 bg-white shadow-sm">
            {/* Franja de acento superior */}
            <div className="absolute left-0 right-0 top-0 h-[3px] bg-gradient-to-r from-blue-500 via-indigo-500 to-violet-500" />

            {/* Fondo decorativo */}
            <div className="absolute -right-16 -top-16 h-64 w-64 rounded-full bg-gradient-to-br from-blue-400/8 to-indigo-400/8 blur-3xl" />

            <div className="relative z-10 flex flex-col gap-4 p-6 md:flex-row md:items-center md:justify-between md:p-7">
                {/* Izquierda: Avatar + Saludo */}
                <div className="flex items-center gap-4">
                    <div className="relative flex h-14 w-14 shrink-0 items-center justify-center rounded-2xl bg-gradient-to-br from-blue-50 to-indigo-100 border border-blue-100/60 shadow-inner">
                        <UserCircle className="h-9 w-9 text-blue-600" strokeWidth={1.5} />
                        <span className="absolute -bottom-0.5 -right-0.5 flex h-3.5 w-3.5 items-center justify-center rounded-full bg-white shadow-sm p-0.5">
                            <span className="h-full w-full rounded-full bg-emerald-500 animate-pulse" />
                        </span>
                    </div>

                    <div>
                        <p className="text-[11px] font-semibold uppercase tracking-widest text-slate-400">
                            {greeting}
                        </p>
                        <h1 className="text-xl font-bold tracking-tight text-slate-900 md:text-2xl">
                            {userName ?? 'Usuario'}
                        </h1>
                        <div className="mt-1.5 flex flex-wrap items-center gap-2">
                            <span className="inline-flex items-center gap-1.5 rounded-lg border border-blue-100 bg-blue-50 px-2.5 py-0.5 text-[10px] font-bold uppercase tracking-wider text-blue-700">
                                <ShieldCheck className="h-3 w-3" />
                                {role}
                            </span>
                            <span className="inline-flex items-center gap-1.5 rounded-lg border border-emerald-100 bg-emerald-50 px-2.5 py-0.5 text-[10px] font-bold uppercase tracking-wider text-emerald-600">
                                <Activity className="h-3 w-3" />
                                Sesión Activa
                            </span>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    );
}
