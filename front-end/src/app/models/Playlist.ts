import { User } from '@app/models/User';
import { Song } from '@app/models/Song';

/**
 * Modelo de una lista de reproducción.
 */
export interface Playlist {
  name: string;
  user: User;
  songs: Song[]
}
