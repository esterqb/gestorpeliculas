export interface PeliculaDTO {
    id: number;
    titulo: string;
    sinopsis: string;
    duracion: number;
    fechaEstreno: string;
    valoracion: number;
    posterUrl: string;
    bannerUrl?: string;
    directorId: number;
    actoresIds: number[];
    categoriasIds: number[];
}

export interface UsuarioDTO {
    id: number;
    username: string;
    email: string;
    rol: string;
}

export interface UsuarioCreateUpdateDTO {
    username: string;
    email: string;
    password: string;
    rol: string;
}