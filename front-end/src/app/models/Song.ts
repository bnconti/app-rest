/**
 * Modelo de una canción.
 */
export interface Song {
  id?: bigint;
  name: string;
  author: string;
  genre: string;
}
