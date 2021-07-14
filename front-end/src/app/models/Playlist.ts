import { User } from '@app/models/User';

/**
 * Modelo de una playlist.
 */
export interface Playlist {
  name: string;
  user: User;
}
