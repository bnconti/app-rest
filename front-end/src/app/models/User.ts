/**
 * Modelo del usuario.
 */
export class User {
  email: string;
  token: string;

  constructor(email: string, token: string) {
    this.email = email;
    this.token = token;
  }
}
