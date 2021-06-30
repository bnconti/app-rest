
/**
 * Modelo del usuario. Necesito los nulos como tipo alternativo por si no tengo un usuario logueado.
 * Tal vez pueda hacerse de una manera más prolija (crear una interfaz, implementar una clase para 
 * un usuario explícito y otra para uno anónimo).
 */
export class User {
  id: string | null;
  email: string | null;
  token: string | null;

  constructor(id: string | null, email: string | null, token: string | null) {
    this.id = id;
    this.email = email;
    this.token = token;
  }
}