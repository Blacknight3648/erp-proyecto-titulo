// ─────────────────────────────────────────────
// ROLES
// ─────────────────────────────────────────────

export interface Role {
    RolId: number;
    RolNombre: string;
}

export interface CreateRoleDTO {
    RolNombre: string;
}

// ─────────────────────────────────────────────
// ÁREAS
// ─────────────────────────────────────────────

export interface Area {
    AreaId: number;
    AreaNombre: string;
}

export interface CreateAreaDTO {
    AreaNombre: string;
}

// ─────────────────────────────────────────────
// COLABORADOR / USUARIO
// ─────────────────────────────────────────────

/** Datos que devuelve la API para un usuario */
export interface ColaboradorDTO {
    usuarioId: number;
    usuarioRun: string;
    usuarioNombre: string;
    usuarioApellidos: string;
    usuarioEmail: string;
    fechaNacimiento?: string; // ISO date string "YYYY-MM-DD"
    direccion?: string;
    region?: string;
    comuna?: string;
    enabled: boolean;
    roles: string[];  // nombres de roles
    areas: string[];  // nombres de áreas
}

/** Estado interno del formulario en ColaboradorModal */
export interface ColaboradorFormData {
    usuarioNombre: string;
    usuarioRun: string;
    usuarioEmail: string;
    usuarioPassword: string;
    telefono: string;
    area: string;
    rol: string;
    activo: boolean;
}

/** Payload formateado que se envía al guardar */
export interface ColaboradorSavePayload extends ColaboradorFormData {
    usuarioRun: string;      // formateado con puntos y guión: "12.345.678-9"
    telefono: string; // prefijado con "+56" si aplica
}

/** DTO para crear un usuario nuevo (coincide con CreateUserDTO del backend) */
export interface CreateColaboradorDTO {
    usuarioRun: string;
    usuarioNombre: string;
    usuarioApellidos: string;
    usuarioEmail: string;
    usuarioPassword: string;
    telefono: string;
    fechaNacimiento?: string;
    direccion?: string;
    region?: string;
    comuna?: string;
    roles?: string[];
    areas?: string[];
}

// ─────────────────────────────────────────────
// PROPS DEL MODAL
// ─────────────────────────────────────────────

export interface ColaboradorModalProps {
    onClose: () => void;
    onSave: (data: ColaboradorSavePayload) => void;
    collaboratorToEdit?: ColaboradorFormData | null;
}

// ─────────────────────────────────────────────
// RETORNO DE HOOKS
// ─────────────────────────────────────────────

export interface UseRolesReturn {
    roles: Role[];
    loading: boolean;
    fetchRoles: () => Promise<void>;
    createRole: (data: CreateRoleDTO) => Promise<boolean>;
    updateRole: (id: number, data: CreateRoleDTO) => Promise<boolean>;
    deleteRole: (id: number) => Promise<boolean>;
    assignPermissions: (roleId: number, permissionIds: number[]) => Promise<boolean>;
}

export interface UseAreasReturn {
    areas: Area[];
    loading: boolean;
    fetchAreas: () => Promise<void>;
    createArea: (data: CreateAreaDTO) => Promise<boolean>;
    updateArea: (id: number, data: CreateAreaDTO) => Promise<boolean>;
    deleteArea: (id: number) => Promise<boolean>;
}
