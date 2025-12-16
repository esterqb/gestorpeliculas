export interface PeliculaDTO {
    id: number;
    titulo: string;
    duracion?: number | null;
    fechaEstreno: string;
    sinopsis: string;
    valoracion?: number | null;
    directorId: number;
    actoresIds: number[];
    plataformasIds: number[];
    idiomasIds: number[];
    categoriasIds: number[];
    tmdbId: number;
    posterUrl: string;
    bannerUrl?: string;
}